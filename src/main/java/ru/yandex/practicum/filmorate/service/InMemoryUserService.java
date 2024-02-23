package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class InMemoryUserService implements UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Override
    public User createUser(User user) {
        return inMemoryUserStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Пользователь {} обновлен.", user);
        return inMemoryUserStorage.updateUser(user);
    }

    @Override
    public User getUserById(int userId) {
        return inMemoryUserStorage.getUserById(userId);
    }

    @Override
    public ArrayList<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    @Override
    public void addFriend(int userId, int friendId) {
        log.info("Пользователь {} добавил {} в список друзей.", userId, friendId);
        User user1 = inMemoryUserStorage.getUserById(userId);
        User user2 = inMemoryUserStorage.getUserById(friendId);
        user1.addFriend(friendId);
        user2.addFriend(userId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        log.info("Пользователь {} удалил {} из списка друзей.", userId, friendId);
        User user1 = inMemoryUserStorage.getUserById(userId);
        User user2 = inMemoryUserStorage.getUserById(friendId);
        user1.deleteFriend(friendId);
        user2.deleteFriend(userId);
    }

    @Override
    public ArrayList<User> getFriends(int userId) {
        User user = inMemoryUserStorage.getUserById(userId);
        ArrayList<User> friends = new ArrayList<>();
        for (int element : user.getFriends()) {
            User listUser = inMemoryUserStorage.getUserById(element);
            friends.add(listUser);
        }
        return friends;
    }

    @Override
    public ArrayList<User> getMutualFriends(int firstUserId, int secondUserId) {
        User user1 = inMemoryUserStorage.getUserById(firstUserId);
        User user2 = inMemoryUserStorage.getUserById(secondUserId);
        Set<Integer> commonElements = new HashSet<>(user1.getFriends());
        commonElements.retainAll(user2.getFriends());
        ArrayList<User> mutualFriends = new ArrayList<>();
        for (int element : commonElements) {
            User user = inMemoryUserStorage.getUserById(element);
            mutualFriends.add(user);
        }
        return mutualFriends;
    }
}
