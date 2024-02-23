package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserService {
    public User createUser(User user);

    public User updateUser(User user);

    public User getUserById(int userId);

    public ArrayList<User> getAllUsers();

    public void addFriend(int userId, int friendId);

    public void deleteFriend(int userId, int friendId);

    public ArrayList<User> getFriends(int userId);

    public ArrayList<User> getMutualFriends(int firstUserId, int secondUserId);
}
