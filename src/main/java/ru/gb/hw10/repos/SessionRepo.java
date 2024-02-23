package ru.gb.hw10.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.hw10.entity.Session;

import java.util.List;

public interface SessionRepo extends JpaRepository<Session, Integer> {
    @Transactional
    List<Session> removeSessionByUserId(int userId);
}

