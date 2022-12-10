package ru.job4j.cinema.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.constant.TypeFailController;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.service.MovieService;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.util.UserSession;
import ru.job4j.cinema.model.Session;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Контроллер. Сеанс
 * @author Burlakov
 */
@Controller
@AllArgsConstructor
public class SessionController implements ControllerClass {
    private final SessionService sessionService;
    private final TicketService ticketService;
    private final MovieService movieService;
    private static final int TYPE = TypeFailController.SESSION;

    /**
     * форма добавления
     * @param model Model
     * @param session HttpSession
     * @return addSession
     */
    @Override
    @GetMapping("/formAddSession")
    public String formAdd(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("movies", movieService.findAll());
        return "addSession";
    }

    /**
     * создание новой записи
     * @param session сеанс
     * @return redirect:/sessions если успешно, failRedirect в противном случае
     */
    @PostMapping("/createSession")
    public String createSession(@ModelAttribute Session session) {
        boolean rsl = sessionService.add(session);
        String redirect = "redirect:/sessions";
        if (!rsl) {
            redirect = failRedirect(TYPE, "Failed to create");
        }
        return redirect;
    }

    /**
     * вывод списка всех записей
     * @param model Model
     * @param session HttpSession
     * @return sessions
     */
    @Override
    @GetMapping("/sessions")
    public String lists(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("sessions", sessionService.findAllWithMovieName());
        return "sessions";
    }

    /**
     * форма редактирования записи
     * @param model Model
     * @param id идентификатор сеанса
     * @param session HttpSession
     * @return updateSession если успешно, failRedirect в противном случае
     */
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

    /**
     * обновление записи
     * @param session сеанс
     * @return sessions если успешно, failRedirect в противном случае
     */
    @PostMapping("/updateSession")
    public String updateSession(@ModelAttribute Session session) {
        boolean rsl = sessionService.update(session);
        String redirect = "redirect:/sessions";
        if (!rsl) {
            redirect = failRedirect(TYPE, "Failed to update");
        }
        return redirect;
    }

    /**
     * форма места в кинозале
     * @param model model
     * @param sessionID идентификатор сеанса
     * @param session HttpSession
     * @return place если успешно, failRedirect в противном случае
     */
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
