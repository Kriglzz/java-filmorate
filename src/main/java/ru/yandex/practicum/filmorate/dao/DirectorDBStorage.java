package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import static ru.yandex.practicum.filmorate.model.Film.DirectorWrap;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DirectorDBStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    public static RowMapper<DirectorWrap> directorRowMapper() {
        DirectorWrap director = new DirectorWrap();
        return (rs, rowNum) -> {
            director.setId(rs.getInt("director_id"));
            director.setName(rs.getString("director_name"));
            return director;
        };
    }

    @Override
    public DirectorWrap addDirector(DirectorWrap director) {
        String sql = "INSERT INTO directors (director_name) " +
                "VALUES (?);";
        KeyHolder key = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, director.getName());
            return preparedStatement;
        }, key);
        int id = key.getKey().intValue();
        director.setId(id);

        Optional<DirectorWrap> addedDirector = Optional.of(director);
        return addedDirector.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @Override
    public DirectorWrap getDirectorById(int directorId) {
        String sql = "SELECT * FROM directors WHERE director_id = ?;";
        DirectorWrap director;
        try {
            director = jdbcTemplate.queryForObject(sql, directorRowMapper(), directorId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return director;
    }

    @Override
    public DirectorWrap updateDirector(DirectorWrap director) {
        String sql = "UPDATE directors SET director_name = ? WHERE director_id = ?;";
        getDirectorById(director.getId());
        jdbcTemplate.update(sql, director.getName(), director.getId());
        return getDirectorById(director.getId());
    }

    @Override
    public void deleteDirector(int directorId) {
        String sql = "DELETE FROM directors WHERE director_id = ?;";
        getDirectorById(directorId);
        jdbcTemplate.update(sql, directorId);
    }

    @Override
    public List<DirectorWrap> getAllDirectors() {
        String sql = "SELECT * FROM directors;";
        return jdbcTemplate.query(sql, directorRowMapper());
    }
}
