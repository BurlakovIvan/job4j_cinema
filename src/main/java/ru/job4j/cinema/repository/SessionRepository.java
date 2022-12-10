package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Session;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория - сеанс
 * @author Burlakov
 */
public interface SessionRepository {

    /**
     * список всех сеансов
     * @return List от Session
     */
    List<Session> findAll();

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
