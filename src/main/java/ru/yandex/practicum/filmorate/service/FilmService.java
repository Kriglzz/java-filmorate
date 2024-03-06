package ru.yandex.practicum.filmorate.service;

import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FilmService {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(int filmId);

    ArrayList<Film> getAllFilms();

    void giveLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);

    List<Film> getMostLikedFilms(Integer count);

    List<Map<String, Object>> getMpa();

    Map<String, Object> getMpaById(int mpaId);

    List<Map<String, Object>> getGenres();

    Map<String, Object> getGenreById(int genreId);

    void deleteFilm(int filmId);
}
