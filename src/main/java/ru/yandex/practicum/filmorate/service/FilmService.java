package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmService {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(int filmId);

    List<Film> getAllFilms();

    void giveLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);

    List<Film> getMostLikedFilms(Integer count, Integer genreId, Integer year);

    List<Film> getFilmsByQuery(String query, String by);

    List<Map<String, Object>> getMpa();

    Map<String, Object> getMpaById(int mpaId);

    List<Map<String, Object>> getGenres();

    Map<String, Object> getGenreById(int genreId);

    List<Film> getCommonFilms(Integer userId, Integer friendId);

    void deleteFilm(int filmId);

    List<Film> getDirectorFilmsSortedBy(int directorId, String sortBy);
}
