package ru.job4j.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Модель данных билет
 * @author Burlakov
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @EqualsAndHashCode.Include
    private int id;
    private int sessionId;
    private int posRow;
    private int cell;
    private int userId;
}
