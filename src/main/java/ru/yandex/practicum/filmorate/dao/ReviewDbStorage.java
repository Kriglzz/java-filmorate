package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Review addReview(Review review) {
        String sql = "INSERT INTO reviews (content, is_positive, user_id, film_id, useful) " +
                "VALUES (:content, :is_positive, :user_id, :film_id, :useful)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("content", review.getContent());
        params.addValue("is_positive", review.getIsPositive());
        params.addValue("user_id", review.getUserId());
        params.addValue("film_id", review.getFilmId());
        params.addValue("useful", review.getUseful());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder);
        int id = keyHolder.getKey().intValue();
        review.setReviewId(id);
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        String sql = "UPDATE reviews SET content = :content, is_positive = :is_positive " +
                "WHERE review_id = :review_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("content", review.getContent());
        params.addValue("is_positive", review.getIsPositive());
        params.addValue("review_id", review.getReviewId());
        namedParameterJdbcTemplate.update(sql, params);
        return getById(review.getReviewId()).orElseThrow(() -> new NotFoundException("Data not found"));
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM reviews WHERE review_id=:review_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("review_id", id);
        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public Optional<Review> getById(Integer id) {
        String sql = "SELECT review_id, content, is_positive, user_id, film_id, useful " +
                "FROM reviews WHERE review_id=:review_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("review_id", id);
        List<Review> reviews = namedParameterJdbcTemplate.query(sql, params, new ReviewMapper());
        return reviews.isEmpty() ? Optional.empty() : Optional.of(reviews.get(0));
    }

    @Override
    public List<Review> getAll(Integer id, int count) {
        if (id == -999) {
            String sql = "SELECT review_id, content, is_positive, user_id, film_id, useful FROM reviews " +
                    "GROUP BY review_id ORDER BY useful DESC LIMIT :count";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("count", count);
            return namedParameterJdbcTemplate.query(sql, params, new ReviewMapper());
        } else {
            String sql = "SELECT review_id, content, is_positive, user_id, film_id, useful FROM reviews" +
                    " WHERE film_id=:film_id GROUP BY review_id ORDER BY useful DESC LIMIT :count";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("film_id", id);
            params.addValue("count", count);
            return namedParameterJdbcTemplate.query(sql, params, new ReviewMapper());
        }
    }

    @Override
    public void likeAReview(Integer id, Long userId) {
        String sql = "MERGE INTO film_reviews (review_id, user_id, is_positive)" +
                " VALUES (:review_id, :user_id, :is_positive)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("review_id", id);
        params.addValue("user_id", userId);
        params.addValue("is_positive", Boolean.TRUE);
        namedParameterJdbcTemplate.update(sql, params);
        updateScore(id);
    }

    @Override
    public void dislikeAReview(Integer id, Long userId) {
        String sql = "MERGE INTO film_reviews (review_id, user_id, is_positive)" +
                " VALUES (:review_id, :user_id, :is_positive)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("review_id", id);
        params.addValue("user_id", userId);
        params.addValue("is_positive", Boolean.FALSE);
        namedParameterJdbcTemplate.update(sql, params);
        updateScore(id);
    }

    @Override
    public void deleteScore(Integer id, Long userId) {
        String sql = "DELETE FROM film_reviews WHERE review_id=:review_id AND user_id=:user_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("review_id", id);
        params.addValue("user_id", userId);
        namedParameterJdbcTemplate.update(sql, params);
        updateScore(id);
    }

    private void updateScore(Integer id) {
        String sql = "SELECT SUM(CASE WHEN is_positive = TRUE THEN 1 ELSE -1 END) useful " +
                "FROM film_reviews WHERE review_id=:review_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("review_id", id);
        int score = namedParameterJdbcTemplate.query(sql, params, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("useful");
            }
        }).stream().findAny().orElse(0);
        params.addValue("useful", score);
        namedParameterJdbcTemplate.update("UPDATE reviews SET useful=:useful WHERE review_id=:review_id", params);
    }

    public static class ReviewMapper implements RowMapper<Review> {

        @Override
        public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
            Review review = new Review();
            review.setReviewId(rs.getInt("review_id"));
            review.setContent(rs.getString("content"));
            review.setIsPositive(rs.getBoolean("is_positive"));
            review.setUserId(rs.getInt("user_id"));
            review.setFilmId(rs.getInt("film_id"));
            review.setUseful(rs.getInt("useful"));
            return review;
        }
    }
}
