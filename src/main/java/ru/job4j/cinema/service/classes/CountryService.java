package ru.job4j.cinema.service.classes;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Country;
import ru.job4j.cinema.repository.CountryStore;
import ru.job4j.cinema.service.ServiceCountry;

import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class CountryService implements ServiceCountry {
    private final CountryStore countryRepository;

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    @Override
    public boolean add(Country country) {
        return countryRepository.add(country);
    }

    @Override
    public Optional<Country> findById(int id) {
        return countryRepository.findById(id);
    }

    @Override
    public boolean update(Country country) {
        return countryRepository.update(country);
    }
}
