package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    ArrayList<User> getAllUsers();

    User getUserById(int userId);

    HashMap<Integer, String> addFriend(int userId, int friendId);

    HashMap<Integer, String> deleteFriend(int userId, int friendId);
}
