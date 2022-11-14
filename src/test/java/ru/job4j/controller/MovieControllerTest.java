package ru.job4j.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.constant.TypeFailController;
import ru.job4j.model.Country;
import ru.job4j.model.Movie;
import ru.job4j.model.Session;
import ru.job4j.service.CountryService;
import ru.job4j.service.MovieService;
import ru.job4j.service.SessionService;

import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieControllerTest {

    @Test
    public void whenAddMovie() {
        List<Country> countries = Arrays.asList(
                new Country(1, "Russia"),
                new Country(2, "Canada")
        );
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        MovieService movieService = mock(MovieService.class);
        CountryService countryService = mock(CountryService.class);
        SessionService sessionService = mock(SessionService.class);
        when(countryService.findAll()).thenReturn(countries);
        MovieController movieController =
                new MovieController(movieService, countryService, sessionService);
        String page = movieController.formAdd(model, session);
        assertThat(page).isEqualTo("addMovie");
    }

    @Test
    public void whenCreateMovieSuccess() {
        Movie movie = new Movie();
        MultipartFile file = mock(MultipartFile.class);
        MovieService movieService = mock(MovieService.class);
        CountryService countryService = mock(CountryService.class);
        SessionService sessionService = mock(SessionService.class);
        when(movieService.add(Mockito.any())).thenReturn(true);
        MovieController movieController =
                new MovieController(movieService, countryService, sessionService);
        String page = "None";
        try {
            when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
            page = movieController.createMovie(movie, file, LocalDate.now());
            verify(movieService).add(Mockito.any());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        assertThat(page).isEqualTo("redirect:/movies");
    }

    @Test
    public void whenCreateMovieFail() {
        Movie movie = new Movie();
        MultipartFile file = mock(MultipartFile.class);
        MovieService movieService = mock(MovieService.class);
        CountryService countryService = mock(CountryService.class);
        SessionService sessionService = mock(SessionService.class);
        when(movieService.add(Mockito.any())).thenReturn(false);
        MovieController movieController =
                new MovieController(movieService, countryService, sessionService);
        String page = "None";
        try {
            when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
            page = movieController.createMovie(movie, file, LocalDate.now());
            verify(movieService).add(Mockito.any());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int type = TypeFailController.MOVIE;
        assertThat(page)
                .isEqualTo(String
                        .format("redirect:/fail?type=%s&message=Failed to create", type));
    }

    @Test
    public void whenFindAllMovies() {
        Movie movie1 = new Movie(1, "Movie",
                "Description", LocalDate.now(), 1, null);
        Movie movie2 = new Movie(2, "Movie two",
                "Description movie", LocalDate.now(), 1, null);
        Map<Movie, String> movies = Map.of(movie1, "Country One",
                movie2, "Country Two");
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        MovieService movieService = mock(MovieService.class);
        CountryService countryService = mock(CountryService.class);
        SessionService sessionService = mock(SessionService.class);
        when(movieService.findAllWithCountryName()).thenReturn(movies);
        MovieController movieController =
                new MovieController(movieService, countryService, sessionService);
        String page = movieController.lists(model, session);
        assertThat(page).isEqualTo("movies");
    }

    @Test
    public void whenUpdateMovieByIdSuccess() {
        Movie movie = new Movie(1, "Movie",
                "Description", LocalDate.now(), 1, null);
        Optional<Movie> input = Optional.of(movie);
        List<Country> countries = Arrays.asList(
                new Country(1, "Russia"),
                new Country(2, "Canada")
        );
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        MovieService movieService = mock(MovieService.class);
        CountryService countryService = mock(CountryService.class);
        SessionService sessionService = mock(SessionService.class);
        MovieController movieController =
                new MovieController(movieService, countryService, sessionService);
        when(movieService.findById(1)).thenReturn(input);
        when(countryService.findAll()).thenReturn(countries);
        String page = movieController.formUpdateMovie(model, 1, session);
        assertThat(page).isEqualTo("updateMovie");
    }

    @Test
    public void whenUpdateMovieByIdFail() {
        Optional<Movie> input = Optional.empty();
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        MovieService movieService = mock(MovieService.class);
        CountryService countryService = mock(CountryService.class);
        SessionService sessionService = mock(SessionService.class);
        MovieController movieController =
                new MovieController(movieService, countryService, sessionService);
        when(movieService.findById(1)).thenReturn(input);
        String page = movieController.formUpdateMovie(model, 1, session);
        int type = TypeFailController.MOVIE;
        assertThat(page)
                .isEqualTo(String
                        .format("redirect:/fail?type=%s&message=Failed to update", type));
    }

    @Test
    public void whenUpdateMovieSuccess() {
        Movie movie = new Movie();
        MultipartFile file = mock(MultipartFile.class);
        MovieService movieService = mock(MovieService.class);
        CountryService countryService = mock(CountryService.class);
        SessionService sessionService = mock(SessionService.class);
        when(movieService.update(Mockito.any())).thenReturn(true);
        MovieController movieController =
                new MovieController(movieService, countryService, sessionService);
        String page = "None";
        try {
            when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
            page = movieController.updateMovie(movie, file, LocalDate.now());
            verify(movieService).update(Mockito.any());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        assertThat(page).isEqualTo("redirect:/movies");
    }

    @Test
    public void whenUpdateMovieFail() {
        Movie movie = new Movie();
        MultipartFile file = mock(MultipartFile.class);
        MovieService movieService = mock(MovieService.class);
        CountryService countryService = mock(CountryService.class);
        SessionService sessionService = mock(SessionService.class);
        when(movieService.update(Mockito.any())).thenReturn(false);
        MovieController movieController =
                new MovieController(movieService, countryService, sessionService);
        String page = "None";
        try {
            when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
            page = movieController.updateMovie(movie, file, LocalDate.now());
            verify(movieService).update(Mockito.any());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int type = TypeFailController.MOVIE;
        assertThat(page)
                .isEqualTo(String
                        .format("redirect:/fail?type=%s&message=Failed to update", type));
    }

    @Test
    public void whenMovieByIdSuccess() {
        Movie movie = new Movie(1, "Movie",
                "Description", LocalDate.now(), 1, null);
        Optional<Movie> input = Optional.of(movie);
        List<Session> sessions = Arrays.asList(
                new Session(1, 1, "Session1"),
                new Session(2, 2, "Session2")
        );
        Optional<Country> country = Optional.of(new Country(1, "Russia"));
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        MovieService movieService = mock(MovieService.class);
        CountryService countryService = mock(CountryService.class);
        SessionService sessionService = mock(SessionService.class);
        when(movieService.findById(1)).thenReturn(input);
        when(sessionService.sessionForMovie(input.get().getId())).thenReturn(sessions);
        when(countryService.findById(input.get().getCountryId())).thenReturn(country);
        MovieController movieController =
                new MovieController(movieService, countryService, sessionService);
        String page = movieController.theMovie(model, 1, session);
        assertThat(page).isEqualTo("movie");

    }

    @Test
    public void whenMovieByIdFail() {
        Optional<Movie> input = Optional.empty();
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        MovieService movieService = mock(MovieService.class);
        CountryService countryService = mock(CountryService.class);
        SessionService sessionService = mock(SessionService.class);
        when(movieService.findById(1)).thenReturn(input);
        MovieController movieController =
                new MovieController(movieService, countryService, sessionService);
        String page = movieController.theMovie(model, 1, session);
        verify(movieService).findById(1);
        int type = TypeFailController.MOVIE;
        assertThat(page)
                .isEqualTo(String
                        .format("redirect:/fail?type=%s&message=Failed", type));
    }
}