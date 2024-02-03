package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    public User createUser(User user) {
        log.info("Пользователь {} добавлен", user);
        return inMemoryUserStorage.createUser(user);
    }

    public User updateUser(User user) {
        log.info("Пользователь {} обновлен", user);
        return inMemoryUserStorage.updateUser(user);
    }

    public ArrayList<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }
}
