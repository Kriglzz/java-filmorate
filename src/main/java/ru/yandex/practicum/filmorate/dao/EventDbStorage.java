package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Event addEvent(Event event) {
        String sql = "INSERT INTO events (timestamp, entity_id, user_id, event_type, operation) " +
                "VALUES (:timestamp, :entity_id, :user_id, :event_type, :operation)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("timestamp", event.getTimestamp());
        params.addValue("entity_id", event.getEntityId());
        params.addValue("user_id", event.getUserId());
        params.addValue("event_type", event.getEventType().getValue());
        params.addValue("operation", event.getOperation().getValue());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder);
        Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        event.setEventId(id);
        return event;
    }

    @Override
    public List<Event> getFeed(int userId) {
        String sql = "SELECT * FROM events " +
                "WHERE user_id = :user_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        return namedParameterJdbcTemplate.query(sql, params, new EventDbStorage.EventMapper());
    }

    public static class EventMapper implements RowMapper<Event> {

        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {

            return new Event(
                    rs.getInt("event_id"),
                    rs.getTimestamp("timestamp"),
                    rs.getInt("user_id"),
                    EventType.valueOf(rs.getString("event_type")),
                    OperationType.valueOf(rs.getString("operation")),
                    rs.getInt("entity_id")
            );
        }
    }
}
