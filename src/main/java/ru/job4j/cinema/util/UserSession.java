package ru.job4j.cinema.util;

import ru.job4j.cinema.model.User;

import javax.servlet.http.HttpSession;

public final class UserSession {
    private UserSession() {
    }

    public static User user(HttpSession session) {
        User userSession = (User) session.getAttribute("user");
        if (userSession == null) {
            userSession = new User();
            userSession.setUserName("Гость");
        }
        return userSession;
    }
}
