package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dataBase.InMemoryUserDataBase;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final InMemoryUserDataBase inMemoryUserDataBase;

    public User createUser(User user) {
        log.info("Пользователь {} добавлен", user);
        return inMemoryUserDataBase.createUser(user);
    }

    public User updateUser(User user) {
        log.info("Пользователь {} обновлен", user);
        return inMemoryUserDataBase.updateUser(user);
    }

    public Map<Integer, User> getAllUsers() {
        return inMemoryUserDataBase.getAllUsers();
    }
}
