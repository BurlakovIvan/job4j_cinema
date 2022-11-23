package ru.job4j.cinema.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.util.UserSession;
import ru.job4j.cinema.constant.TypeFailController;
import ru.job4j.cinema.model.Session;

import javax.servlet.http.HttpSession;

@Controller
@ThreadSafe
@AllArgsConstructor
public class TicketController implements ControllerClass {
    private final TicketService ticketService;
    private final SessionService sessionService;
    private static final int TYPE = TypeFailController.TICKET;

    @Override
    @GetMapping("/formAddTicket")
    public String formAdd(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("sessions", sessionService.findAll());
        return "addTicket";
    }

    @PostMapping("/createTicket")
    public String createTicket(@ModelAttribute Ticket ticket) {
        boolean result = ticketService.add(ticket.getSessionId());
        String redirect = "redirect:/index";
        if (!result) {
            redirect = failRedirect(TYPE, "Failed to update");
        }
        return redirect;
    }

    @Override
    @GetMapping("/tickets")
    public String lists(Model model, HttpSession session) {
        /*
        С заделом на будущее, чтобы можно было выводить для админ все билеты,
        которые есть сейчас, для просмотра и редактирования
        в текущей версии не требуется
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("tickets", ticketService.findAll());
        return "tickets";
         */
        return "index";
    }

    @PostMapping("/thisPlace/{posRow}/{cell}")
    public String thisPlace(Model model, @PathVariable("posRow") int posRow,
                            @PathVariable("cell") int cell, HttpSession session) {
        var user = UserSession.user(session);
        if ("Гость".equals(user.getUserName())) {
            return "login";
        }
        model.addAttribute("user", UserSession.user(session));
        session.setAttribute("posRow", posRow);
        session.setAttribute("cell", cell);
        model.addAttribute("movie", session.getAttribute("movie"));
        model.addAttribute("ses", session.getAttribute("session"));
        model.addAttribute("posRow", posRow + 1);
        model.addAttribute("cell", cell + 1);
        return "buy";
    }

    @GetMapping("/buyTicket")
    public String buyTicket(Model model, HttpSession session) {
        Session sessionMovie = (Session) session.getAttribute("session");
        User userMovie = (User) session.getAttribute("user");
        Ticket ticket = new Ticket(0, sessionMovie.getId(),
                (Integer) session.getAttribute("posRow"),
                (Integer) session.getAttribute("cell"),
                userMovie.getId());
        var rsl = ticketService.addTicketUser(ticket);
        String redirect = failRedirect(TYPE, "ERROR buy");
        if (rsl) {
            model.addAttribute("message", "Вы купили билет!");
            redirect = "success";
        }
        return redirect;
    }

    @GetMapping("/failRedirectTicket")
    public String failRedirectTicket(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        var sessionId = (Session) session.getAttribute("session");
        model.addAttribute("places",
                ticketService.findTicketForSessionMovie(sessionId.getId()));
        return "place";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "redirect:/index";
    }

    @PostMapping("/success")
    public String success() {
        return "redirect:/index";
    }

}
