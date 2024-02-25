package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InMemoryFilmServiceTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindFilmById() {
        Film film = new Film("2007",
                "Give me back my 2007",
                LocalDate.of(2007, 7, 7),
                100);
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage(jdbcTemplate);
        inMemoryFilmStorage.addFilm(film);
        Film savedFilm = inMemoryFilmStorage.getFilmById(film.getId());
        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }
}
