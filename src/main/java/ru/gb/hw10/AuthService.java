package ru.gb.hw10;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gb.hw10.entities.Session;
import ru.gb.hw10.entities.User;
import ru.gb.hw10.entities.UserLoginDto;
import ru.gb.hw10.repos.SessionRepo;
import ru.gb.hw10.repos.UserRepo;

import java.util.List;

@Service
public class AuthService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SessionRepo sessionRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) {
        User userFound = userRepo.findByUsername(user.getUsername());
        if (userFound != null) {
            return userFound;
        }
        return userRepo.save(user);
    }

    public User login(UserLoginDto userLoginDto) {
        User user = userRepo.findByUsername(userLoginDto.getUsername());
        if (user == null) {
            return null;
        }
        boolean passwordMatched = passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword());
        if (!passwordMatched) {
            return null;
        }
        sessionRepo.removeAllByUserId(user.getId());
        Session userSession = new Session();
        userSession.setUserId(user.getId());
        userSession.setUsername(userSession.getUsername());
        sessionRepo.save(userSession);
        return user;
    }

    public boolean logout(Long userId) {
        List<Session> sessions = sessionRepo.removeAllByUserId(userId);
        return sessions != null && !sessions.isEmpty();
    }
}
