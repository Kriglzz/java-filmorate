package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.EventDbStorage;
import ru.yandex.practicum.filmorate.dao.FilmDBStorage;
import ru.yandex.practicum.filmorate.dao.UserDBStorage;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class UserDbService implements UserService {
    private final UserDBStorage userStorage;
    private final FilmDBStorage filmStorage;
    private final EventDbStorage eventStorage;

    @Override
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Пользователь {} обновлен.", user);
        return userStorage.updateUser(user);
    }

    @Override
    public User getUserById(int userId) {
        return userStorage.getUserById(userId);
    }

    @Override
    public ArrayList<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public void addFriend(int userId, int friendId) {
        log.info("Пользователь {} пытается добавить {} в список друзей.", userId, friendId);
        User user = userStorage.getUserById(userId);
        if (user.getFriendStatus() == null) {
            user.setFriendStatus(new HashMap<>());
        }
        user.setFriendStatus(userStorage.addFriend(userId, friendId));

        eventStorage.addEvent(new Event(
                null,
                Timestamp.from(Instant.now()),
                userId,
                EventType.FRIEND,
                OperationType.ADD,
                friendId
        ));

        userStorage.updateUser(user);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        log.info("Пользователь {} удаляет {} из списка друзей.", userId, friendId);
        User user = userStorage.getUserById(userId);
        user.getFriendStatus().remove(friendId);

        eventStorage.addEvent(new Event(
                null,
                Timestamp.from(Instant.now()),
                userId,
                EventType.FRIEND,
                OperationType.REMOVE,
                friendId
        ));

        if (user.getFriendStatus() != null
                && user.getFriendStatus().containsKey(userId)) {
            user.setFriendStatus(userStorage.deleteFriend(userId, friendId));
        }
        userStorage.updateUser(user);
    }

    @Override
    public ArrayList<User> getFriends(int userId) {
        User user = userStorage.getUserById(userId);
        ArrayList<User> friends = new ArrayList<>();
        for (int element : user.getFriendStatus().keySet()) {
            User listUser = userStorage.getUserById(element);
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

    @Override
    public void deleteUser(Long userId) {
        log.info("Удаление пользователя с id {}", userId);
        userStorage.deleteUser(userId);
    }

    @Override
    public ArrayList<Film> getRecommendations(int userId) {
        List<Film> films = new ArrayList<>();
        ArrayList<Film> recommendations = new ArrayList<>();
        for (Film film : filmStorage.getAllFilms()) {
            if (film.getLikes().contains(userId)) {
                films.add(film);
            }
        }
        for (Map.Entry<Integer, Integer> entry : findSimilarity(films, userId)) {
            recommendations.addAll(filmStorage.getUnCommonFilms(userId, entry.getKey()));
        }
        return recommendations;
    }

    private List<Map.Entry<Integer, Integer>> findSimilarity(List<Film> films, int userId) {
        // Матрица схожести, где ключ - userId, а значение - сумма схожих лайков
        Map<Integer, Integer> similarityMatrix = new HashMap<>();
        // Заполняем матрицу схожести
        for (Film film : films) {
            for (Integer userWhoLiked : film.getLikes()) {
                if (!(userWhoLiked == userId)) {
                    similarityMatrix.putIfAbsent(userWhoLiked, 0);
                    similarityMatrix.merge(userWhoLiked, 1, Integer::sum);
                }
            }
        }
        //сортируем матрицу по value
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(similarityMatrix.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        return list;
    }

    @Override
    public List<Event> getFeed(int userId) {
        // Если юзер не найден, то вернется 404
        getUserById(userId);
        return eventStorage.getFeed(userId);
    }

}
