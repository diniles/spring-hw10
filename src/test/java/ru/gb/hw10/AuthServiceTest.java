package ru.gb.hw10;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.gb.hw10.entities.Session;
import ru.gb.hw10.entities.User;
import ru.gb.hw10.entities.UserLoginDto;
import ru.gb.hw10.repos.SessionRepo;
import ru.gb.hw10.repos.UserRepo;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AuthServiceTest {
    private AutoCloseable closeable;
    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private UserRepo userRepo;

    @Mock
    private SessionRepo sessionRepo;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void close() throws Exception {
        closeable.close();
    }

    @Test
    void testRegister() {
        User newUser = new User();
        newUser.setUsername("user");
        newUser.setPassword("user");
        newUser.setRole("USER");

        when(userRepo.save(newUser)).thenReturn(newUser);
        when(passwordEncoderMock.encode(Mockito.anyString())).thenReturn("user");
        when(passwordEncoderMock.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        User createdUser = authService.register(newUser);

        System.out.println("Test user: " + createdUser);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepo).save(userCaptor.capture());
        Mockito.verify(userRepo, Mockito.times(1)).save(newUser);
        assertThat(userCaptor.getValue().getUsername()).isEqualTo(newUser.getUsername());
        assertThat(createdUser).isNotNull();
    }

    @Test
    void testRegisterExistingUser() {
        User newUser = new User();
        newUser.setUsername("user");
        newUser.setPassword("user");
        newUser.setRole("USER");

        when(userRepo.findByUsername("user")).thenReturn(newUser);

        User createdUser = authService.register(newUser);

        assertThat(createdUser).isNull();
    }

    @Test
    void testLogin() {
        User newUser = new User();
        newUser.setUsername("user");
        newUser.setPassword("user");
        newUser.setRole("USER");
        newUser.setId(1L);

        when(userRepo.findByUsername("user")).thenReturn(newUser);

        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setUsername("user");
        userLoginDto.setPassword("user");
        when(passwordEncoderMock.matches("user", "user")).thenReturn(true);
        when(sessionRepo.save(Mockito.any(Session.class))).thenReturn(new Session());

        User logged;
        logged = authService.login(userLoginDto);

        Mockito.verify(passwordEncoderMock, Mockito.times(1)).matches("user", "user");
        Mockito.verify(sessionRepo, Mockito.times(1)).save(Mockito.any(Session.class));
        assertThat(logged).isNotNull();

        userLoginDto.setPassword("incorrect");
        when(passwordEncoderMock.matches("incorrect", "user")).thenReturn(false);
        logged = authService.login(userLoginDto);
        Mockito.verify(passwordEncoderMock, Mockito.times(1)).matches("incorrect", "user");
        assertThat(logged).isNull();

        when(userRepo.findByUsername("user")).thenReturn(null);
        logged = authService.login(userLoginDto);
        assertThat(logged).isNull();
    }

    @Test
    void testLogout() {
        List<Session> list = new ArrayList<>();
        Session session = new Session();
        session.setUserId(1L);
        list.add(session);
        when(sessionRepo.removeAllByUserId(1L)).thenReturn(list);
        when(sessionRepo.removeAllByUserId(100L)).thenReturn(null);
        assertThat(authService.logout(1L)).isTrue();
        assertThat(authService.logout(100L)).isFalse();
    }
}
