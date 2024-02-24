package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Map;


public interface FilmStorage {

    public Film addFilm(Film film);

    public Film updateFilm(Film film);

    public ArrayList<Film> getAllFilms();

    public Film getFilmById(int filmId);

    public void deleteFilm(int filmId);

    public Map<Integer, String> getMpa();

    public Map<Integer, String> getMpaById(int genreId);

    public Map<Integer, String> getGenres();

    public Map<Integer, String> getGenreById(int genreId);
}
