package ru.yandex.practicum.filmorate.dataBase;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserDataBase {
    public User addUser(User user);

    public User updateUser(User user);

    public Map<Integer, User> getAllUsers();
}
