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
import java.util.stream.Collectors;

@Slf4j
@Component
public class FilmDBStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        KeyHolder key = new GeneratedKeyHolder();
        String sql = "INSERT INTO film(name, description, release_date, duration) " +
                "VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(film.getReleaseDate().atStartOfDay()));
            preparedStatement.setInt(4, film.getDuration());
            return preparedStatement;
        }, key);
        int id = key.getKey().intValue();
        film.setId(id);

        insertMpa(film);
        insertGenre(film);
        insertLikes(film);

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
        String sql = "UPDATE film SET name=?, description=?, release_date=?, duration=? WHERE film_id=?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());

        String deleteMpaSql = "DELETE FROM MPA_ids WHERE film_id=?";
        jdbcTemplate.update(deleteMpaSql, film.getId());
        insertMpa(film);

        String deleteGenresSql = "DELETE FROM film_genres WHERE film_id=?";
        jdbcTemplate.update(deleteGenresSql, film.getId());
        insertGenre(film);

        String deleteLikesSql = "DELETE FROM likes WHERE film_id=?";
        jdbcTemplate.update(deleteLikesSql, film.getId());
        insertLikes(film);
        Optional<Film> updatedFilm = Optional.of(film);
        return updatedFilm.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        ArrayList<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM film");
        while (filmRows.next()) {
            Film film = new Film(
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"));
            film.setId(filmRows.getInt("film_id"));
            films.add(film);
        }
        ArrayList<Film> filmsWithStats = new ArrayList<>();
        for (Film film : films) {
            int id = film.getId();
            film.setMpa(selectMpa().get(id));
            film.setGenres(selectGenres().get(id));
            if (film.getGenres() == null) {
                film.setGenres(new HashSet<Film.GenreWrap>());
            } // Ииии тааак сойдеееет
            film.setLikes(selectLikes(id));
            filmsWithStats.add(film);
        }

        return filmsWithStats;
    }

    @Override
    public Film getFilmById(int filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE film_id = ?", filmId);

        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"));
            film.setId(filmRows.getInt("film_id"));
            int id = film.getId();
            film.setMpa(selectMpa().get(id));
            film.setGenres(selectGenres().get(id));
            if (film.getGenres() == null) {
                film.setGenres(new HashSet<Film.GenreWrap>());
            }
            film.setLikes(selectLikes(id));

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

    @Override
    public Set<Integer> giveLike(int userId, int filmId) {

        Set<Integer> likes = new HashSet<>();
        likes.add(userId);

        Film film = getFilmById(filmId);
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }

        return likes;
    }

    public Set<Integer> deleteLike(int userId, int filmId) {
        String deleteLike = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(deleteLike, userId, filmId);

        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT user_id FROM likes WHERE film_id = ?", filmId);
        Set<Integer> likes = new HashSet<>();

        Film film = getFilmById(filmId);
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        return likes;
    }

    private void insertMpa(Film film) {
        if (film.getMpa() != null) {
            String insertMpaSql = "INSERT INTO MPA_ids(film_id, mpa_id) VALUES (?, ?)";
            jdbcTemplate.update(insertMpaSql, film.getId(), film.getMpa().getId());
        }
    }

    private void insertGenre(Film film) {
        for (Film.GenreWrap genre : film.getGenres()) {
            String insertGenresSql = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(insertGenresSql, film.getId(), genre.getId());
        }
        if (selectGenres().get(film.getId()) != null) {
            film.setGenres(selectGenres().get(film.getId()));
        }
    }

    private void insertLikes(Film film) {
        String insertLikesSql = "INSERT INTO likes(film_id, user_id) VALUES (?, ?)";
        if (!film.getLikes().isEmpty()) {
            for (Integer userWhoLikeId : film.getLikes()) {
                if (userWhoLikeId > 0) {
                    jdbcTemplate.update(insertLikesSql, film.getId(), userWhoLikeId);
                }
            }
        } else {
            jdbcTemplate.update(insertLikesSql, film.getId(), null);
        }
    }

    private HashMap<Integer, Film.MpaWrap> selectMpa() {

        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(
                "SELECT MPA_ids.film_id, motion_picture_association.mpa_id, motion_picture_association.mpa_name " +
                        "FROM MPA_ids " +
                        "LEFT OUTER JOIN motion_picture_association " +
                        "ON MPA_ids.mpa_id = motion_picture_association.mpa_id");

        HashMap<Integer, Film.MpaWrap> filmsMpa = new HashMap<>();
        while (mpaRows.next()) {
            Film.MpaWrap mpaWrap = new Film.MpaWrap();
            int id = mpaRows.getInt("film_id");
            mpaWrap.setId(mpaRows.getInt("mpa_id"));
            mpaWrap.setName(mpaRows.getString("mpa_name"));
            filmsMpa.put(id, mpaWrap);
        }
        return filmsMpa;
    }

    private HashMap<Integer, Set<Film.GenreWrap>> selectGenres() {

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(
                "SELECT * FROM film_genres" +
                        " LEFT OUTER JOIN genres" +
                        " ON film_genres.genre_id = genres.genre_id");

        HashMap<Integer, Set<Film.GenreWrap>> filmsGenres = new HashMap<>();
        while (genreRows.next()) {
            int filmId = genreRows.getInt("film_id");
            Film.GenreWrap genreWrap = new Film.GenreWrap();
            genreWrap.setId(genreRows.getInt("genre_id"));
            genreWrap.setName(genreRows.getString("genre_name"));
            if (!filmsGenres.containsKey(filmId)) {
                filmsGenres.put(filmId, new TreeSet<>(Comparator.comparingInt(Film.GenreWrap::getId)));
            }
            filmsGenres.get(filmId).add(genreWrap);
        }
        return filmsGenres;
    }

    private HashSet<Integer> selectLikes(int filmId) {
        SqlRowSet filmLikesRows = jdbcTemplate.queryForRowSet(
                "SELECT user_id FROM likes WHERE film_id = ?", filmId);

        HashSet<Integer> filmLikes = new HashSet<>();

        while (filmLikesRows.next()) {
            int userId = filmLikesRows.getInt("user_id");
            filmLikes.add(userId);
        }
        return filmLikes;
    }

    /**
     * Получить список общих фильмов
     */
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        SqlRowSet commonFilmsRows = jdbcTemplate.queryForRowSet(
                "SELECT u.film_id " +
                    "FROM likes as u " +
                    "INNER JOIN (SELECT film_id FROM likes WHERE user_id = ? ) as f " +
                    "ON u.film_id = f.film_id " +
                    "WHERE user_id = ? ;", friendId, userId);
        ArrayList<Film> result = new ArrayList<>();
        while (commonFilmsRows.next()) {
            int filmId = commonFilmsRows.getInt("film_id");
            result.add(getFilmById(filmId));
        }
        return result.stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .collect(Collectors.toList());
    }

}
