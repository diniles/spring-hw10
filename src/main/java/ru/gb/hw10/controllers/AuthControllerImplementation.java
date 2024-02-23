package ru.gb.hw10.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.hw10.AuthService;
import ru.gb.hw10.entities.User;
import ru.gb.hw10.entities.UserLoginDto;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthControllerImplementation implements AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = authService.register(user);
        if (registeredUser == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(registeredUser);
    }

    @GetMapping("/logout/{id}")
    public ResponseEntity<Boolean> logout(@PathVariable("id") Long id) {
        if (authService.logout(id)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody UserLoginDto userLoginDto) {
        User user = authService.login(userLoginDto);
        if (user == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(user.getId());
    }
}
