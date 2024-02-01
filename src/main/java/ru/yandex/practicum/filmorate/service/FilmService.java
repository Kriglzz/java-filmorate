package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dataBase.InMemoryFilmDataBase;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {
    private final InMemoryFilmDataBase inMemoryFilmDataBase;

    public Film addFilm(Film film) {
        log.info("Фильм {} добавлен", film);
        return inMemoryFilmDataBase.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Фильм {} обновлен", film);
        return inMemoryFilmDataBase.updateFilm(film);
    }

    public ArrayList<Film> getAllFilms() {
        return inMemoryFilmDataBase.getAllFilms();
    }
}
