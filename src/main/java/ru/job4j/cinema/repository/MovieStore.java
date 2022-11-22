package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Movie;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MovieStore {
    Map<Movie, String> findAllWithCountryName();

    List<Movie> findAll();

    boolean add(Movie movie);

    Optional<Movie> findById(int id);

    boolean updateWithPhoto(Movie movie);

    boolean updateWithoutPhoto(Movie movie);

}
