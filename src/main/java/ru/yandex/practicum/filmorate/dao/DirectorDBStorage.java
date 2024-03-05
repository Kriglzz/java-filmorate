package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DirectorDBStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate; //TODO: 1.Создать маппер
    
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
    } //TODO:2.Реализовать методы
}
