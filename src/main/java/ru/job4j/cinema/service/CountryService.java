package ru.job4j.cinema.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Country;
import ru.job4j.cinema.repository.CountryRepository;

import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    public boolean add(Country country) {
        return countryRepository.add(country);
    }

    public Optional<Country> findById(int id) {
        return countryRepository.findById(id);
    }

    public boolean update(Country country) {
        return countryRepository.update(country);
    }
}
