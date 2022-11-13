package ru.job4j.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.Main;
import ru.job4j.constant.NumberCinemaPlace;
import ru.job4j.model.Movie;
import ru.job4j.model.Session;
import ru.job4j.model.Ticket;
import ru.job4j.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class TicketRepositoryTest {
    private final static BasicDataSource POOL = new Main().loadPool();
    private final static TicketRepository REPOSITORY = new TicketRepository(POOL);
    private final static String TRUNCATE = "TRUNCATE TABLE %s RESTART IDENTITY;";

    @BeforeAll
    static void init() {
        String stringBuilder = "SET REFERENTIAL_INTEGRITY FALSE;"
                + "\n"
                + String.format(TRUNCATE, "tickets");
        try (Connection cn = POOL.getConnection();
             PreparedStatement ps = cn.prepareStatement(stringBuilder)
        ) {
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        MovieRepository movieRepository = new MovieRepository(POOL);
        Movie movie = new Movie(1, "Movie1",
                "Description1", LocalDate.now(), 1, null);
        movieRepository.add(movie);
        SessionRepository sessionRepository = new SessionRepository(POOL);
        Session session1 = new Session(1, 1, "Session1");
        sessionRepository.add(session1);
        Session session2 = new Session(2, 1, "Session2");
        sessionRepository.add(session2);
        UserRepository userRepository = new UserRepository(POOL);
        User user = new User(1, "User", "password", "email");
        userRepository.add(user);
    }

    @Test
    public void whenAddTicketAll() {
        var createAll = REPOSITORY.add(1);
        assertThat(createAll).isTrue();
        var ticketInRep = REPOSITORY.findTicketForSessionMovie(1);
        assertThat(ticketInRep.size()).isEqualTo(NumberCinemaPlace.CELL * NumberCinemaPlace.ROW);
    }

    @Test
    public void whenAddTicketUser() {
        var createAll = REPOSITORY.add(1);
        assertThat(createAll).isTrue();
        Ticket ticket = new Ticket(1, 1, 0, 0, 1);
        var create = REPOSITORY.addTicketUser(ticket);
        assertThat(create).isTrue();
        var ticketInRep = REPOSITORY.findTicketForSessionMovie(1);
        assertThat(ticketInRep.size()).isEqualTo(NumberCinemaPlace.CELL * NumberCinemaPlace.ROW);
        assertThat(ticketInRep).contains(ticket);
    }

    @Test
    public void whenAddTicketForSessionMovie() {
        var createAll = REPOSITORY.add(1);
        assertThat(createAll).isTrue();
        createAll = REPOSITORY.add(2);
        assertThat(createAll).isTrue();
        var ticketInRep = REPOSITORY.findTicketForSessionMovie(2);
        assertThat(ticketInRep.size()).isEqualTo(NumberCinemaPlace.CELL * NumberCinemaPlace.ROW);
    }

    @AfterEach
    public void delete() {
        try (Connection cn = POOL.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement(String.format(TRUNCATE, "tickets"))
        ) {
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @AfterAll
    static void deleteAll() {
        String stringBuilder = String.format(TRUNCATE, "movies")
                + "\n"
                + String.format(TRUNCATE, "sessions")
                + "\n"
                + String.format(TRUNCATE, "users")
                + "\n"
                + String.format(TRUNCATE, "tickets");
        try (Connection cn = POOL.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement(stringBuilder)
        ) {
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}