package ru.job4j.cinema.service;

import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс сервис - пользователь
 * @author Burlakov
 */
public interface UserService {

    /**
     * список всех пользователей
     * @return List User
     */
    List<User> findAll();

    /**
     * поиск пользователя по электронной почте и паролю
     * @param email электронная почта пользователя
     * @param password пароль пользователя
     * @return Optional от User
     */
    Optional<User> findUserByEmailAndPwd(String email, String password);

    /**
     * поиск пользователя по идентификатору
     * @param id идентификатор пользователя
     * @return Optional от User
     */
    Optional<User> findById(int id);

    /**
     * добавление нового пользователя
     * @param user пользователь
     * @return Optional от User
     */
    Optional<User> add(User user);
}
