package ru.job4j.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Country;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@AllArgsConstructor
@Repository
public class CountryRepository {
    private final BasicDataSource pool;
    private static final Logger LOGGER = LoggerFactory.getLogger(CountryRepository.class.getName());
    private final static String SELECT = "SELECT * FROM countries";
    private final static String SELECT_WITH_WHERE = String.format("%s WHERE id = ?", SELECT);
    private final static String UPDATE = """
                                         UPDATE countries
                                         SET name = ?
                                         WHERE id = ?
                                         """;
    private final static String INSERT = """
                                         INSERT INTO countries(name)
                                         VALUES (?)
                                         """;

    public List<Country> findAll() {
        List<Country> countries = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(String.format("%s ORDER BY name", SELECT))
        ) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    countries.add(newCountry(resultSet));
                }
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return countries;
    }

    public boolean add(Country country) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT)
        ) {
            ps.setString(1, country.getName());
            rsl = ps.executeUpdate() > 0;
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return rsl;
    }

    public boolean update(Country country) {
        boolean rsl  = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            ps.setString(1, country.getName());
            ps.setInt(2, country.getId());
            rsl = ps.executeUpdate() > 0;
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return rsl;
    }

    public Optional<Country> findById(int id) {
        Optional<Country> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(SELECT_WITH_WHERE)
        ) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    rsl = Optional.of(newCountry(resultSet));
                }
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return rsl;
    }

    private Country newCountry(ResultSet resultSet) throws SQLException {
        return new Country(
                resultSet.getInt("id"),
                resultSet.getString("name"));
    }
}