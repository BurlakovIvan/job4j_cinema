package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.util.LoadProperties;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class JdbcSessionRepositoryTest {
    private final static DataSource POOL = new LoadProperties().loadPool();
    private final static SessionRepository REPOSITORY = new JdbcSessionRepository(POOL);
    private final static String TRUNCATE = "TRUNCATE TABLE %s RESTART IDENTITY;";

    @BeforeAll
    static void init() {
        String stringBuilder = "SET REFERENTIAL_INTEGRITY FALSE;"
                + "\n"
                + String.format(TRUNCATE, "sessions");
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
        Movie movie1 = new Movie(2, "Movie2",
                "Description2", LocalDate.now(), 1, null);
        jdbcMovieRepository.add(movie1);
    }

    @Test
    public void whenAddSessionAndFindById() {
        Session session = new Session(1, 1, "Session");
        var create = REPOSITORY.add(session);
        assertThat(create).isTrue();
        var sessionInRep = REPOSITORY.findById(session.getId());
        assertThat(sessionInRep)
                .isNotEmpty()
                .get()
                .isEqualTo(session);
    }

    @Test
    public void whenFindAllSession() {
        Session session1 = new Session(1, 1, "Session1");
        Session session2 = new Session(2, 2, "Session2");
        var create = REPOSITORY.add(session1);
        assertThat(create).isTrue();
        create = REPOSITORY.add(session2);
        assertThat(create).isTrue();
        var sessionInRep = REPOSITORY.findAll();
        assertThat(sessionInRep)
                .isEqualTo(Map.of(session1, "Movie1", session2, "Movie2"));
    }

    @Test
    public void whenUpdateSession() {
        Session session1 = new Session(1, 1, "Session1");
        Session session2 = new Session(2, 2, "Session2");
        var create = REPOSITORY.add(session1);
        assertThat(create).isTrue();
        REPOSITORY.add(session2);
        String newName = "Session3";
        session2.setName(newName);
        var update = REPOSITORY.update(session2);
        assertThat(update).isTrue();
        var updateSession = REPOSITORY.findById(session2.getId());
        assertThat(updateSession).isNotEmpty();
        assertThat(updateSession.get().getName()).isEqualTo(newName);
    }

    @Test
    public void whenFindSessionForMovie() {
        Session session1 = new Session(1, 1, "Session1");
        Session session2 = new Session(2, 2, "Session2");
        var create = REPOSITORY.add(session1);
        assertThat(create).isTrue();
        create = REPOSITORY.add(session2);
        assertThat(create).isTrue();
        var sessionInRep = REPOSITORY.sessionForMovie(1);
        assertThat(sessionInRep)
                .isEqualTo(List.of(session1));
    }

    @AfterEach
    public void delete() {
        try (Connection cn = POOL.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement(String.format(TRUNCATE, "sessions"))
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
                + String.format(TRUNCATE, "sessions");
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