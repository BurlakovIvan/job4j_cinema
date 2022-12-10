package ru.job4j.cinema.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.MovieRepository;
import ru.job4j.cinema.repository.SessionRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс сервис - сеанс
 * @author Burlakov
 */
@Service
@AllArgsConstructor
public class SimpleSessionService implements SessionService {
    private final SessionRepository sessionRepository;

    private MovieRepository movieRepository;
    /**
     * список всех сеансов с названиями фильмов
     * @return Map от Session(ключ) и название фильма(значение)
     */

    @Override
    public Map<Session, String> findAllWithMovieName() {
        Map<Integer, String> movies = movieRepository
                .findAll()
                .stream()
                .collect(Collectors
                        .toMap(Movie::getId, Movie::getName));
        List<Session> sessions = findAll();
        return sessions
                .stream()
                .collect(Collectors
                        .toMap(session -> session,
                                session -> movies.get(session.getMovieId())));
    }

    private List<Session> findAll() {
        return sessionRepository.findAll();
    }

    /**
     * добавление нового сеанса
     * @param session сеанс
     * @return истина если успешно, иначе ложь
     */
    @Override
    public boolean add(Session session) {
        return sessionRepository.add(session);
    }

    /**
     * поиск записи по идентификатору
     * @param id идентификатор сеанса
     * @return Optional от Session
     */
    @Override
    public Optional<Session> findById(int id) {
        return sessionRepository.findById(id);
    }

    /**
     * обновление сеанса
     * @param session сеанс
     * @return истина если успешно, иначе ложь
     */
    @Override
    public boolean update(Session session) {
        return sessionRepository.update(session);
    }

    /**
     * список сеансов фильма
     * @param movieID идентификатор фильма
     * @return List Session
     */
    @Override
    public List<Session> sessionForMovie(int movieID) {
        return sessionRepository.sessionForMovie(movieID);
    }
}
