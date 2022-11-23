package ru.job4j.cinema.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Movie;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@ThreadSafe
@AllArgsConstructor
@Repository
public class JdbcMovieRepository implements MovieRepository {

    private final DataSource pool;
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcMovieRepository.class.getName());
    private final static String SELECT = "SELECT * FROM movies";

    private final static String SELECT_AND_COUNTRY_NAME = """
                                                          SELECT m.id, m.name, m.description, m.created,
                                                              m.photo, m.country_id, c.name AS countryName
                                                          FROM movies m
                                                          LEFT JOIN countries c
                                                          ON m.country_id = c.id
                                                          """;
    private final static String SELECT_WITH_WHERE = String.format("%s WHERE id = ?", SELECT);

    private final static String UPDATE = """
                                         UPDATE movies
                                         SET name = ?, description = ?, country_id = ?, created = ?%s
                                         WHERE id = ?
                                         """;
    private final static String UPDATE_WITH_PHOTO = String.format(UPDATE, ", photo = ?");
    private final static String UPDATE_WITHOUT_PHOTO = String.format(UPDATE, "");
    private final static String INSERT = """
                                         INSERT INTO movies(name, description, created, country_id, photo)
                                         VALUES (?, ?, ?, ?, ?)
                                         """;

    @Override
    public Map<Movie, String> findAllWithCountryName() {
        Map<Movie, String> movies = new HashMap<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(SELECT_AND_COUNTRY_NAME)
        ) {
            try (ResultSet resultSet = ps.executeQuery()) {
                Movie movieResult;
                while (resultSet.next()) {
                    movieResult = newMovie(resultSet);
                    movies.put(movieResult, resultSet.getString("countryName"));
                }
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return movies;
    }

    @Override
    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(SELECT)
        ) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    movies.add(newMovie(resultSet));
                }
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return movies;
    }

    @Override
    public boolean add(Movie movie) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT)
        ) {
            ps.setString(1, movie.getName());
            ps.setString(2, movie.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(movie.getCreated()
                    .atTime(0, 0, 0)));
            ps.setInt(4, movie.getCountryId());
            ps.setBytes(5, movie.getPhoto());
            rsl = ps.executeUpdate() > 0;
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return rsl;
    }

    @Override
    public boolean updateWithPhoto(Movie movie) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE_WITH_PHOTO)) {
            ps.setString(1, movie.getName());
            ps.setString(2, movie.getDescription());
            ps.setInt(3, movie.getCountryId());
            ps.setTimestamp(4, Timestamp.valueOf(movie.getCreated()
                    .atTime(0, 0, 0)));
            ps.setBytes(5, movie.getPhoto());
            ps.setInt(6, movie.getId());
            rsl = ps.executeUpdate() > 0;
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return rsl;
    }

    @Override
    public boolean updateWithoutPhoto(Movie movie) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE_WITHOUT_PHOTO)) {
            ps.setString(1, movie.getName());
            ps.setString(2, movie.getDescription());
            ps.setInt(3, movie.getCountryId());
            ps.setTimestamp(4, Timestamp.valueOf(movie.getCreated()
                    .atTime(0, 0, 0)));
            ps.setInt(5, movie.getId());
            rsl = ps.executeUpdate() > 0;
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return rsl;
    }

    @Override
    public Optional<Movie> findById(int id) {
        Optional<Movie> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(SELECT_WITH_WHERE)
        ) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    rsl = Optional.of(newMovie(resultSet));
                }
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return rsl;
    }

    private Movie newMovie(ResultSet resultSet) throws SQLException {
        return new Movie(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getTimestamp("created").toLocalDateTime()
                        .toLocalDate(),
                resultSet.getInt("country_id"),
                resultSet.getBytes("photo"));
    }

}
