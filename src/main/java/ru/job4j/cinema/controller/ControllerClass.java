package ru.job4j.cinema.controller;

import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

/**
 * Интерфейс контроллер
 * @author Burlakov
 */
public interface ControllerClass {
    /**
     * страница формы добавления
     * @param model Model
     * @param session HttpSession
     * @return форма добавления
     */
    String formAdd(Model model, HttpSession session);

    /**
     * страница вывода списка значений
     * @param model Model
     * @param session HttpSession
     * @return форма вывода списка
     */
    String lists(Model model, HttpSession session);

    /**
     * страница обработки ошибки
     * @param type тип страницы
     * @param message ошибка сообщение
     * @return форма ошибки
     */
    default String failRedirect(int type, String message) {
        return String.format("redirect:/fail?type=%s&message=%s", type, message);
    }
}
