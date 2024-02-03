package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    public Film addFilm(Film film) {
        log.info("Фильм {} добавлен", film);
        return inMemoryFilmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Фильм {} обновлен", film);
        return inMemoryFilmStorage.updateFilm(film);
    }

    public ArrayList<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }
}
