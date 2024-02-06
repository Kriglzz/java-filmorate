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

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(defaultValue = "10") Integer count) {
        return inMemoryFilmService.getMostLikedFilms();
    }

}
