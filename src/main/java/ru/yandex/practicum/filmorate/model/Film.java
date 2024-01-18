package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.validator.AfterDate;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * Film.
 */
@lombok.Data
public class Film {
    @lombok.NonNull
    @NotBlank
    private final String name;
    @lombok.NonNull
    @Max(200)
    private final String description;
    @lombok.NonNull
    @AfterDate(date = "1895-12-28")
    private final LocalDateTime releaseDate;
    @lombok.NonNull
    private int id;
    @lombok.NonNull
    @Positive
    private int duration;

    public Film(int id, String name, String description, LocalDateTime releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
