package ru.job4j.cinema.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.util.UserSession;
import ru.job4j.cinema.constant.TypeFailController;

import javax.servlet.http.HttpSession;

/**
 * Контроллер. Ошибка
 * @author Burlakov
 */
@Controller
@AllArgsConstructor
public class FailController {

    /**
     * форма ошибки
     * @param model Model
     * @param type тип страницы
     * @param message сообщение об ошибки
     * @param session HttpSession
     * @return fail
     */
    @GetMapping("/fail")
    public String fail(Model model,
                       @RequestParam(name = "type") int type,
                       @RequestParam(name = "message") String message,
                       HttpSession session) {
        model.addAttribute("type", type);
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("message", message);
        return "fail";
    }

    /**
     * страница после вывода ошибки
     * @param type тип страницы
     * @return в зависимости от типа (formAddUser, countries, movies, sessions, index)
     */
    @PostMapping("/failRedirect")
    public String failRedirect(@RequestParam("type") int type) {
        return switch (type) {
            case TypeFailController.USER -> "redirect:/formAddUser";
            case TypeFailController.COUNTRY -> "redirect:/countries";
            case TypeFailController.MOVIE -> "redirect:/movies";
            case TypeFailController.SESSION -> "redirect:/sessions";
            default -> "redirect:/index";
        };
    }
}
