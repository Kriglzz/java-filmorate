package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.InMemoryFilmService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final InMemoryFilmService inMemoryFilmService;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmService.updateFilm(film);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        return inMemoryFilmService.getFilmById(filmId);
    }

    @GetMapping
    public ArrayList<Film> getAllFilms() {
        return inMemoryFilmService.getAllFilms();
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void giveLikeToFilm(@PathVariable int filmId,
                               @PathVariable int userId) {
        inMemoryFilmService.giveLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable int filmId,
                           @PathVariable int userId) {
        inMemoryFilmService.deleteLike(userId, filmId);
    }

    /**
     * Получить список популярных фильмов
     */
    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(required = false) Integer year) {
        return inMemoryFilmService.getMostLikedFilms(count, genreId, year);
    }

    /**
     * Поиск по названию фильмов и по режиссёру.
     * Возвращает список фильмов, отсортированных по популярности
     * @param query — текст для поиска
     * @param by — может принимать значения director (поиск по режиссёру),
     *           title (поиск по названию), либо оба значения через запятую
     *           при поиске одновременно и по режиссеру и по названию.
     */
    @GetMapping("/search")
    public List<Film> getFilmsByQuery(@RequestParam String query,
                                      @RequestParam String by) {
        return inMemoryFilmService.getFilmsByQuery(query, by);
    }

    /**
     * Получить список общих фильмов
     */
    @GetMapping("/common")
    public List<Film> getCommonFilms(
            @RequestParam(value = "userId") Integer userId,
            @RequestParam(value = "friendId") Integer friendId
    ) {
        return inMemoryFilmService.getCommonFilms(userId, friendId);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable int filmId) {
        inMemoryFilmService.deleteFilm(filmId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorFilmsSortedBy(
            @PathVariable int directorId,
            @RequestParam(value = "sortBy") String sortBy
    ) {
        return inMemoryFilmService.getDirectorFilmsSortedBy(directorId, sortBy);
    }
}
