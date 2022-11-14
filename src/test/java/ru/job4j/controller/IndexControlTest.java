package ru.job4j.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.model.Movie;
import ru.job4j.model.Session;
import ru.job4j.service.MovieService;
import ru.job4j.service.SessionService;

import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class IndexControlTest {

    @Test
    public void whenIndexCall() {
        Movie movie1 = new Movie(1, "Movie",
                "Description", LocalDate.now(), 1, null);
        Movie movie2 = new Movie(2, "Movie two",
                "Description movie", LocalDate.now(), 1, null);
        Map<Movie, String> movies = Map.of(movie1, "Country One",
                movie2, "Country Two");
        Session session1 = new Session(1, 1, "Session1");
        Session session2 = new Session(2, 2, "Session2");
        Map<Session, String> sessions = Map.of(session1, "Movie", session2, "Movie Two");
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        MovieService movieService = mock(MovieService.class);
        SessionService sessionService = mock(SessionService.class);
        when(movieService.findAllWithCountryName()).thenReturn(movies);
        when(sessionService.findAll()).thenReturn(sessions);
        IndexControl indexControl = new IndexControl(movieService, sessionService);
        String page = indexControl.index(model, session);
        assertThat(page).isEqualTo("index");
    }
}