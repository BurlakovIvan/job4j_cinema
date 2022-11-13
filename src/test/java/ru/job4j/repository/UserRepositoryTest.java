package ru.job4j.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.Main;
import ru.job4j.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class UserRepositoryTest {
    private final static BasicDataSource POOL = new Main().loadPool();
    private final static UserRepository REPOSITORY = new UserRepository(POOL);
    private final static String TRUNCATE = "TRUNCATE TABLE users RESTART IDENTITY;";

    @BeforeAll
    static void init() {
        String stringBuilder = "SET REFERENTIAL_INTEGRITY FALSE;"
                + "\n"
                + TRUNCATE;
        try (Connection cn = POOL.getConnection();
             PreparedStatement ps = cn.prepareStatement(stringBuilder)
        ) {
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void whenAddUser() {
        User user = new User(1, "User", "password", "email");
        var createUser = REPOSITORY.add(user);
        assertThat(createUser)
                .isNotEmpty()
                .get()
                .isEqualTo(user);
    }

    @Test
    public void whenFindAllUsers() {
        User user1 = new User(1, "User1", "password", "emailUser1");
        var createUser = REPOSITORY.add(user1);
        assertThat(createUser)
                .isNotEmpty()
                .get()
                .isEqualTo(user1);
        User user2 = new User(2, "User2", "password", "emailUser2");
        createUser = REPOSITORY.add(user2);
        assertThat(createUser)
                .isNotEmpty()
                .get()
                .isEqualTo(user2);
        var findAllUser = REPOSITORY.findAll();
        assertThat(findAllUser).isEqualTo(List.of(user1, user2));
    }

    @Test
    public void whenFindUserById() {
        User user1 = new User(1, "User1", "password", "emailUser1");
        var createUser = REPOSITORY.add(user1);
        assertThat(createUser)
                .isNotEmpty()
                .get()
                .isEqualTo(user1);
        User user2 = new User(2, "User2", "password", "emailUser2");
        createUser = REPOSITORY.add(user2);
        assertThat(createUser)
                .isNotEmpty()
                .get()
                .isEqualTo(user2);
        var findUser = REPOSITORY.findById(2);
        assertThat(findUser)
                .isNotEmpty()
                .get()
                .isEqualTo(user2);
    }

    @Test
    public void whenFindUserByEmailAndPassword() {
        User user1 = new User(1, "User1", "password", "emailUser1");
        var createUser = REPOSITORY.add(user1);
        assertThat(createUser)
                .isNotEmpty()
                .get()
                .isEqualTo(user1);
        User user2 = new User(2, "User2", "password", "emailUser2");
        createUser = REPOSITORY.add(user2);
        assertThat(createUser)
                .isNotEmpty()
                .get()
                .isEqualTo(user2);
        var findUser = REPOSITORY
                .findUserByEmailAndPwd("emailUser1", "password");
        assertThat(findUser)
                .isNotEmpty()
                .get()
                .isEqualTo(user1);
    }

    @AfterEach
    public void delete() {
        try (Connection cn = POOL.getConnection();
             PreparedStatement ps = cn.prepareStatement(TRUNCATE)
        ) {
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}