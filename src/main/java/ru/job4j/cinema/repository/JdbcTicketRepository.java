package ru.job4j.cinema.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.constant.NumberCinemaPlace;
import ru.job4j.cinema.model.Ticket;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс репозитория - билет
 * Потокобезопасен за счет singleton и обработки запроса на стороне SQL
 * @author Burlakov
 */
@ThreadSafe
@AllArgsConstructor
@Repository
public class JdbcTicketRepository implements TicketRepository {
    private final DataSource pool;
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTicketRepository.class.getName());

    private final static String INSERT = """
                                         INSERT INTO tickets(session_id, pos_row, cell)
                                         VALUES (?, ?, ?)
                                         """;

    private final static String SELECT_PLACE = """
                                         SELECT id, pos_row, cell, user_id
                                         FROM tickets
                                         WHERE session_id = ?
                                         ORDER BY pos_row, cell
                                         """;

    private final static String UPDATE = """
                                         UPDATE tickets
                                         SET user_id = ?
                                         WHERE session_id = ? AND pos_row = ?
                                         AND cell = ? AND user_id is null
                                         """;

    /**
     * добавление билетов на сеанс
     * @param sessionID идентификатор билета
     * @return истина если успешно, иначе ложь
     */
    @Override
    public boolean add(int sessionID) {
        int row = NumberCinemaPlace.ROW;
        int cell = NumberCinemaPlace.CELL;
        boolean rsl = false;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < cell; j++) {
                try (Connection cn = pool.getConnection();
                     PreparedStatement ps = cn.prepareStatement(INSERT)
                ) {
                    ps.setInt(1, sessionID);
                    ps.setInt(2, i);
                    ps.setInt(3, j);
                    rsl = ps.executeUpdate() > 0;
                } catch (Exception ex) {
                    LOGGER.error("ERROR: ", ex);
                }
            }
        }
        return rsl;
    }

    /**
     * список билетов на сеанс
     * @param sessionId идентификатор сеанса
     * @return List Ticket
     */
    @Override
    public List<Ticket> findTicketForSessionMovie(int sessionId) {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(SELECT_PLACE)
        ) {
            ps.setInt(1, sessionId);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    tickets.add(newTicket(resultSet, sessionId));
                }
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return tickets;
    }

    /**
     * билет купили
     * @param ticket билет
     * @return истина если успешно, иначе ложь
     */
    @Override
    public boolean addTicketUser(Ticket ticket) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            ps.setInt(1, ticket.getUserId());
            ps.setInt(2, ticket.getSessionId());
            ps.setInt(3, ticket.getPosRow());
            ps.setInt(4, ticket.getCell());
            rsl = ps.executeUpdate() > 0;
        } catch (Exception ex) {
            LOGGER.error("ERROR: ", ex);
        }
        return rsl;
    }

    /**
     * приватный метод создания нового объекта типа билет
     * @param resultSet результат запроса
     * @return новый объект типа билет
     * @throws SQLException ошибка
     */
    private Ticket newTicket(ResultSet resultSet, int sessionId) throws SQLException {
        return new Ticket(
                resultSet.getInt("id"),
                sessionId,
                resultSet.getInt("pos_row"),
                resultSet.getInt("cell"),
                resultSet.getInt("user_id"));
    }
}
