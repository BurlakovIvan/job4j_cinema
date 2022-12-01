package ru.job4j.cinema.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.service.MovieService;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.util.UserSession;

import javax.servlet.http.HttpSession;

/**
 * Контроллер. Индексная страница
 * @author Burlakov
 */
@Controller
@AllArgsConstructor
public class IndexControl {

    private final MovieService movieService;
    private final SessionService sessionService;

    /**
     * стартовая страница
     * @param model Model
     * @param session HttpSession
     * @return index
     */
    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("movies", movieService.findAllWithCountryName());
        model.addAttribute("sessions", sessionService.findAll());
        return "index";
    }
}
