package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.AfterDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private Set<Integer> likes = new HashSet<>();
    @JsonProperty("mpa")
    private MpaWrap mpa;
    @JsonProperty("genres")
    private Set<GenreWrap> genres = new HashSet<>();

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    @Data
    public static class MpaWrap {
        private int id;
        private String name;
    }

    @Data
    public static class GenreWrap {
        private int id;
        private String name;
    }

}
