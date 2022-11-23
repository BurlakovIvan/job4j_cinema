package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;

import java.util.List;

public interface TicketRepository {

    boolean add(int ticketID);

    List<Ticket> findTicketForSessionMovie(int sessionId);

    boolean addTicketUser(Ticket ticket);
}
