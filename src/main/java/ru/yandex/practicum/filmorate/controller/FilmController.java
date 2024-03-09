package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDBService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmDBService filmDBService;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmDBService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmDBService.updateFilm(film);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        return filmDBService.getFilmById(filmId);
    }

    @GetMapping
    public ArrayList<Film> getAllFilms() {
        return filmDBService.getAllFilms();
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void giveLikeToFilm(@PathVariable int filmId,
                               @PathVariable int userId) {
        filmDBService.giveLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable int filmId,
                           @PathVariable int userId) {
        filmDBService.deleteLike(userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmDBService.getMostLikedFilms(count);
    }

    /**
     * Получить список общих фильмов
     */
    @GetMapping("/common")
    public List<Film> getCommonFilms(
            @RequestParam(value = "userId", required = true) Integer userId,
            @RequestParam(value = "friendId", required = true) Integer friendId
    ) {
        return filmDBService.getCommonFilms(userId, friendId);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable int filmId) {
        filmDBService.deleteFilm(filmId);
    }

}
