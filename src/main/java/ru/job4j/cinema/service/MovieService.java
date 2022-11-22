package ru.job4j.cinema.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.repository.MovieRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public Map<Movie, String> findAllWithCountryName() {
        return movieRepository.findAllWithCountryName();
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public boolean add(Movie movie) {
        return movieRepository.add(movie);
    }

    public Optional<Movie> findById(int id) {
        return movieRepository.findById(id);
    }

    public boolean update(Movie movie) {
        if (movie.getPhoto().length > 0) {
            return movieRepository.updateWithPhoto(movie);
        } else {
            return movieRepository.updateWithoutPhoto(movie);
        }
    }

}
