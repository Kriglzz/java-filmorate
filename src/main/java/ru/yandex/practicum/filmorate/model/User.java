package ru.yandex.practicum.filmorate.model;

import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.validator.NoBlankSpace;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Validated
@lombok.Data
public class User {
    private static int userCount = 0;
    @lombok.NonNull
    @NotBlank
    @Email
    private final String email;
    @lombok.NonNull
    @NotBlank
    @NoBlankSpace
    private final String login;
    @lombok.NonNull
    @Past
    private final LocalDate birthday;
    @lombok.NonNull
    private String name;
    @lombok.NonNull
    private int id;
    private Set<Integer> friends = new HashSet<>();
    private Map<Integer, String> friendStatus = new HashMap<>();

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        isNameNotBlank();
    }

    @AssertTrue(message = "Указано пустое имя. Будет использован login пользователя.")
    public boolean isNameNotBlank() {
        if (name.isBlank()) {
            name = login;
            return false;
        } else {
            return true;
        }
    }


    public void addFriendRequest(int friendId) {
        friendStatus.put(friendId, "friendshipRequested");
    }

    public void addFriend(int friendId) {
        friends.add(friendId);
        friendStatus.put(friendId, "isFriend");
    }

    public void deleteFriend(int friendId) {
        friends.remove(friendId);
        friendStatus.put(friendId, "notFriend");
    }
}
