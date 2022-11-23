package ru.job4j.cinema.repository;

import org.junit.jupiter.api.*;
import ru.job4j.cinema.model.Country;
import ru.job4j.cinema.util.LoadProperties;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class JdbcCountryRepositoryTest {

    private final static DataSource POOL = new LoadProperties().loadPool();
    private final static CountryRepository REPOSITORY = new JdbcCountryRepository(POOL);
    private final static String TRUNCATE = "TRUNCATE TABLE countries RESTART IDENTITY;";

    @BeforeAll
    static void init() {
        String stringBuilder = "SET REFERENTIAL_INTEGRITY FALSE;"
                + "\n"
                + TRUNCATE;
        try (Connection cn = POOL.getConnection();
             PreparedStatement ps = cn.prepareStatement(stringBuilder)) {
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void whenCreateCountry() {
        Country country1 = new Country(1, "USA");
        Country country2 = new Country(2, "Russia");
        var create = REPOSITORY.add(country1);
        REPOSITORY.add(country2);
        assertThat(create).isTrue();
        var countryInRep = REPOSITORY.findAll();
        assertThat(countryInRep)
                .isEqualTo(List.of(country2, country1));
    }

    @Test
    public void whenFindAllCountries() {
        Country country1 = new Country(1, "USA");
        Country country2 = new Country(2, "Russia");
        Country country3 = new Country(3, "Canada");
        REPOSITORY.add(country1);
        REPOSITORY.add(country2);
        REPOSITORY.add(country3);
        var countryInRep = REPOSITORY.findAll();
        assertThat(countryInRep)
                .isEqualTo(List.of(country3, country2, country1));
    }

    @Test
    public void whenFindCountryById() {
        Country country1 = new Country(1, "USA");
        Country country2 = new Country(2, "Russia");
        REPOSITORY.add(country1);
        REPOSITORY.add(country2);
        var countryInRep = REPOSITORY.findById(country2.getId());
        assertThat(countryInRep)
                .isNotEmpty()
                .get()
                .isEqualTo(country2);
    }

    @Test
    public void whenUpdateCountry() {
        Country country1 = new Country(1, "USA");
        Country country2 = new Country(2, "Russia");
        REPOSITORY.add(country1);
        REPOSITORY.add(country2);
        country2.setName("Canada");
        var updateCountry = REPOSITORY.update(country2);
        assertThat(updateCountry).isTrue();
        var countryInRep = REPOSITORY.findById(country2.getId());
        assertThat(countryInRep).isNotEmpty();
        assertThat(countryInRep.get().getName()).isEqualTo("Canada");
    }

    @AfterEach
    public void delete() {
        try (Connection cn = POOL.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement(TRUNCATE)) {
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}