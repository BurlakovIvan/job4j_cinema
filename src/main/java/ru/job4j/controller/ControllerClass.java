package ru.job4j.controller;

import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

public interface ControllerClass {
    String formAdd(Model model, HttpSession session);

    String lists(Model model, HttpSession session);

    default String failRedirect(int type, String message) {
        return String.format("redirect:/fail?type=%s&message=%s", type, message);
    }
}
