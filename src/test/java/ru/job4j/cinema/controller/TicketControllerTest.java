package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import ru.job4j.cinema.constant.TypeFailController;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.service.SimpleSessionService;
import ru.job4j.cinema.service.SimpleTicketService;
import ru.job4j.cinema.util.UserSession;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketControllerTest {

    @Test
    public void whenAddTicket() {
        Session session1 = new Session(1, 1, "Session1");
        Session session2 = new Session(2, 1, "Session2");
        Map<Session, String> sessions = Map.of(session1, "Movie", session2, "Movie");
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        SessionService sessionService = mock(SimpleSessionService.class);
        TicketService ticketService = mock(SimpleTicketService.class);
        TicketController ticketController
                = new TicketController(ticketService, sessionService);
        when(sessionService.findAll()).thenReturn(sessions);
        String page = ticketController.formAdd(model, session);
        verify(sessionService).findAll();
        assertThat(page).isEqualTo("addTicket");
    }

    @Test
    public void whenCreateTicketSuccess() {
        Ticket ticket = new Ticket(1, 1, 1, 1, 1);
        SessionService sessionService = mock(SimpleSessionService.class);
        TicketService ticketService = mock(SimpleTicketService.class);
        TicketController ticketController
                = new TicketController(ticketService, sessionService);
        when(ticketService.add(ticket.getSessionId())).thenReturn(true);
        String page = ticketController.createTicket(ticket);
        verify(ticketService).add(ticket.getSessionId());
        assertThat(page).isEqualTo("redirect:/index");
    }

    @Test
    public void whenCreateTicketFail() {
        Ticket ticket = new Ticket(1, 1, 1, 1, 1);
        SessionService sessionService = mock(SimpleSessionService.class);
        TicketService ticketService = mock(SimpleTicketService.class);
        TicketController ticketController
                = new TicketController(ticketService, sessionService);
        when(ticketService.add(ticket.getSessionId())).thenReturn(false);
        String page = ticketController.createTicket(ticket);
        verify(ticketService).add(ticket.getSessionId());
        int type = TypeFailController.TICKET;
        assertThat(page)
                .isEqualTo(String
                        .format("redirect:/fail?type=%s&message=Failed to update", type));
    }

    @Test
    public void whenFindAllTickets() {
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        SessionService sessionService = mock(SimpleSessionService.class);
        TicketService ticketService = mock(SimpleTicketService.class);
        TicketController ticketController
                = new TicketController(ticketService, sessionService);
        String page = ticketController.lists(model, session);
        assertThat(page).isEqualTo("index");
    }

    @Test
    public void whenPlaceTicketNoAccess() {
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        SessionService sessionService = mock(SimpleSessionService.class);
        TicketService ticketService = mock(SimpleTicketService.class);
        TicketController ticketController
                = new TicketController(ticketService, sessionService);
        String page = ticketController.thisPlace(model, 1, 1, session);
        assertThat(page).isEqualTo("login");
    }

    @Test
    public void whenPlaceTicketSuccess() {
        User user = new User(1, "Name", "Password", "Email");
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        SessionService sessionService = mock(SimpleSessionService.class);
        TicketService ticketService = mock(SimpleTicketService.class);
        TicketController ticketController
                = new TicketController(ticketService, sessionService);
        when(UserSession.user(session)).thenReturn(user);
        String page = ticketController.thisPlace(model, 1, 1, session);
        assertThat(page).isEqualTo("buy");
    }

    @Test
    public void whenBuyTicketSuccess() {
        Session sessionMovie = new Session(1, 1, "Session");
        User user = new User(1, "Name", "Password", "Email");
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        SessionService sessionService = mock(SimpleSessionService.class);
        TicketService ticketService = mock(SimpleTicketService.class);
        TicketController ticketController
                = new TicketController(ticketService, sessionService);
        when(ticketService.addTicketUser(Mockito.any())).thenReturn(true);
        when(session.getAttribute("session")).thenReturn(sessionMovie);
        when(session.getAttribute("user")).thenReturn(user);
        when(session.getAttribute("posRow")).thenReturn(1);
        when(session.getAttribute("cell")).thenReturn(1);
        String page = ticketController.buyTicket(model, session);
        assertThat(page).isEqualTo("success");
    }

    @Test
    public void whenBuyTicketFail() {
        Session sessionMovie = new Session(1, 1, "Session");
        User user = new User(1, "Name", "Password", "Email");
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        SessionService sessionService = mock(SimpleSessionService.class);
        TicketService ticketService = mock(SimpleTicketService.class);
        TicketController ticketController
                = new TicketController(ticketService, sessionService);
        when(ticketService.addTicketUser(Mockito.any())).thenReturn(false);
        when(session.getAttribute("session")).thenReturn(sessionMovie);
        when(session.getAttribute("user")).thenReturn(user);
        when(session.getAttribute("posRow")).thenReturn(1);
        when(session.getAttribute("cell")).thenReturn(1);
        String page = ticketController.buyTicket(model, session);
        int type = TypeFailController.TICKET;
        assertThat(page)
                .isEqualTo(String
                        .format("redirect:/fail?type=%s&message=ERROR buy", type));
    }

    @Test
    public void whenFailRedirectTicket() {
        Ticket ticket1 = new Ticket(1, 1, 1, 1, 1);
        Ticket ticket2 = new Ticket(2, 1, 1, 1, 2);
        List<Ticket> tickets = List.of(ticket1, ticket2);
        Session sessionMovie = new Session(1, 1, "Session");
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        SessionService sessionService = mock(SimpleSessionService.class);
        TicketService ticketService = mock(SimpleTicketService.class);
        TicketController ticketController
                = new TicketController(ticketService, sessionService);
        when(session.getAttribute("session")).thenReturn(sessionMovie);
        when(ticketService.findTicketForSessionMovie(sessionMovie.getId())).thenReturn(tickets);
        String page = ticketController.failRedirectTicket(model, session);
        assertThat(page).isEqualTo("place");
    }

    @Test
    public void whenCancel() {
        SessionService sessionService = mock(SimpleSessionService.class);
        TicketService ticketService = mock(SimpleTicketService.class);
        TicketController ticketController
                = new TicketController(ticketService, sessionService);
        String page = ticketController.cancel();
        assertThat(page).isEqualTo("redirect:/index");
    }

    @Test
    public void whenSuccess() {
        SessionService sessionService = mock(SimpleSessionService.class);
        TicketService ticketService = mock(SimpleTicketService.class);
        TicketController ticketController
                = new TicketController(ticketService, sessionService);
        String page = ticketController.success();
        assertThat(page).isEqualTo("redirect:/index");
    }
}