package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Session;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Интерфейс сервис - сеанс
 * @author Burlakov
 */
public interface SessionService {
    /**
     * список всех сеансов с названиями фильмов
     * @return Map от Session(ключ) и название фильма(значение)
     */
    Map<Session, String> findAll();

    /**
     * добавление нового сеанса
     * @param session сеанс
     * @return истина если успешно, иначе ложь
     */
    boolean add(Session session);

    /**
     * поиск записи по идентификатору
     * @param id идентификатор сеанса
     * @return Optional от Session
     */
    Optional<Session> findById(int id);

    /**
     * обновление сеанса
     * @param session сеанс
     * @return истина если успешно, иначе ложь
     */
    boolean update(Session session);

    /**
     * список сеансов фильма
     * @param movieID идентификатор фильма
     * @return List Session
     */
    List<Session> sessionForMovie(int movieID);
}
