package ru.job4j.cinema.repository;

import org.junit.jupiter.api.*;
import ru.job4j.cinema.model.Country;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.util.LoadProperties;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class JdbcMovieRepositoryTest {
    private final static DataSource POOL = new LoadProperties().loadPool();
    private final static MovieRepository REPOSITORY = new JdbcMovieRepository(POOL);
    private final static String TRUNCATE = "TRUNCATE TABLE %s RESTART IDENTITY;";

    @BeforeAll
    static void init() {
        String stringBuilder = "SET REFERENTIAL_INTEGRITY FALSE;"
                + "\n"
                + String.format(TRUNCATE, "movies");
        try (Connection cn = POOL.getConnection();
             PreparedStatement ps = cn.prepareStatement(stringBuilder)
        ) {
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JdbcCountryRepository repositoryCountry = new JdbcCountryRepository(POOL);
        Country country = new Country(1, "Country");
        repositoryCountry.add(country);
    }

    @Test
    public void whenAddMovie() {
        Movie movie = new Movie(1, "Movie",
                "Description", LocalDate.now(), 1, null);
        var create = REPOSITORY.add(movie);
        assertThat(create).isTrue();
        var movieInRep = REPOSITORY.findById(movie.getId());
        assertThat(movieInRep)
                .isNotEmpty()
                .get()
                .isEqualTo(movie);
    }

    @Test
    public void whenFindAllMovies() {
        Movie movie1 = new Movie(1, "Movie",
                "Description", LocalDate.now(), 1, null);
        Movie movie2 = new Movie(2, "Movie two",
                "Description movie", LocalDate.now(), 1, null);
        var create = REPOSITORY.add(movie1);
        assertThat(create).isTrue();
        create = REPOSITORY.add(movie2);
        assertThat(create).isTrue();
        var movieInRep = REPOSITORY.findAll();
        assertThat(movieInRep)
                .isEqualTo(List.of(movie1, movie2));
    }

    @Test
    public void whenUpdateMovieWithoutPhoto() {
        Movie movie1 = new Movie(1, "Movie",
                "Description", LocalDate.now(), 1, null);
        Movie movie2 = new Movie(2, "Movie two",
                "Description movie", LocalDate.now(), 1, null);
        var create = REPOSITORY.add(movie1);
        assertThat(create).isTrue();
        create = REPOSITORY.add(movie2);
        assertThat(create).isTrue();
        String newName = "Film";
        movie1.setName(newName);
        var update = REPOSITORY.updateWithoutPhoto(movie1);
        assertThat(update).isTrue();
        var updateMovie = REPOSITORY.findById(movie1.getId());
        assertThat(updateMovie).isNotEmpty();
        assertThat(updateMovie.get().getName()).isEqualTo(newName);
    }

    @Test
    public void whenUpdateMovieWithPhoto() {
        Movie movie1 = new Movie(1, "Movie",
                "Description", LocalDate.now(), 1, null);
        Movie movie2 = new Movie(2, "Movie two",
                "Description movie", LocalDate.now(), 1, null);
        var create = REPOSITORY.add(movie1);
        assertThat(create).isTrue();
        create = REPOSITORY.add(movie2);
        assertThat(create).isTrue();
        movie1.setName("Film");
        byte[] newPhoto = new byte[]{12, 45, 48};
        movie1.setPhoto(newPhoto);
        var update = REPOSITORY.updateWithPhoto(movie1);
        assertThat(update).isTrue();
        var updateMovie = REPOSITORY.findById(movie1.getId());
        assertThat(updateMovie).isNotEmpty();
        assertThat(updateMovie.get().getPhoto()).isEqualTo(newPhoto);
    }

    @AfterEach
    public void delete() {
        try (Connection cn = POOL.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement(String.format(TRUNCATE, "movies"))
        ) {
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @AfterAll
    static void deleteAll() {
        String stringBuilder = String.format(TRUNCATE, "countries")
                + "\n"
                + String.format(TRUNCATE, "movies");
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