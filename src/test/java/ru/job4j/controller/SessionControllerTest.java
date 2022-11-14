package ru.job4j.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import ru.job4j.constant.TypeFailController;
import ru.job4j.model.Movie;
import ru.job4j.model.Session;
import ru.job4j.model.Ticket;
import ru.job4j.service.MovieService;
import ru.job4j.service.SessionService;
import ru.job4j.service.TicketService;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionControllerTest {

    @Test
    public void whenAddSession() {
        Movie movie1 = new Movie(1, "Movie",
                "Description", LocalDate.now(), 1, null);
        Movie movie2 = new Movie(2, "Movie two",
                "Description movie", LocalDate.now(), 1, null);
        List<Movie> movies = List.of(movie1, movie2);
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        SessionService sessionService = mock(SessionService.class);
        TicketService ticketService = mock(TicketService.class);
        MovieService movieService = mock(MovieService.class);
        when(movieService.findAll()).thenReturn(movies);
        SessionController sessionController =
                new SessionController(sessionService, ticketService, movieService);
        String page = sessionController.formAdd(model, session);
        verify(movieService).findAll();
        assertThat(page).isEqualTo("addSession");
    }

    @Test
    public void whenCreateSessionSuccess() {
        SessionService sessionService = mock(SessionService.class);
        TicketService ticketService = mock(TicketService.class);
        MovieService movieService = mock(MovieService.class);
        when(sessionService.add(Mockito.any())).thenReturn(true);
        SessionController sessionController =
                new SessionController(sessionService, ticketService, movieService);
        String page = sessionController.createSession(Mockito.any());
        verify(sessionService).add(Mockito.any());
        assertThat(page).isEqualTo("redirect:/sessions");
    }

    @Test
    public void whenCreateSessionFail() {
        SessionService sessionService = mock(SessionService.class);
        TicketService ticketService = mock(TicketService.class);
        MovieService movieService = mock(MovieService.class);
        when(sessionService.add(Mockito.any())).thenReturn(false);
        SessionController sessionController =
                new SessionController(sessionService, ticketService, movieService);
        String page = sessionController.createSession(Mockito.any());
        verify(sessionService).add(Mockito.any());
        int type = TypeFailController.SESSION;
        assertThat(page)
                .isEqualTo(String
                        .format("redirect:/fail?type=%s&message=Failed to create", type));
    }

    @Test
    public void whenFindAllSession() {
        Session session1 = new Session(1, 1, "Session1");
        Session session2 = new Session(2, 1, "Session2");
        Map<Session, String> sessions = Map.of(session1, "Movie", session2, "Movie");
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        SessionService sessionService = mock(SessionService.class);
        TicketService ticketService = mock(TicketService.class);
        MovieService movieService = mock(MovieService.class);
        when(sessionService.findAll()).thenReturn(sessions);
        SessionController sessionController =
                new SessionController(sessionService, ticketService, movieService);
        String page = sessionController.lists(model, session);
        verify(sessionService).findAll();
        assertThat(page).isEqualTo("sessions");
    }

    @Test
    public void whenUpdateSessionsByIdSuccess() {
        Movie movie1 = new Movie(1, "Movie",
                "Description", LocalDate.now(), 1, null);
        Movie movie2 = new Movie(2, "Movie two",
                "Description movie", LocalDate.now(), 1, null);
        List<Movie> movies = List.of(movie1, movie2);
        Session sessionMovie = new Session(1, 1, "Session");
        Optional<Session> input = Optional.of(sessionMovie);
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        SessionService sessionService = mock(SessionService.class);
        TicketService ticketService = mock(TicketService.class);
        MovieService movieService = mock(MovieService.class);
        when(sessionService.findById(1)).thenReturn(input);
        when(movieService.findAll()).thenReturn(movies);
        SessionController sessionController =
                new SessionController(sessionService, ticketService, movieService);
        String page = sessionController.formUpdateSession(model, 1, session);
        verify(movieService).findAll();
        assertThat(page).isEqualTo("updateSession");
    }

    @Test
    public void whenUpdateSessionsByIdFail() {
        Optional<Session> input = Optional.empty();
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        SessionService sessionService = mock(SessionService.class);
        TicketService ticketService = mock(TicketService.class);
        MovieService movieService = mock(MovieService.class);
        when(sessionService.findById(1)).thenReturn(input);
        SessionController sessionController =
                new SessionController(sessionService, ticketService, movieService);
        String page = sessionController.formUpdateSession(model, 1, session);
        verify(sessionService).findById(1);
        int type = TypeFailController.SESSION;
        assertThat(page)
                .isEqualTo(String
                        .format("redirect:/fail?type=%s&message=Failed to update", type));
    }

    @Test
    public void whenUpdateSessionsSuccess() {
        SessionService sessionService = mock(SessionService.class);
        TicketService ticketService = mock(TicketService.class);
        MovieService movieService = mock(MovieService.class);
        when(sessionService.update(Mockito.any())).thenReturn(true);
        SessionController sessionController =
                new SessionController(sessionService, ticketService, movieService);
        String page = sessionController.updateSession(Mockito.any());
        verify(sessionService).update(Mockito.any());
        assertThat(page).isEqualTo("redirect:/sessions");
    }

    @Test
    public void whenUpdateSessionsFail() {
        SessionService sessionService = mock(SessionService.class);
        TicketService ticketService = mock(TicketService.class);
        MovieService movieService = mock(MovieService.class);
        when(sessionService.update(Mockito.any())).thenReturn(false);
        SessionController sessionController =
                new SessionController(sessionService, ticketService, movieService);
        String page = sessionController.updateSession(Mockito.any());
        verify(sessionService).update(Mockito.any());
        int type = TypeFailController.SESSION;
        assertThat(page)
                .isEqualTo(String
                        .format("redirect:/fail?type=%s&message=Failed to update", type));
    }

    @Test
    public void whenSessionByIdSuccess() {
        Ticket ticket1 = new Ticket(1, 1, 1, 1, 1);
        Ticket ticket2 = new Ticket(2, 1, 1, 2, 2);
        List<Ticket> tickets = List.of(ticket1, ticket2);
        Movie movie = new Movie(1, "Movie",
                "Description", LocalDate.now(), 1, null);
        Optional<Movie> movieOptional = Optional.of(movie);
        Session sessionMovie = new Session(1, 1, "Session");
        Optional<Session> input = Optional.of(sessionMovie);
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        SessionService sessionService = mock(SessionService.class);
        TicketService ticketService = mock(TicketService.class);
        MovieService movieService = mock(MovieService.class);
        when(sessionService.findById(1)).thenReturn(input);
        when(movieService.findById(input.get().getMovieId())).thenReturn(movieOptional);
        when(ticketService.findTicketForSessionMovie(1)).thenReturn(tickets);
        SessionController sessionController =
                new SessionController(sessionService, ticketService, movieService);
        String page = sessionController.thisSessions(model, 1, session);
        verify(ticketService).findTicketForSessionMovie(1);
        assertThat(page).isEqualTo("place");
    }

    @Test
    public void whenSessionByIdFail() {
        Optional<Session> input = Optional.empty();
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        SessionService sessionService = mock(SessionService.class);
        TicketService ticketService = mock(TicketService.class);
        MovieService movieService = mock(MovieService.class);
        when(sessionService.findById(1)).thenReturn(input);
        SessionController sessionController =
                new SessionController(sessionService, ticketService, movieService);
        String page = sessionController.thisSessions(model, 1, session);
        int type = TypeFailController.SESSION;
        assertThat(page)
                .isEqualTo(String
                        .format("redirect:/fail?type=%s&message=Failed", type));
    }
}