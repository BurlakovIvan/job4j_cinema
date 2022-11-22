package ru.job4j.cinema.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@ThreadSafe
@AllArgsConstructor
@Repository
public class SessionRepository {
    private final BasicDataSource pool;
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionRepository.class.getName());
    private final static String SELECT = """
                                         SELECT s.id AS id, s.movie_id AS movie_id, s.name AS name,
                                          m.name AS movieName
                                          FROM sessions as s
                                         LEFT JOIN movies m
                                         ON s.movie_id = m.id
                                         """;
    private final static String SELECT_SESSIONS = """
                                                  SELECT DISTINCT id, name, movie_id
                                                  FROM sessions
                                                  WHERE movie_id = ?
                                                  ORDER BY name
                                                  """;
    private final static String ORDER_BY = "ORDER BY";
    private final static String SELECT_WITH_WHERE = String.format("%s WHERE s.id = ?", SELECT);
    private final static String UPDATE = """
                                         UPDATE sessions
                                         SET name = ?, movie_id = ?
                                         WHERE id = ?
                                         """;
    private final static String INSERT = """
                                         INSERT INTO sessions(name, movie_id)
                                         VALUES (?, ?)
                                         """;

    public Map<Session, String> findAll() {
        Map<Session, String> sessions = new HashMap<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     String.format("%s %s movie_id, name", SELECT, ORDER_BY))
        ) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    sessions.put(newSession(resultSet),
                            resultSet.getString("movieName"));
                }
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return sessions;
    }

    public boolean add(Session session) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT)
        ) {
            ps.setString(1, session.getName());
            ps.setInt(2, session.getMovieId());
            rsl = ps.executeUpdate() > 0;
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return rsl;
    }

    public boolean update(Session session) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            ps.setString(1, session.getName());
            ps.setInt(2, session.getMovieId());
            ps.setInt(3, session.getId());
            rsl = ps.executeUpdate() > 0;
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return rsl;
    }

    public Optional<Session> findById(int id) {
        Optional<Session> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(SELECT_WITH_WHERE)
        ) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    rsl = Optional.of(newSession(resultSet));
                }
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return rsl;
    }

    public List<Session> sessionForMovie(int movieID) {
        List<Session> sessions = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(SELECT_SESSIONS)
        ) {
            ps.setInt(1, movieID);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    sessions.add(newSession(resultSet));
                }
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return sessions;
    }

    private Session newSession(ResultSet resultSet) throws SQLException {
        return new Session(
                resultSet.getInt("id"),
                resultSet.getInt("movie_id"),
                resultSet.getString("name"));
    }
}
