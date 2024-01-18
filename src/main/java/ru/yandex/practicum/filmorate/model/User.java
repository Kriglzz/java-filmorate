package ru.yandex.practicum.filmorate.model;

import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.validator.NoBlankSpace;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;

@Validated
@lombok.Data
public class User {
    @lombok.NonNull
    @NotBlank
    @Email
    private final String email;
    @lombok.NonNull
    @NotBlank
    @NoBlankSpace
    private final String login;
    @lombok.NonNull
    private final String name;
    @lombok.NonNull
    @Past
    private final LocalDateTime birthday;
    @lombok.NonNull
    private int id;
    public User(int id, String email, String login, String name, LocalDateTime birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    @AssertTrue(message = "Указано пустое имя. Будет использован login пользователя")
    private boolean isNameSameAsLogin() {
        return name.isEmpty() || name.equals(login);
    }
}
