package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Objects;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final ArrayList<Film> films = new ArrayList<>();

    @Override
    public Film addFilm(Film film) {
        films.add(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        for (Film f : films) {
            if (Objects.equals(f.getId(), film.getId())) {
                films.remove(f);
                break;
            }
        }
        films.add(film);
        return film;
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        return films;
    }
}
