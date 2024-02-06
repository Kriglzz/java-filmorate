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
public class InMemoryFilmService implements FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Override
    public Film addFilm(Film film) {
        log.info("Фильм {} добавлен", film);
        return inMemoryFilmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Фильм {} обновлен", film);
        return inMemoryFilmStorage.updateFilm(film);
    }

    @Override
    public Film getFilmById(int filmId) {
        return inMemoryFilmStorage.getFilmById(filmId);
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    @Override
    public void giveLike(int userId, int filmId) {
        log.info("Попытка пользователя {userId} поставить лайк фильму {filmId}.");
        Film film = inMemoryFilmStorage.getFilmById(filmId);
        if (film.getLikes()
                .stream()
                .anyMatch(id -> id == userId)) {
            log.info("Пользователь {userId} не смог поставил лайк фильму {filmId}.");
        } else {
            film.giveLike(userId);
            log.info("Пользователь {userId} поставил лайк фильму {filmId}.");
        }
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        log.info("Попытка пользователя {userId} удалить лайк у фильма {filmId}.");
        Film film = inMemoryFilmStorage.getFilmById(filmId);
        if (film.getLikes()
                .stream()
                .anyMatch(id -> id == userId)) {
            film.deleteLike(userId);
            log.info("Пользователь {userId} удалил лайк у фильма {filmId}.");
        } else {
            log.info("Пользователь {userId} не смог удалить лайк у фильма {filmId}.");
        }
    }

    @Override
    public List<Film> getMostLikedFilms() {
        ArrayList<Film> films = inMemoryFilmStorage.getAllFilms();
        Collections.sort(films,
                Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed());
        return films.subList(0, Math.min(films.size(), 10));
    }
}
