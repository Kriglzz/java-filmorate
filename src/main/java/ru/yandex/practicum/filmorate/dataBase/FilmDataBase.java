package ru.yandex.practicum.filmorate.dataBase;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;


public interface FilmDataBase {

    public Film addFilm(Film film);

    public Film updateFilm(Film film);

    public ArrayList<Film> getAllFilms();
}
