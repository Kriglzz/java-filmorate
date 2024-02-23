package ru.yandex.practicum.filmorate.exception;

public class UnknownUpdateException extends RuntimeException {

    public UnknownUpdateException(final String message) {
        super(message);
    }
}
