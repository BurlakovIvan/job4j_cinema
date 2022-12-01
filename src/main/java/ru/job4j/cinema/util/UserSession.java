package ru.job4j.cinema.util;

import ru.job4j.cinema.model.User;

import javax.servlet.http.HttpSession;

/**
 * Класс сеанс текущего пользователя
 * @author Burlakov
 */
public final class UserSession {
    private UserSession() {
    }

    /**
     * пользователь сеанса
     * @param session HttpSession
     * @return пользователь
     */
    public static User user(HttpSession session) {
        User userSession = (User) session.getAttribute("user");
        if (userSession == null) {
            userSession = new User();
            userSession.setUserName("Гость");
        }
        return userSession;
    }
}
