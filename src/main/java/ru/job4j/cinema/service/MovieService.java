package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Movie;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Интерфейс сервис - фильм
 * @author Burlakov
 */
public interface MovieService {

    /**
     * все фильмы вместе с названиями стран производства
     * @return Map от Movie(ключ) и название страны(значение)
     */
    Map<Movie, String> findAllWithCountryName();

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
     * обновление фильма
     * @param movie фильм
     * @return истина если успешно, иначе ложь
     */
    boolean update(Movie movie);
}
