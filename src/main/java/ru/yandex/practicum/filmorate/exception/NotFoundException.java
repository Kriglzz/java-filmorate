package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("Data not found");
    }
}