package ru.job4j.cinema.service.classes;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketStore;
import ru.job4j.cinema.service.ServiceTicket;

import java.util.List;

@Service
@ThreadSafe
@AllArgsConstructor
public class TicketService implements ServiceTicket {
    private final TicketStore ticketRepository;

    @Override
    public boolean add(int ticketID) {
        return ticketRepository.add(ticketID);
    }

    @Override
    public List<Ticket> findTicketForSessionMovie(int sessionId) {
        return ticketRepository.findTicketForSessionMovie(sessionId);
    }

    @Override
    public boolean addTicketUser(Ticket ticket) {
        return ticketRepository.addTicketUser(ticket);
    }

}
