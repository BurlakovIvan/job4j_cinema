package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Country;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс сервис - страна
 * @author Burlakov
 */
public interface CountryService {
    /**
     * список всех стран
     * @return List Country
     */
    List<Country> findAll();

    /**
     * добавление новой страны
     * @param country страна
     * @return истина если успешно, иначе ложь
     */
    boolean add(Country country);

    /**
     * поиск записи по идентификатору
     * @param id идентификатор страны
     * @return Optional от Country
     */
    Optional<Country> findById(int id);

    /**
     * обновление записи
     * @param country страна
     * @return истина если успешно, иначе ложь
     */
    boolean update(Country country);
}
