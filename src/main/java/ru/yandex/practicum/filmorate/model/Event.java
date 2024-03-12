package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;

@lombok.Data
@AllArgsConstructor
public class Event {
    private Integer eventId;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private final Timestamp timestamp;
    private final Integer userId;
    private final EventType eventType;
    private final OperationType operation;
    private final Integer entityId;
}
