package ru.job4j.cinema.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.List;

/**
 * Класс сервис - билет
 * @author Burlakov
 */
@Service
@AllArgsConstructor
public class SimpleTicketService implements TicketService {
    private final TicketRepository ticketRepository;

    /**
     * добавление билетов на сеанс
     * @param sessionID идентификатор билета
     * @return истина если успешно, иначе ложь
     */
    @Override
    public boolean add(int sessionID) {
        return ticketRepository.add(sessionID);
    }

    /**
     * список билетов на сеанс
     * @param sessionId идентификатор сеанса
     * @return List Ticket
     */
    @Override
    public List<Ticket> findTicketForSessionMovie(int sessionId) {
        return ticketRepository.findTicketForSessionMovie(sessionId);
    }

    /**
     * билет купили
     * @param ticket билет
     * @return истина если успешно, иначе ложь
     */
    @Override
    public boolean addTicketUser(Ticket ticket) {
        return ticketRepository.addTicketUser(ticket);
    }

}
