package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dao.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

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
            inMemoryFilmStorage.deleteFilm(filmId);
            log.info("Пользователь {} удалил лайк у фильма {}.", userId, filmId);
        } else {
            log.info("Пользователь {} не смог удалить лайк у фильма {}.", userId, filmId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
    }

    @Override
    public List<Film> getMostLikedFilms(Integer count) {
        log.info("Вывод топ {} популярных фильмов .", count);
        ArrayList<Film> films = inMemoryFilmStorage.getAllFilms();
        Collections.sort(films,
                Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed());
        return films.subList(0, Math.min(films.size(), count));
        //сортировка и вывод фильмов только с лайками, на случай чего
        /*log.info("Вывод топ {} популярных фильмов .", count);
        List<Film> films = inMemoryFilmStorage.getAllFilms().stream()
                .filter(film -> !film.getLikes()
                        .isEmpty()).sorted(Comparator
                        .comparingInt((Film film) -> film.getLikes().size())
                        .reversed()).collect(Collectors.toList());
        return films.subList(0, Math.min(films.size(), count));*/
    }

    @Override
    public List<Map<String, Object>> getMpa() {
        log.info("Вывод всех рейтингов Ассоциации кинокомпаний и их id");
        List<Map<String, Object>> result = new ArrayList<>();
        Map<Integer, String> mpaMap = inMemoryFilmStorage.getMpa();
        for (Map.Entry<Integer, String> entry : mpaMap.entrySet()) {
            Map<String, Object> mpaInfo = new HashMap<>();
            mpaInfo.put("id", entry.getKey());
            mpaInfo.put("name", entry.getValue());
            result.add(mpaInfo);
        }
        return result;
    }

    @Override
    public Map<String, Object> getMpaById(int mpaId) {
        log.info("Вывод рейтинга Ассоциации кинокомпаний с id {}", mpaId);
        Map<Integer, String> mpaMap = inMemoryFilmStorage.getMpa();
        Map<String, Object> mpaInfo = new HashMap<>();
        for (Map.Entry<Integer, String> entry : mpaMap.entrySet()) {
            mpaInfo.put("id", entry.getKey());
            mpaInfo.put("name", entry.getValue());
        }
        return mpaInfo;
    }

    @Override
    public List<Map<String, Object>> getGenres() {
        log.info("Вывод всех жанров и их id");
        List<Map<String, Object>> result = new ArrayList<>();
        Map<Integer, String> genreMap = inMemoryFilmStorage.getMpa();
        for (Map.Entry<Integer, String> entry : genreMap.entrySet()) {
            Map<String, Object> genreInfo = new HashMap<>();
            genreInfo.put("id", entry.getKey());
            genreInfo.put("name", entry.getValue());
            result.add(genreInfo);
        }
        return result;
    }

    @Override
    public Map<String, Object> getGenreById(int genreId) {
        log.info("Вывод конкретного жанра с id {}", genreId);
        Map<Integer, String> genreMap = inMemoryFilmStorage.getMpa();
        Map<String, Object> genreInfo = new HashMap<>();
        for (Map.Entry<Integer, String> entry : genreMap.entrySet()) {
            genreInfo.put("id", entry.getKey());
            genreInfo.put("name", entry.getValue());
        }
        return genreInfo;
    }

}
