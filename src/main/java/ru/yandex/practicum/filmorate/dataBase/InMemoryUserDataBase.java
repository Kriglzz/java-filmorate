package ru.yandex.practicum.filmorate.dataBase;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Objects;

import java.util.ArrayList;

@Component
public class InMemoryUserDataBase implements UserDataBase {
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
        public ArrayList<User> getAllUsers () {
            return users;
        }
}