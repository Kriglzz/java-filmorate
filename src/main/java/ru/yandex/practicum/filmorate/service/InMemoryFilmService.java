package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Override
    public Film addFilm(Film film) {
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
        log.info("Попытка пользователя {} поставить лайк фильму {}.", userId, filmId);
        Film film = inMemoryFilmStorage.getFilmById(filmId);
        if (film.getLikes()
                .stream()
                .anyMatch(id -> id == userId)) {
            log.info("Пользователь {} не смог поставил лайк фильму {}.", userId, filmId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        } else {
            film.giveLike(userId);
            log.info("Пользователь {} поставил лайк фильму {}.", userId, filmId);
        }
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        log.info("Попытка пользователя {} удалить лайк у фильма {}.", userId, filmId);
        Film film = inMemoryFilmStorage.getFilmById(filmId);
        if (film.getLikes()
                .stream()
                .anyMatch(id -> id == userId)) {
            film.deleteLike(userId);
            log.info("Пользователь {} удалил лайк у фильма {}.", userId, filmId);
        } else {
            log.info("Пользователь {} не смог удалить лайк у фильма {}.", userId, filmId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
    }

    @Override
    public List<Film> getMostLikedFilms(Integer count) {
        //старый код, который не убирал фильмы с 0 лайков
        /*ArrayList<Film> films = inMemoryFilmStorage.getAllFilms();
        Collections.sort(films,
                Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed());
        return films.subList(0, Math.min(films.size(), 10));*/
        log.info("Вывод топ {} популярных фильмов .", count);
        List<Film> films = inMemoryFilmStorage.getAllFilms().stream()
                .filter(film -> !film.getLikes()
                        .isEmpty()).sorted(Comparator
                        .comparingInt((Film film) -> film.getLikes().size())
                        .reversed()).collect(Collectors.toList());
        return films.subList(0, Math.min(films.size(), count));
    }
}
