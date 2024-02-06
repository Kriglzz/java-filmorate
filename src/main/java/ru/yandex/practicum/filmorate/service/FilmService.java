package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

public interface FilmService {
    public Film addFilm(Film film);

    public Film updateFilm(Film film);

    public Film getFilmById(int filmId);

    public ArrayList<Film> getAllFilms();

    public void giveLike(int userId, int filmId);

    public void deleteLike(int userId, int filmId);

    public List<Film> getMostLikedFilms(Integer count);

}
