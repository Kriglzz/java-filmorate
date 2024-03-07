package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dao.FilmDBStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {
    private final FilmDBStorage filmDBStorage;

    @Override
    public Film addFilm(Film film) {
        return filmDBStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновляется фильм {}.", film);
        return filmDBStorage.updateFilm(film);
    }

    @Override
    public Film getFilmById(int filmId) {
        return filmDBStorage.getFilmById(filmId);
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        return filmDBStorage.getAllFilms();
    }

    @Override
    public void giveLike(int userId, int filmId) {
        log.info("Попытка пользователя {} поставить лайк фильму {}.", userId, filmId);
        Film film = filmDBStorage.getFilmById(filmId);
        Set<Integer> likes = film.getLikes();
        likes.add(userId);
        film.setLikes(likes);
        if (userId > 0 && film.getLikes().contains(userId)) {
            log.info("Пользователь {} поставил лайк фильму {}.", userId, filmId);
            filmDBStorage.updateFilm(film);
        } else {
            log.info("Пользователь {} не смог поставил лайк фильму {}.", userId, filmId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        log.info("Попытка пользователя {} удалить лайк у фильма {}.", userId, filmId);
        Film film = filmDBStorage.getFilmById(filmId);
        if (userId > 0 && film.getLikes().contains(userId)) {
            filmDBStorage.deleteLike(userId, filmId);
            log.info("Пользователь {} удалил лайк у фильма {}.", userId, filmId);
        } else {
            log.info("Пользователь {} не смог удалить лайк у фильма {}.", userId, filmId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
    }

    @Override
    public List<Film> getMostLikedFilms(Integer count, Integer genreId, Integer year) {
        log.info("Вывод топ {} популярных фильмов.", count);

        List<Film> filteredFilmList = filmDBStorage.getAllFilms();
        List<Film> popularFilmList = filteredFilmList.stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count).collect(Collectors.toList());
        if (year != null) {
            log.info("Отсортируем по году выпуска {}", year);
            filteredFilmList = filteredFilmList.stream()
                    .filter(film -> film.getReleaseDate().getYear() == year).collect(Collectors.toList());
        }
        if (genreId != null) {
            log.info("Отсортируем по жанру с id {}", genreId);
            String genreName = filmDBStorage.getGenres().get(genreId);
            filteredFilmList = filteredFilmList.stream()
                    .filter(film -> film.getGenres().contains(new Film.GenreWrap(genreId, genreName)))
                    .collect(Collectors.toList());
        }
        if (year != null || genreId != null) {
            popularFilmList.retainAll(filteredFilmList);
        }
        return popularFilmList.stream()
                .limit(count).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getMpa() {
        log.info("Вывод всех рейтингов Ассоциации кинокомпаний и их id");
        List<Map<String, Object>> result = new ArrayList<>();
        Map<Integer, String> mpaMap = filmDBStorage.getMpa();
        for (Map.Entry<Integer, String> entry : mpaMap.entrySet()) {
            Map<String, Object> mpaInfo = new HashMap<>();
            mpaInfo.put("id", entry.getKey());
            mpaInfo.put("name", entry.getValue());
            result.add(mpaInfo);
        }
        return result;
    }

    @Override
    public Map<String, Object> getMpaById(int mpaId) {
        log.info("Вывод рейтинга Ассоциации кинокомпаний с id {}", mpaId);
        Map<Integer, String> mpaMap = filmDBStorage.getMpaById(mpaId);
        if (!mpaMap.isEmpty()) {
            Map<String, Object> mpaInfo = new HashMap<>();
            for (Map.Entry<Integer, String> entry : mpaMap.entrySet()) {
                mpaInfo.put("id", entry.getKey());
                mpaInfo.put("name", entry.getValue());
            }
            return mpaInfo;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Рейтинг Ассоциации не найден.");
        }
    }

    @Override
    public List<Map<String, Object>> getGenres() {
        log.info("Вывод всех жанров и их id");
        List<Map<String, Object>> result = new ArrayList<>();
        Map<Integer, String> genreMap = filmDBStorage.getGenres();
        for (Map.Entry<Integer, String> entry : genreMap.entrySet()) {
            Map<String, Object> genreInfo = new HashMap<>();
            genreInfo.put("id", entry.getKey());
            genreInfo.put("name", entry.getValue());
            result.add(genreInfo);
        }
        return result;
    }

    @Override
    public Map<String, Object> getGenreById(int genreId) {
        log.info("Вывод конкретного жанра с id {}", genreId);
        Map<Integer, String> genreMap = filmDBStorage.getGenreById(genreId);
        if (!genreMap.isEmpty()) {
            Map<String, Object> genreInfo = new HashMap<>();
            for (Map.Entry<Integer, String> entry : genreMap.entrySet()) {
                genreInfo.put("id", entry.getKey());
                genreInfo.put("name", entry.getValue());
            }
            return genreInfo;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Жанр не найден.");
        }
    }

    /**
     * Получить список общих фильмов
     */
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        log.info("Вывод общих фильмов для юзера с id {} и друга с id {}.", userId, friendId);
        return filmDBStorage.getCommonFilms(userId, friendId);
    }

    @Override
    public void deleteFilm(int filmId) {
        log.info("Удаление фильма с id {}", filmId);
        filmDBStorage.deleteFilm(filmId);
    }

}
