package ru.job4j.cinema.service.classes;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserStore;
import ru.job4j.cinema.service.ServiceUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class UserService implements ServiceUser {
    private final UserStore userRepository;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return userRepository.findUserByEmailAndPwd(email, password);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> add(User user) {
        return userRepository.add(user);
    }
}
