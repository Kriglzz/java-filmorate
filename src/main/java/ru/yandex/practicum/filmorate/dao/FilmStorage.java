package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(int filmId);

    void deleteFilm(int filmId);

    Map<Integer, String> getMpa();

    Map<Integer, String> getMpaById(int genreId);

    Map<Integer, String> getGenres();

    Map<Integer, String> getGenreById(int genreId);

    Set<Integer> giveLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);

    List<Film> getUnCommonFilms(Integer userId, Integer friendId);

    List<Film> getFilmsByQuery(String query, String by);

    List<Film> getCommonFilms(Integer userId, Integer friendId);
}
