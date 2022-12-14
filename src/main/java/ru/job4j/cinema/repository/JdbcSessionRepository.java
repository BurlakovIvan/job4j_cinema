package ru.job4j.cinema.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Класс репозитория - сеанс
 * Потокобезопасен за счет singleton и обработки запроса на стороне SQL
 * @author Burlakov
 */
@ThreadSafe
@AllArgsConstructor
@Repository
public class JdbcSessionRepository implements SessionRepository {
    private final DataSource pool;
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcSessionRepository.class.getName());
    private final static String SELECT = """
                                         SELECT id, movie_id, name
                                         FROM sessions
                                         """;
    private final static String SELECT_SESSIONS = """
                                                  SELECT DISTINCT id, name, movie_id
                                                  FROM sessions
                                                  WHERE movie_id = ?
                                                  ORDER BY name
                                                  """;
    private final static String ORDER_BY = "ORDER BY";
    private final static String SELECT_WITH_WHERE = String.format("%s WHERE id = ?", SELECT);
    private final static String UPDATE = """
                                         UPDATE sessions
                                         SET name = ?, movie_id = ?
                                         WHERE id = ?
                                         """;
    private final static String INSERT = """
                                         INSERT INTO sessions(name, movie_id)
                                         VALUES (?, ?)
                                         """;

    /**
     * список всех сеансов
     * @return List от Session
     */
    @Override
    public List<Session> findAll() {
        List<Session> sessions = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     String.format("%s %s name", SELECT, ORDER_BY))
        ) {
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

    /**
     * добавление нового сеанса
     * @param session сеанс
     * @return истина если успешно, иначе ложь
     */
    @Override
    public boolean add(Session session) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, session.getName());
            ps.setInt(2, session.getMovieId());
            rsl = ps.executeUpdate() > 0;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    session.setId(keys.getInt(0));
                }
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return rsl;
    }

    /**
     * обновление сеанса
     * @param session сеанс
     * @return истина если успешно, иначе ложь
     */
    @Override
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

    /**
     * поиск записи по идентификатору
     * @param id идентификатор сеанса
     * @return Optional от Session
     */
    @Override
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

    /**
     * список сеансов фильма
     * @param movieID идентификатор фильма
     * @return List Session
     */
    @Override
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

    /**
     * приватный метод создания нового объекта типа сеанс
     * @param resultSet результат запроса
     * @return новый объект типа сеанс
     * @throws SQLException ошибка
     */
    private Session newSession(ResultSet resultSet) throws SQLException {
        return new Session(
                resultSet.getInt("id"),
                resultSet.getInt("movie_id"),
                resultSet.getString("name"));
    }
}
