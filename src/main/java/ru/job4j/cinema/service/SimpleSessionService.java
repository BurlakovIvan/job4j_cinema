package ru.job4j.cinema.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.SessionRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class SimpleSessionService implements SessionService {
    private final SessionRepository sessionRepository;

    @Override
    public Map<Session, String> findAll() {
        return sessionRepository.findAll();
    }

    @Override
    public boolean add(Session session) {
        return sessionRepository.add(session);
    }

    @Override
    public Optional<Session> findById(int id) {
        return sessionRepository.findById(id);
    }

    @Override
    public boolean update(Session session) {
        return sessionRepository.update(session);
    }

    @Override
    public List<Session> sessionForMovie(int movieID) {
        return sessionRepository.sessionForMovie(movieID);
    }
}
