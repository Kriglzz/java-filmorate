package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public InMemoryFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        KeyHolder key = new GeneratedKeyHolder();
        String sql = "INSERT INTO film(name, description, release_date, duration, MPA_id) " +
                "VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(film.getReleaseDate().atStartOfDay()));
            preparedStatement.setInt(4, film.getDuration());
            preparedStatement.setInt(5, (Integer)film.getMpa().get("id")); // А ВОТ ТУТАЧКИ ПРОБЛЕМА
            return preparedStatement;
        }, key);
        int id = key.getKey().intValue();

        int mpaIdFromDB = jdbcTemplate
                .queryForObject("SELECT MPA_id FROM film WHERE film_id = ?", Integer.class, id);
        Map <String, Object> mapToInsert = new HashMap<>();
        mapToInsert.put("id", mpaIdFromDB);

        String mpaNameFromDB = jdbcTemplate
                .queryForObject("SELECT MPA_name FROM motion_picture_association WHERE MPA_id = ?",
                        String.class, mpaIdFromDB);
        mapToInsert.put("name", mpaNameFromDB);

        film.setMpa(mapToInsert);
        film.setId(id);
        if (film.getGenreIds() != null) {
            String insertGenresSql = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";
            for (int genreId : film.getGenreIds()) {
                jdbcTemplate.update(insertGenresSql, film.getId(), genreId);
            }

            List<Integer> genreIds = jdbcTemplate.queryForList("SELECT genre_id FROM film_genres WHERE film_id = ?",
                    Integer.class, film.getId());
            film.setGenreIds(new HashSet<>(genreIds));
            Map<String, Object> genresMap = new HashMap<>();
            ArrayList<Map<String, Object>> genresArray = new ArrayList<>();
            for (Integer genreId : genreIds){
                String genreName = jdbcTemplate
                        .queryForObject("SELECT genre_name FROM genres WHERE genre_id = ?",
                                String.class, genreId);
                genresMap.put("id", genreId);
                genresMap.put("name", genreName);
                genresArray.add(genresMap);
            }
            film.setGenres(genresArray);
        }
        log.info("Фильм {} добавлен", film);
        Optional<Film> addedFilm = Optional.of(film);
        return addedFilm.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void checkFilm(int filmId) {
        if (!(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM film WHERE film_id = ?",
                Integer.class, filmId) > 0)) {
            log.info("Фильм c id {} не найден", filmId);
            throw new IllegalArgumentException("Не верный id");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        checkFilm(film.getId());
        String sql = "UPDATE film SET name=?, description=?, release_date=?, duration=?, MPA_id=? WHERE film_id=?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().get("id"),
                film.getId());
        if (film.getGenreIds() != null) {
            String deleteGenresSql = "DELETE FROM film_genre WHERE film_id=?";
            jdbcTemplate.update(deleteGenresSql, film.getId());

            String insertGenres = "INSERT INTO film_genre(film_id, genre_id) VALUES (?, ?)";
            Set<Integer> setToInsert = new HashSet<>();
            setToInsert.addAll(film.getGenreIds());
            jdbcTemplate.update(insertGenres, film.getId(), setToInsert);

            List<Integer> genreIds = jdbcTemplate.queryForList("SELECT genre_id FROM film_genres WHERE film_id = ?",
                    Integer.class, film.getId());
            film.setGenreIds(new HashSet<>(genreIds));
            Map<String, Object> genresMap = new HashMap<>();
            ArrayList<Map<String, Object>> genresArray = new ArrayList<>();
            for (Integer genreId : genreIds){
                String genreName = jdbcTemplate
                        .queryForObject("SELECT genre_name FROM genres WHERE genre_id = ?",
                                String.class, genreId);
                genresMap.put("id", genreId);
                genresMap.put("name", genreName);
                genresArray.add(genresMap);
            }
            film.setGenres(genresArray);

        }

        Optional<Film> updatedFilm = Optional.of(film);
        return updatedFilm.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        ArrayList<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM film");
        while (filmRows.next()) {
            Film film = new Film(filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"));
            film.setId(filmRows.getInt("film_id"));

            Map <String, Object> mapToInsert = new HashMap<>();
            mapToInsert.put("id", filmRows.getInt("MPA_id"));

            String mpaNameFromDB = jdbcTemplate
                    .queryForObject("SELECT MPA_name FROM motion_picture_association WHERE MPA_id = ?",
                            String.class, filmRows.getInt("MPA_id"));
            mapToInsert.put("name", mpaNameFromDB);

            film.setMpa(mapToInsert);
            films.add(film);
        }
        return films;
    }

    @Override
    public Film getFilmById(int filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE film_id = ?", filmId);

        while (filmRows.next()) {
            Film film = new Film(
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration")
            );
            film.setId(filmRows.getInt("film_id"));
            Map <String, Object> mapToInsert = new HashMap<>();
            mapToInsert.put("id", filmRows.getInt("MPA_id"));

            String mpaNameFromDB = jdbcTemplate
                    .queryForObject("SELECT MPA_name FROM motion_picture_association WHERE MPA_id = ?",
                            String.class, filmRows.getInt("MPA_id"));
            mapToInsert.put("name", mpaNameFromDB);

            film.setMpa(mapToInsert);

            List<Integer> genreIds = jdbcTemplate.queryForList("SELECT genre_id FROM film_genres WHERE film_id = ?",
                    Integer.class, film.getId());
            film.setGenreIds(new HashSet<>(genreIds));
            Map<String, Object> genresMap = new HashMap<>();
            ArrayList<Map<String, Object>> genresArray = new ArrayList<>();
            for (Integer genreId : genreIds){
                String genreName = jdbcTemplate
                        .queryForObject("SELECT genre_name FROM genres WHERE genre_id = ?",
                                String.class, genreId);
                genresMap.put("id", genreId);
                genresMap.put("name", genreName);
                genresArray.add(genresMap);
            }
            film.setGenres(genresArray);

            Optional<Film> foundFilm = Optional.of(film);
            return foundFilm.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден"));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
    }

    @Override
    public void deleteFilm(int filmId) {
        checkFilm(filmId);
        String sql = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public Map<Integer, String> getMpa() {
        Map<Integer, String> mpaMap = new HashMap<>();
        SqlRowSet mpaFromRow = jdbcTemplate.queryForRowSet("SELECT * FROM motion_picture_association");
        while (mpaFromRow.next()) {
            mpaMap.put(mpaFromRow.getInt("MPA_id"), mpaFromRow.getString("MPA_name"));
        }
        return mpaMap;
    }

    @Override
    public Map<Integer, String> getMpaById(int mpaId) {
        Map<Integer, String> mpaMap = new HashMap<>();
        SqlRowSet mpaFromRow = jdbcTemplate
                .queryForRowSet("SELECT * FROM motion_picture_association WHERE mpa_id = ?", mpaId);
        while (mpaFromRow.next()) {
            mpaMap.put(mpaFromRow.getInt("MPA_id"), mpaFromRow.getString("MPA_name"));
        }
        Optional<Map<Integer, String>> foundMpa = Optional.of(mpaMap);
        return foundMpa.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Рейтинг не найден"));
    }

    @Override
    public Map<Integer, String> getGenres() {
        Map<Integer, String> genreMap = new HashMap<>();
        SqlRowSet genreFromRow = jdbcTemplate.queryForRowSet("SELECT * FROM genres");
        while (genreFromRow.next()) {
            genreMap.put(genreFromRow.getInt("genre_id"), genreFromRow.getString("genre_name"));
        }
        return genreMap;
    }

    @Override
    public Map<Integer, String> getGenreById(int genreId) {
        Map<Integer, String> genreMap = new HashMap<>();
        SqlRowSet genreFromRow = jdbcTemplate
                .queryForRowSet("SELECT * FROM genres WHERE genre_id = ?", genreId);
        while (genreFromRow.next()) {
            genreMap.put(genreFromRow.getInt("genre_id"), genreFromRow.getString("genre_name"));
        }
        Optional<Map<Integer, String>> foundGenre = Optional.of(genreMap);
        return foundGenre.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Жанр не найден"));
    }
}
