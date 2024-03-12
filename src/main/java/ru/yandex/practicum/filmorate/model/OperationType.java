package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationType {
    REMOVE("REMOVE"),
    ADD("ADD"),
    UPDATE("UPDATE");

    private final String value;
}
