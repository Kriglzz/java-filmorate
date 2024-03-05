package ru.yandex.practicum.filmorate.dao;

import java.util.List;

import static ru.yandex.practicum.filmorate.model.Film.DirectorWrap;

public interface DirectorStorage {
    DirectorWrap addDirector(DirectorWrap director);
    
    DirectorWrap getDirectorById(int directorId);
    
    DirectorWrap updateDirector(DirectorWrap director);
    
    void deleteDirector(int directorId);
    
    List<DirectorWrap> getAllDirectors();
}
