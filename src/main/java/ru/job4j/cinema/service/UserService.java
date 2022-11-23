package ru.job4j.cinema.service;

import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findUserByEmailAndPwd(String email, String password);

    Optional<User> findById(int id);

    Optional<User> add(User user);
}
