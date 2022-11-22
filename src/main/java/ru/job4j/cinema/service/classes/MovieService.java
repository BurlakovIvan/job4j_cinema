package ru.job4j.cinema.service.classes;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.repository.MovieStore;
import ru.job4j.cinema.service.ServiceMovie;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class MovieService implements ServiceMovie {
    private final MovieStore movieRepository;

    @Override
    public Map<Movie, String> findAllWithCountryName() {
        return movieRepository.findAllWithCountryName();
    }

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public boolean add(Movie movie) {
        return movieRepository.add(movie);
    }

    @Override
    public Optional<Movie> findById(int id) {
        return movieRepository.findById(id);
    }

    @Override
    public boolean update(Movie movie) {
        if (movie.getPhoto().length > 0) {
            return movieRepository.updateWithPhoto(movie);
        } else {
            return movieRepository.updateWithoutPhoto(movie);
        }
    }

}
