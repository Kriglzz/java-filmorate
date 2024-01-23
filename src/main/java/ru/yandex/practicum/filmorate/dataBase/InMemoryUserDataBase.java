package ru.yandex.practicum.filmorate.dataBase;

import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@RestController
public class InMemoryUserDataBase implements UserDataBase {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        int id = user.getId();
        User savedUser = users.get(id);
        if (savedUser == null) {
            return null;
        }
        users.put(id, user);
        return user;
    }

    @Override
    public Map<Integer, User> getAllUsers() {
        return users;
    }
}
