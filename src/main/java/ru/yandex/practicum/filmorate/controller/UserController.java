package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.InMemoryUserService;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final InMemoryUserService inMemoryUserService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return inMemoryUserService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return inMemoryUserService.updateUser(user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        return inMemoryUserService.getUserById(userId);
    }

    @GetMapping
    public ArrayList<User> getAllUsers() {
        return inMemoryUserService.getAllUsers();
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable int userId,
                          @PathVariable int friendId) {
        inMemoryUserService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable int userId,
                             @PathVariable int friendId) {
        inMemoryUserService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public ArrayList<User> getMutualFriends(@PathVariable int userId,
                                            @PathVariable int otherId) {
        return inMemoryUserService.getMutualFriends(userId, otherId);
    }
}
