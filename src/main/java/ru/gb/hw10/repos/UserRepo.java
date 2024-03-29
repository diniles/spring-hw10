package ru.gb.hw10.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.hw10.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
