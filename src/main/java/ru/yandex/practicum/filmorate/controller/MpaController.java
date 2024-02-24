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
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final FilmService filmService;

    @GetMapping
    public List<Map<String, Object>> getMpa() {
        return filmService.getMpa();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getMpaById(@PathVariable int id) {
        return filmService.getMpaById(id);
    }
}
