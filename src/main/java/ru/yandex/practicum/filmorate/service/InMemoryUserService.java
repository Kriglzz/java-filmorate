package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDBStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class InMemoryUserService implements UserService {
    private final UserDBStorage userDBStorage;

    @Override
    public User createUser(User user) {
        return userDBStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Пользователь {} обновлен.", user);
        return userDBStorage.updateUser(user);
    }

    @Override
    public User getUserById(int userId) {
        return userDBStorage.getUserById(userId);
    }

    @Override
    public ArrayList<User> getAllUsers() {
        return userDBStorage.getAllUsers();
    }

    @Override
    public void addFriend(int userId, int friendId) {
        log.info("Пользователь {} пытается добавить {} в список друзей.", userId, friendId);
        User user = userDBStorage.getUserById(userId);
        if (user.getFriendStatus() == null) {
            user.setFriendStatus(new HashMap<>());
        }
        user.setFriendStatus(userDBStorage.addFriend(userId, friendId));
        userDBStorage.updateUser(user);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        log.info("Пользователь {} удаляет {} из списка друзей.", userId, friendId);
        User user = userDBStorage.getUserById(userId);
        user.getFriendStatus().remove(friendId);
        if (user.getFriendStatus() != null
                && user.getFriendStatus().containsKey(userId)) {
            user.setFriendStatus(userDBStorage.deleteFriend(userId, friendId));
        }
        userDBStorage.updateUser(user);
    }

    @Override
    public ArrayList<User> getFriends(int userId) {
        User user = userDBStorage.getUserById(userId);
        ArrayList<User> friends = new ArrayList<>();
        for (int element : user.getFriendStatus().keySet()) {
            User listUser = userDBStorage.getUserById(element);
            friends.add(listUser);
        }
        return friends;
    }

    @Override
    public ArrayList<User> getMutualFriends(int firstUserId, int secondUserId) {
        Map<Integer, String> firstUserFriends = getUserById(firstUserId).getFriendStatus();
        Map<Integer, String> secondUserFriends = getUserById(secondUserId).getFriendStatus();

        ArrayList<User> mutualFriends = new ArrayList<>();

        for (Integer key : firstUserFriends.keySet()) {
            if (secondUserFriends.containsKey(key)) {
                mutualFriends.add(getUserById(key));
            }
        }
        return mutualFriends;
    }
}
