package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {

    Review addReview(Review review);

    Review updateReview(Review review);

    void deleteById(Integer id);

    Review getById(Integer id);

    List<Review> getAll(Integer id, int count);

    void likeAReview(Integer id, Long userId);

    void dislikeAReview(Integer id, Long userId);

    void deleteLikeOfReview(Integer id, Long userId);

    void deleteDislikeOfReview(Integer id, Long userId);
}
