package ru.job4j.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.model.Session;
import ru.job4j.repository.SessionRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    public Map<Session, String> findAll() {
        return sessionRepository.findAll();
    }

    public boolean add(Session session) {
        return sessionRepository.add(session);
    }

    public Optional<Session> findById(int id) {
        return sessionRepository.findById(id);
    }

    public boolean update(Session session) {
        return sessionRepository.update(session);
    }

    public List<Session> sessionForMovie(int movieID) {
        return sessionRepository.sessionForMovie(movieID);
    }
}
