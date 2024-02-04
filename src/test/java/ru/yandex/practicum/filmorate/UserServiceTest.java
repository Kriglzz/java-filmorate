package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    @Test
    public void testFindUserById() {
        User user = new User(
                "bethezds@gmail.com",
                "MoneyLover",
                "Todd",
                LocalDate.of(1970, 10, 6));
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        inMemoryUserStorage.createUser(user);
        User savedUser = inMemoryUserStorage.getUserById(user.getId());
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }
}
