package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Country;

import java.util.List;
import java.util.Optional;

public interface ServiceCountry {
    List<Country> findAll();

    boolean add(Country country);

    Optional<Country> findById(int id);

    boolean update(Country country);
}
