package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final FilmService filmService;

    @GetMapping
    public List<Map<String, Object>> getGenres() {
        return filmService.getGenres();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getGenreById(@PathVariable Integer id) {
        return filmService.getGenreById(id);
    }
}
