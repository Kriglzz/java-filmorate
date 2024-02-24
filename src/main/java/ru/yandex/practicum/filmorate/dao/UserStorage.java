package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserStorage {
    public User createUser(User user);

    public User updateUser(User user);

    public ArrayList<User> getAllUsers();

    public User getUserById(int userId);
}
