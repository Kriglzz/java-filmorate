package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorStorage;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DbDirectorService implements DirectorService {
    private final DirectorStorage storage;
    
    @Override
    public Film.DirectorWrap addDirector(Film.DirectorWrap director) {
        return null;
    }
    
    @Override
    public Film.DirectorWrap getDirectorById(int directorId) {
        return null;
    }
    
    @Override
    public Film.DirectorWrap updateDirector(Film.DirectorWrap director) {
        return null;
    }
    
    @Override
    public void deleteDirector(int directorId) {
    
    }
    
    @Override
    public List<Film.DirectorWrap> getAllDirectors() {
        return null;
    }
}
