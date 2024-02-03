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
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    public User createUser(User user) {
        log.info("Пользователь {} добавлен", user);
        return inMemoryUserStorage.createUser(user);
    }

    public User updateUser(User user) {
        log.info("Пользователь {} обновлен", user);
        return inMemoryUserStorage.updateUser(user);
    }

    public ArrayList<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        User user1 = inMemoryUserStorage.getUserById(userId);
        User user2 = inMemoryUserStorage.getUserById(friendId);
        user1.addFriend(friendId);
        user2.addFriend(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        User user1 = inMemoryUserStorage.getUserById(userId);
        User user2 = inMemoryUserStorage.getUserById(friendId);
        user1.deleteFriend(friendId);
        user2.deleteFriend(userId);
    }

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
