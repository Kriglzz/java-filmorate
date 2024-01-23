package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.validator.AfterDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Film.
 */
@lombok.Data
public class Film {
    private static int filmCount = 0;
    @lombok.NonNull
    @NotBlank
    private final String name;
    @lombok.NonNull
    @Size(max = 200, message = "Длина описания не должна превышать 200 символов")
    private final String description;
    @lombok.NonNull
    @AfterDate(date = "1895-12-28")
    private final LocalDate releaseDate;
    @lombok.NonNull
    private int id;
    @lombok.NonNull
    @Positive
    private int duration;

    public Film(String name, String description, LocalDate releaseDate, int duration) {

        this.id = generateId();
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    Integer generateId() {
        return ++filmCount;
    }
}
