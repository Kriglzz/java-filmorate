package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.UserDBStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbServiceTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        User user = new User(
                "bethezds@gmail.com",
                "MoneyLover",
                "Todd",
                LocalDate.of(1970, 10, 6));
        UserDBStorage userDBStorage = new UserDBStorage(jdbcTemplate);
        userDBStorage.createUser(user);
        User savedUser = userDBStorage.getUserById(user.getId());
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }
}
