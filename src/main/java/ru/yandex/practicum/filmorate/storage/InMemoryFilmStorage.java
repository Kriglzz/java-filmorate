package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final ArrayList<Film> films = new ArrayList<>();

    @Override
    public Film addFilm(Film film) {
        film.setId(film.generateId());
        films.add(film);
        log.info("Фильм {} добавлен", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        boolean filmFound = films.stream().anyMatch(f -> Objects.equals(f.getId(), film.getId()));
        if (filmFound) {
            films.removeIf(u -> Objects.equals(u.getId(), film.getId()));
            films.add(film);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        return film;
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        return films;
    }

    @Override
    public Film getFilmById(int filmId) {
        Optional<Film> foundFilm = films.stream()
                .filter(film -> film.getId() == filmId)
                .findFirst();
        return foundFilm.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден"));
    }
}
