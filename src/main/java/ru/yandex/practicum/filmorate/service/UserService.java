package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(User user);

    User getUserById(int userId);

    ArrayList<User> getAllUsers();

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    ArrayList<User> getFriends(int userId);

    ArrayList<User> getMutualFriends(int firstUserId, int secondUserId);

    void deleteUser(Long userId);

    ArrayList<Film> getRecommendations(int userId);

    List<Event> getFeed(int userId);
}
