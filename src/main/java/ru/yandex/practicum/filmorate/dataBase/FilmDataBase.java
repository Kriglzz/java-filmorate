package ru.yandex.practicum.filmorate.dataBase;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmDataBase {

    public Film addFilm(Film film);

    public Film updateFilm(Film film);

    public Map<Integer, Film> getAllFilms();
}
