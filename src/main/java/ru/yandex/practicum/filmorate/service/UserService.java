package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserService {
    User createUser(User user);

    User updateUser(User user);

    User getUserById(int userId);

    ArrayList<User> getAllUsers();

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    ArrayList<User> getFriends(int userId);

    ArrayList<User> getMutualFriends(int firstUserId, int secondUserId);
}
