package ru.job4j.cinema.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Country;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.repository.CountryRepository;
import ru.job4j.cinema.repository.MovieRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс сервис - фильм
 * @author Burlakov
 */
@Service
@AllArgsConstructor
public class SimpleMovieService implements MovieService {
    private final MovieRepository movieRepository;

    private final CountryRepository countryRepository;

    /**
     * все фильмы вместе с названиями стран производства
     * @return Map от Movie(ключ) и название страны(значение)
     */
    @Override
    public Map<Movie, String> findAllWithCountryName() {
        Map<Integer, String> countries = countryRepository
                .findAll()
                .stream()
                .collect(Collectors
                        .toMap(Country::getId, Country::getName));
        List<Movie> movies = findAll();
        return movies
                .stream()
                .collect(Collectors
                        .toMap(movie -> movie,
                                movie -> countries.get(movie.getCountryId())));
    }

    /**
     * список всех фильмов
     * @return List Movie
     */
    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    /**
     * добавление нового фильма
     * @param movie фильм
     * @return истина если успешно, иначе ложь
     */
    @Override
    public boolean add(Movie movie) {
        return movieRepository.add(movie);
    }

    /**
     * поиск записи по идентификатору
     * @param id идентификатор фильма
     * @return Optional от Movie
     */
    @Override
    public Optional<Movie> findById(int id) {
        return movieRepository.findById(id);
    }

    /**
     * обновление фильма
     * @param movie фильм
     * @return истина если успешно, иначе ложь
     */
    @Override
    public boolean update(Movie movie) {
        if (movie.getPhoto().length > 0) {
            return movieRepository.updateWithPhoto(movie);
        } else {
            return movieRepository.updateWithoutPhoto(movie);
        }
    }

}
