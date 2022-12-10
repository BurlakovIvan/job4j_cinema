package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Movie;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория - фильм
 * @author Burlakov
 */
public interface MovieRepository {
    /**
     * список всех фильмов
     * @return List Movie
     */
    List<Movie> findAll();

    /**
     * добавление нового фильма
     * @param movie фильм
     * @return истина если успешно, иначе ложь
     */
    boolean add(Movie movie);

    /**
     * поиск записи по идентификатору
     * @param id идентификатор фильма
     * @return Optional от Movie
     */
    Optional<Movie> findById(int id);

    /**
     * обновление фильма вместе с постером
     * @param movie фильм
     * @return истина если успешно, иначе ложь
     */
    boolean updateWithPhoto(Movie movie);

    /**
     * обновление фильма без постера
     * @param movie фильм
     * @return истина если успешно, иначе ложь
     */
    boolean updateWithoutPhoto(Movie movie);

}
