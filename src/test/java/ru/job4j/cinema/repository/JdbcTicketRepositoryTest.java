package ru.job4j.cinema.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.constant.NumberCinemaPlace;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.util.LoadProperties;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class JdbcTicketRepositoryTest {
    private final static DataSource POOL = new LoadProperties().loadPool();
    private final static TicketRepository REPOSITORY = new JdbcTicketRepository(POOL);
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
        JdbcMovieRepository jdbcMovieRepository = new JdbcMovieRepository(POOL);
        Movie movie = new Movie(1, "Movie1",
                "Description1", LocalDate.now(), 1, null);
        jdbcMovieRepository.add(movie);
        JdbcSessionRepository jdbcSessionRepository = new JdbcSessionRepository(POOL);
        Session session1 = new Session(1, 1, "Session1");
        jdbcSessionRepository.add(session1);
        Session session2 = new Session(2, 1, "Session2");
        jdbcSessionRepository.add(session2);
        JdbcUserRepository jdbcUserRepository = new JdbcUserRepository(POOL);
        User user = new User(1, "User", "password", "email");
        jdbcUserRepository.add(user);
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
        Assertions.assertThat(ticketInRep).contains(ticket);
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