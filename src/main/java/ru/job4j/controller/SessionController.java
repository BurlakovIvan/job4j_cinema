package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.constant.TypeFailController;
import ru.job4j.model.Movie;
import ru.job4j.model.Session;
import ru.job4j.service.MovieService;
import ru.job4j.service.SessionService;
import ru.job4j.service.TicketService;
import ru.job4j.util.UserSession;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class SessionController implements ControllerClass {
    private final SessionService sessionService;
    private final TicketService ticketService;
    private final MovieService movieService;
    private static final int TYPE = TypeFailController.SESSION;

    @Override
    @GetMapping("/formAddSession")
    public String formAdd(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("movies", movieService.findAll());
        return "addSession";
    }

    @PostMapping("/createSession")
    public String createSession(@ModelAttribute Session session) {
        boolean rsl = sessionService.add(session);
        String redirect = "redirect:/sessions";
        if (!rsl) {
            redirect = failRedirect(TYPE, "Failed to create");
        }
        return redirect;
    }

    @Override
    @GetMapping("/sessions")
    public String lists(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("sessions", sessionService.findAll());
        return "sessions";
    }

    @GetMapping("/formUpdateSession/{sessionId}")
    public String formUpdateSession(Model model,
                                    @PathVariable("sessionId") int id, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        Optional<Session> sessionOptional = sessionService.findById(id);
        String redirect = failRedirect(TYPE, "Failed to update");
        if (sessionOptional.isPresent()) {
            model.addAttribute("ses", sessionOptional.get());
            model.addAttribute("movies", movieService.findAll());
            redirect = "updateSession";
        }
        return redirect;
    }

    @PostMapping("/updateSession")
    public String updateSession(@ModelAttribute Session session) {
        boolean rsl = sessionService.update(session);
        String redirect = "redirect:/sessions";
        if (!rsl) {
            redirect = failRedirect(TYPE, "Failed to update");
        }
        return redirect;
    }

    @GetMapping("/thisSessions/{sessionId}")
    public String thisSessions(Model model, @PathVariable("sessionId") int sessionID,
                               HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        Optional<Session> sessionMove = sessionService.findById(sessionID);
        String redirect = failRedirect(TYPE, "Failed");
        if (sessionMove.isPresent()) {
            Optional<Movie> movie = movieService.findById(sessionMove.get().getMovieId());
            if (movie.isPresent()) {
                session.setAttribute("session", sessionMove.get());
                session.setAttribute("movie", movie.get());
                model.addAttribute("places", ticketService.findTicketForSessionMovie(sessionID));
                redirect = "place";
            }
        }
        return redirect;
    }

}
