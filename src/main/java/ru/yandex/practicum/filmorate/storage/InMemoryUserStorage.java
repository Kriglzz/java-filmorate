package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final ArrayList<User> users = new ArrayList<>();

    @Override
    public User createUser(User user) {
        users.add(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        for (User u : users) {
            if (Objects.equals(u.getId(), user.getId())) {
                users.remove(u);
                break;
            }
        }
        users.add(user);
        return user;
    }


    @Override
    public ArrayList<User> getAllUsers() {
        return users;
    }

    @Override
    public User getUserById(int userId) {
        Optional<User> foundUser = users.stream()
                .filter(user -> user.getId() == userId)
                .findFirst();
        return foundUser.orElse(null);
    }
}
