package ru.gb.hw10.controllers;

import org.springframework.http.ResponseEntity;
import ru.gb.hw10.entities.User;
import ru.gb.hw10.entities.UserLoginDto;

public interface AuthController {
    ResponseEntity<User> register(User user);

    ResponseEntity<Long> login(UserLoginDto loginDto);

    ResponseEntity<Boolean> logout(Long userId);
}
