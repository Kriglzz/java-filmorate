package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    Review addReview(Review review);

    Review updateReview(Review review);

    void deleteById(Integer id);

    Optional<Review> getById(Integer id);

    List<Review> getAll(Integer id, int count);

    void likeAReview(Integer id, Long userId);

    void dislikeAReview(Integer id, Long userId);

    void deleteScore(Integer id, Long userId);
}
