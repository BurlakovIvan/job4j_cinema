package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Session;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ServiceSession {
    Map<Session, String> findAll();

    boolean add(Session session);

    Optional<Session> findById(int id);

    boolean update(Session session);

    List<Session> sessionForMovie(int movieID);
}
