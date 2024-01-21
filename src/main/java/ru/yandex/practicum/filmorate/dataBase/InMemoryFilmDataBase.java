package ru.yandex.practicum.filmorate.dataBase;

import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;
@RestController
public class InMemoryFilmDataBase implements FilmDataBase {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        int id = film.getId();
        Film savedFilm = films.get(id);
        if (savedFilm == null){
            return null;
        }
        films.put(id, film);
        return film;
    }

    @Override
    public Map<Integer, Film> getAllFilms() {
        return films;
    }
}
