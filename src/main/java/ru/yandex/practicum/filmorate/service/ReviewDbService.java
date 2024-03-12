package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDBStorage;
import ru.yandex.practicum.filmorate.dao.ReviewDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDBStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewDbService implements ReviewService {

    private final ReviewDbStorage reviewStorage;

    private final UserDBStorage userDBStorage;

    private final FilmDBStorage filmDBStorage;

    @Override
    public Review addReview(Review review) {
        log.info("Добавление отзыва {}.", review);
        userDBStorage.getUserById(review.getUserId());
        filmDBStorage.getFilmById(review.getFilmId());
        return reviewStorage.addReview(review);
    }

    @Override
    public Review updateReview(Review review) {
        log.info("Обновление отзыва {}.", review);
        userDBStorage.getUserById(review.getUserId());
        filmDBStorage.getFilmById(review.getFilmId());
        reviewStorage.getById(review.getReviewId()).orElseThrow(() -> new NotFoundException("Data not found"));
        return reviewStorage.updateReview(review);
    }

    @Override
    public void deleteById(Integer id) {
        log.info("Удаление отзыва с id: {}", id);
        reviewStorage.deleteById(id);
    }

    @Override
    public Review getById(Integer id) {
        log.info("Получение отзыва с id: {}", id);
        return reviewStorage.getById(id).orElseThrow(() -> new NotFoundException("Data not found"));
    }

    @Override
    public List<Review> getAll(Integer id, int count) {
        log.info("Получение всех отзывов");
        return reviewStorage.getAll(id, count);
    }

    @Override
    public void likeAReview(Integer reviewId, Long userId) {
        log.info("Лайк на отзыв с id {} пользователем с id: {}", reviewId, userId);
        reviewStorage.getById(reviewId).orElseThrow(() -> new NotFoundException("Data not found"));
        reviewStorage.likeAReview(reviewId, userId);
    }

    @Override
    public void dislikeAReview(Integer reviewId, Long userId) {
        log.info("Дизлайк на отзыв с id {} пользователем с id: {}", reviewId, userId);
        reviewStorage.getById(reviewId).orElseThrow(() -> new NotFoundException("Data not found"));
        reviewStorage.dislikeAReview(reviewId, userId);
    }

    @Override
    public void deleteLikeOfReview(Integer reviewId, Long userId) {
        log.info("Удаление лайка на отзыв с id {} пользователем с id: {}", reviewId, userId);
        reviewStorage.getById(reviewId).orElseThrow(() -> new NotFoundException("Data not found"));
        reviewStorage.deleteScore(reviewId, userId);
    }

    @Override
    public void deleteDislikeOfReview(Integer reviewId, Long userId) {
        log.info("Удаление дизлайка на отзыв с id {} пользователем с id: {}", reviewId, userId);
        reviewStorage.getById(reviewId).orElseThrow(() -> new NotFoundException("Data not found"));
        reviewStorage.deleteScore(reviewId, userId);
    }
}
