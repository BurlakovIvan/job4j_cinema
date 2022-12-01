package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;

import java.util.List;

/**
 * Интерфейс репозитория - билет
 * @author Burlakov
 */
public interface TicketRepository {

    /**
     * добавление билетов на сеанс
     * @param sessionID идентификатор билета
     * @return истина если успешно, иначе ложь
     */
    boolean add(int sessionID);

    /**
     * список билетов на сеанс
     * @param sessionId идентификатор сеанса
     * @return List Ticket
     */
    List<Ticket> findTicketForSessionMovie(int sessionId);

    /**
     * билет купили
     * @param ticket билет
     * @return истина если успешно, иначе ложь
     */
    boolean addTicketUser(Ticket ticket);
}
