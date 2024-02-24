package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FilmService {
    public Film addFilm(Film film);

    public Film updateFilm(Film film);

    public Film getFilmById(int filmId);

    public ArrayList<Film> getAllFilms();

    public void giveLike(int userId, int filmId);

    public void deleteLike(int userId, int filmId);

    public List<Film> getMostLikedFilms(Integer count);

    public List<Map<String, Object>> getMpa();

    public Map<String, Object> getMpaById(int mpaId);

    public List<Map<String, Object>> getGenres();

    public Map<String, Object> getGenreById(int genreId);

}
