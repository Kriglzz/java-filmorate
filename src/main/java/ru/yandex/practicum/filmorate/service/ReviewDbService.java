package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.EventDbStorage;
import ru.yandex.practicum.filmorate.dao.FilmDBStorage;
import ru.yandex.practicum.filmorate.dao.ReviewDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDBStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewDbService implements ReviewService {

    private final ReviewDbStorage reviewStorage;

    private final UserDBStorage userStorage;

    private final FilmDBStorage filmStorage;

    private final EventDbStorage eventStorage;

    @Override
    public Review addReview(Review review) {
        log.info("Добавление отзыва {}.", review);
        userStorage.getUserById(review.getUserId());
        filmStorage.getFilmById(review.getFilmId());
        Review added = reviewStorage.addReview(review);

        eventStorage.addEvent(new Event(
                null,
                Timestamp.from(Instant.now()),
                added.getUserId(),
                EventType.REVIEW,
                OperationType.ADD,
                added.getReviewId()
        ));

        return added;
    }

    @Override
    public Review updateReview(Review review) {
        log.info("Обновление отзыва {}.", review);
        userStorage.getUserById(review.getUserId());
        filmStorage.getFilmById(review.getFilmId());
        reviewStorage.getById(review.getReviewId()).orElseThrow(NotFoundException::new);
        Review updated = reviewStorage.updateReview(review);

        eventStorage.addEvent(new Event(
                null,
                Timestamp.from(Instant.now()),
                updated.getUserId(),
                EventType.REVIEW,
                OperationType.UPDATE,
                updated.getReviewId()
        ));

        return updated;
    }

    @Override
    public void deleteById(Integer id) {
        log.info("Удаление отзыва с id: {}", id);
        Review deleted = reviewStorage.getById(id).orElseThrow(NotFoundException::new);

        eventStorage.addEvent(new Event(
                null,
                Timestamp.from(Instant.now()),
                deleted.getUserId(),
                EventType.REVIEW,
                OperationType.REMOVE,
                deleted.getReviewId()
        ));

        reviewStorage.deleteById(id);
    }

    @Override
    public Review getById(Integer id) {
        log.info("Получение отзыва с id: {}", id);
        return reviewStorage.getById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Review> getAll(Integer id, int count) {
        log.info("Получение всех отзывов");
        return reviewStorage.getAll(id, count);
    }

    @Override
    public void likeAReview(Integer reviewId, Long userId) {
        log.info("Лайк на отзыв с id {} пользователем с id: {}", reviewId, userId);
        reviewStorage.getById(reviewId).orElseThrow(NotFoundException::new);
        reviewStorage.likeAReview(reviewId, userId);
    }

    @Override
    public void dislikeAReview(Integer reviewId, Long userId) {
        log.info("Дизлайк на отзыв с id {} пользователем с id: {}", reviewId, userId);
        reviewStorage.getById(reviewId).orElseThrow(NotFoundException::new);
        reviewStorage.dislikeAReview(reviewId, userId);
    }

    @Override
    public void deleteLikeOfReview(Integer reviewId, Long userId) {
        log.info("Удаление лайка на отзыв с id {} пользователем с id: {}", reviewId, userId);
        reviewStorage.getById(reviewId).orElseThrow(NotFoundException::new);
        reviewStorage.deleteScore(reviewId, userId);
    }

    @Override
    public void deleteDislikeOfReview(Integer reviewId, Long userId) {
        log.info("Удаление дизлайка на отзыв с id {} пользователем с id: {}", reviewId, userId);
        reviewStorage.getById(reviewId).orElseThrow(NotFoundException::new);
        reviewStorage.deleteScore(reviewId, userId);
    }
}
