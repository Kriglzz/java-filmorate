package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDBStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
        User user1 = userDBStorage.getUserById(userId);
        User user2 = userDBStorage.getUserById(friendId);
        if (user2.getFriendStatus() != null
                && user2.getFriendStatus().containsKey(userId)
                && user2.getFriendStatus().get(userId).equals("friendshipRequested")) {
            user1.addFriend(friendId);
            user2.addFriend(userId);
            log.info("Пользователи {} и {} добавили друг друга в друзья!", userId, friendId);
        } else {
            user1.addFriendRequest(friendId);
            log.info("Пользователь {} отправил пользователю {} запрос на добавление в друзья.", userId, friendId);
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        log.info("Пользователь {} удаляет {} из списка друзей.", userId, friendId);
        User user1 = userDBStorage.getUserById(userId);
        User user2 = userDBStorage.getUserById(friendId);
        user1.deleteFriend(friendId);
        user2.deleteFriend(userId);
    }

    @Override
    public ArrayList<User> getFriends(int userId) {
        User user = userDBStorage.getUserById(userId);
        ArrayList<User> friends = new ArrayList<>();
        for (int element : user.getFriends()) {
            User listUser = userDBStorage.getUserById(element);
            friends.add(listUser);
        }
        return friends;
    }

    @Override
    public ArrayList<User> getMutualFriends(int firstUserId, int secondUserId) {
        User user1 = userDBStorage.getUserById(firstUserId);
        User user2 = userDBStorage.getUserById(secondUserId);
        Set<Integer> commonElements = new HashSet<>(user1.getFriends());
        commonElements.retainAll(user2.getFriends());
        ArrayList<User> mutualFriends = new ArrayList<>();
        for (int element : commonElements) {
            User user = userDBStorage.getUserById(element);
            mutualFriends.add(user);
        }
        return mutualFriends;
    }
}
