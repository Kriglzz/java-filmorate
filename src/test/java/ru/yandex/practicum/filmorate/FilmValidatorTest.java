package ru.yandex.practicum.filmorate;

import org.junit.Before;
import org.junit.Test;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.Assert.*;

public class FilmValidatorTest {
    private Validator validator;
    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    public void addInappropriateNameFilmTest() {
        Film film = new Film(
                null,
                "Give me back my 2007",
                LocalDate.of(2007, 7, 7),
                100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1,violations.size());
    }

    @Test
    public void addInappropriateYearFilmTest() {
        Film film = new Film(
                "2007",
                "Give me back my 2007",
                LocalDate.of(777, 7, 7),
                100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1,violations.size());
    }

    @Test
    public void addInappropriateDurationFilmTest() {
        Film film = new Film(
                "2007",
                "Description1",
                LocalDate.of(2007, 7, 7),
                -100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1,violations.size());
    }
}
