package ru.job4j.cinema.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс сервис - пользователь
 * @author Burlakov
 */
@Service
@AllArgsConstructor
public class SimpleUserService implements UserService {
    private final UserRepository userRepository;

    /**
     * список всех пользователей
     * @return List User
     */
    @Override
    public List<User> findAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    /**
     * поиск пользователя по электронной почте и паролю
     * @param email электронная почта пользователя
     * @param password пароль пользователя
     * @return Optional от User
     */
    @Override
    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return userRepository.findUserByEmailAndPwd(email, password);
    }

    /**
     * поиск пользователя по идентификатору
     * @param id идентификатор пользователя
     * @return Optional от User
     */
    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    /**
     * добавление нового пользователя
     * @param user пользователь
     * @return Optional от User
     */
    @Override
    public Optional<User> add(User user) {
        return userRepository.add(user);
    }
}
