package ru.job4j.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    private String description;
    private LocalDate created = LocalDate.now();
    private int countryId;
    private byte[] photo;
}
