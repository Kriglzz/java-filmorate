package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmService.addFilm(film);
    }
    @PutMapping
    public Film updateFilm(@RequestBody Film film){
        return filmService.updateFilm(film);
    }
    @GetMapping
    public Map<Integer, Film> getAllFilms(){
        return filmService.getAllFilms();
    }
}
