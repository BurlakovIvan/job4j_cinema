package ru.job4j.cinema.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.service.ServiceMovie;
import ru.job4j.cinema.service.ServiceSession;
import ru.job4j.cinema.util.UserSession;

import javax.servlet.http.HttpSession;

@Controller
@ThreadSafe
@AllArgsConstructor
public class IndexControl {

    private final ServiceMovie movieService;
    private final ServiceSession sessionService;

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("movies", movieService.findAllWithCountryName());
        model.addAttribute("sessions", sessionService.findAll());
        return "index";
    }
}
