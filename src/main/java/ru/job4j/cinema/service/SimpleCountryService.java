package ru.job4j.cinema.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Country;
import ru.job4j.cinema.repository.CountryRepository;

import java.util.List;
import java.util.Optional;

/**
 * Класс сервис - страна
 * @author Burlakov
 */
@Service
@AllArgsConstructor
public class SimpleCountryService implements CountryService {
    private final CountryRepository countryRepository;

    /**
     * список всех стран
     * @return List Country
     */
    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    /**
     * добавление новой страны
     * @param country страна
     * @return истина если успешно, иначе ложь
     */
    @Override
    public boolean add(Country country) {
        return countryRepository.add(country);
    }

    /**
     * поиск записи по идентификатору
     * @param id идентификатор страны
     * @return Optional от Country
     */
    @Override
    public Optional<Country> findById(int id) {
        return countryRepository.findById(id);
    }

    /**
     * обновление записи
     * @param country страна
     * @return истина если успешно, иначе ложь
     */
    @Override
    public boolean update(Country country) {
        return countryRepository.update(country);
    }
}
