package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;


public interface FilmStorage {

    public Film addFilm(Film film);

    public Film updateFilm(Film film);

    public ArrayList<Film> getAllFilms();

    public Film getFilmById(int filmId);
}
