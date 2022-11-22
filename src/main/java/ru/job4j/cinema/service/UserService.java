package ru.job4j.cinema.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return userRepository.findUserByEmailAndPwd(email, password);
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> add(User user) {
        return userRepository.add(user);
    }
}
