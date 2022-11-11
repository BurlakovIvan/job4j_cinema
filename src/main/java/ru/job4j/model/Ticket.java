package ru.job4j.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
