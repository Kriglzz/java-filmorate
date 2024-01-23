package ru.yandex.practicum.filmorate;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class UserValidatorTest {
    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void addInappropriateEmailFilmTest() {
        User user = new User(
                "IT JUST WORKS",
                "MoneyLover",
                "Todd",
                LocalDate.of(1970, 10, 6));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    public void addInappropriateLoginFilmTest() {
        User user = new User(
                "bethezds@gmail.com",
                "IT JUST WORKS",
                "Todd",
                LocalDate.of(1970, 10, 6));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    public void addBlankNameFilmTest() {
        User user = new User(
                "bethezds@gmail.com",
                "MoneyLover",
                "",
                LocalDate.of(1970, 10, 6));
        assertEquals("MoneyLover", user.getName());
    }

    @Test
    public void addInappropriateDateFilmTest() {
        User user = new User(
                "bethezds@gmail.com",
                "MoneyLover",
                "Todd",
                LocalDate.of(9999, 10, 6));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }
}
