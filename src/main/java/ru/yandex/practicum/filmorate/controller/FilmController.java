package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDbService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmDbService filmService;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        return filmService.getFilmById(filmId);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void giveLikeToFilm(@PathVariable int filmId,
                               @PathVariable int userId) {
        filmService.giveLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable int filmId,
                           @PathVariable int userId) {
        filmService.deleteLike(userId, filmId);
    }

    /**
     * Получить список популярных фильмов
     */
    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(required = false) Integer year) {
        return filmService.getMostLikedFilms(count, genreId, year);
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
        return filmService.getFilmsByQuery(query, by);
    }

    /**
     * Получить список общих фильмов
     */
    @GetMapping("/common")
    public List<Film> getCommonFilms(
            @RequestParam(value = "userId") Integer userId,
            @RequestParam(value = "friendId") Integer friendId
    ) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable Integer filmId) {
        filmService.deleteFilm(filmId);
    }

    /**
     *
     * Поиск фильмов по режиссеру
     * @param directorId id режиссера
     * @param sortBy может принимать значения:
     *               likes (сортировка по количеству лайков),
     *               year (сортировка по году выхода).
     *
     */
    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorFilmsSortedBy(
            @PathVariable Integer directorId,
            @RequestParam(value = "sortBy") String sortBy
    ) {
        return filmService.getDirectorFilmsSortedBy(directorId, sortBy);
    }
}
