package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Map;


public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    ArrayList<Film> getAllFilms();

    Film getFilmById(int filmId);

    void deleteFilm(int filmId);

    Map<Integer, String> getMpa();

    Map<Integer, String> getMpaById(int genreId);

    Map<Integer, String> getGenres();

    Map<Integer, String> getGenreById(int genreId);
}
