package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorStorage;
import static ru.yandex.practicum.filmorate.model.Film.DirectorWrap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorDbService implements DirectorService {
    private final DirectorStorage directorStorage;

    @Override
    public DirectorWrap addDirector(DirectorWrap director) {
        DirectorWrap added = directorStorage.addDirector(director);
        log.info("Режиссер добавлен.");
        return added;
    }

    @Override
    public DirectorWrap getDirectorById(int directorId) {
        DirectorWrap director = directorStorage.getDirectorById(directorId);
        log.info("Режиссер {} получен.", directorId);
        return director;
    }

    @Override
    public DirectorWrap updateDirector(DirectorWrap director) {
        DirectorWrap updated = directorStorage.updateDirector(director);
        log.info("Режиссер {} обновлен.", director.getId());
        return updated;
    }

    @Override
    public void deleteDirector(int directorId) {
        directorStorage.deleteDirector(directorId);
        log.info("Режиссер {} удален.", directorId);
    }

    @Override
    public List<DirectorWrap> getAllDirectors() {
        List<DirectorWrap> directors = directorStorage.getAllDirectors();
        log.info("Всё режиссеры получены.");
        return directors;
    }
}
