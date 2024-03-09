package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserDBService;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserDBService userDBService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userDBService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userDBService.updateUser(user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        return userDBService.getUserById(userId);
    }

    @GetMapping
    public ArrayList<User> getAllUsers() {
        return userDBService.getAllUsers();
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable int userId,
                          @PathVariable int friendId) {
        userDBService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable int userId,
                             @PathVariable int friendId) {
        userDBService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public ArrayList<User> getFriends(@PathVariable int userId) {
        return userDBService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public ArrayList<User> getMutualFriends(@PathVariable int userId,
                                            @PathVariable int otherId) {
        return userDBService.getMutualFriends(userId, otherId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userDBService.deleteUser(userId);
    }
}
