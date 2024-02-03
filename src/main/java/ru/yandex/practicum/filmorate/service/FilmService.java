package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    public void giveLike(int userId, int filmId) {
        Film film = inMemoryFilmStorage.getFilmById(filmId);
        if (film.getLikes()
                .stream()
                .anyMatch(id -> id == userId)) {
            System.out.println("Вы уже поставили Like этому фильму");
        } else {
            film.giveLike(userId);
        }
    }

    public void deleteLike(int userId, int filmId) {
        Film film = inMemoryFilmStorage.getFilmById(filmId);
        if (film.getLikes()
                .stream()
                .anyMatch(id -> id == userId)) {
            film.deleteLike(userId);
        } else {
            System.out.println("Вы еще не оставляли Like этому фильму");
        }
    }

    public List<Film> getMostLikedFilms() {
        ArrayList<Film> films = inMemoryFilmStorage.getAllFilms();
        Collections.sort(films,
                Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed());
        return films.subList(0, Math.min(films.size(), 10));
    }
}
