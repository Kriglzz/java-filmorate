package ru.yandex.practicum.filmorate.service;

import static ru.yandex.practicum.filmorate.model.Film.DirectorWrap;
import java.util.List;

public interface DirectorService {
    DirectorWrap addDirector(DirectorWrap director);
    
    DirectorWrap getDirectorById(int directorId);
    
    DirectorWrap updateDirector(DirectorWrap director);
    
    void deleteDirector(int directorId);
    
    List<DirectorWrap> getAllDirectors();
}
