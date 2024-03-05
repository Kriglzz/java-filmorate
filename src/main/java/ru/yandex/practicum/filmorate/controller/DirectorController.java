package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.model.Film.DirectorWrap;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService service;
    
    @PostMapping
    public DirectorWrap addDirector(@Valid @RequestBody DirectorWrap director) {
        return service.addDirector(director);
    }
    
    @GetMapping("/{directorId}")
    public DirectorWrap getDirectorById(@PathVariable int directorId) {
        return service.getDirectorById(directorId);
    }
    
    @PutMapping
    public DirectorWrap updateDirector(@Valid @RequestBody DirectorWrap director) {
        return service.updateDirector(director);
    }
    
    @DeleteMapping("/{directorId}")
    public void deleteDirector(@PathVariable int directorId) {
        service.deleteDirector(directorId);
    }
    
    @GetMapping
    public List<DirectorWrap> getAllDirectors() {
        return service.getAllDirectors();
    }
}
