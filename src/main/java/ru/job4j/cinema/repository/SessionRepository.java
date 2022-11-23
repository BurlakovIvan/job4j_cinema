package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Session;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SessionRepository {

    Map<Session, String> findAll();

    boolean add(Session session);

    Optional<Session> findById(int id);

    boolean update(Session session);

    List<Session> sessionForMovie(int movieID);
}
