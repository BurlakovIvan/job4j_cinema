package ru.job4j.cinema.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.List;

@Service
@ThreadSafe
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    public boolean add(int ticketID) {
        return ticketRepository.add(ticketID);
    }

    public List<Ticket> findTicketForSessionMovie(int sessionId) {
        return ticketRepository.findTicketForSessionMovie(sessionId);
    }

    public boolean addTicketUser(Ticket ticket) {
        return ticketRepository.addTicketUser(ticket);
    }

}
