package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review addReview(@Valid @RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id) {
        reviewService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Review getById(@PathVariable Integer id) {
        return reviewService.getById(id);
    }

    @GetMapping
    public List<Review> getAll(
            @RequestParam(name = "filmId", defaultValue = "-1", required = false) Integer filmId,
            @RequestParam(name = "count", defaultValue = "10", required = false) Integer count) {
        return reviewService.getAll(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeAReview(@PathVariable Integer id, @PathVariable Long userId) {
        reviewService.likeAReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void dislikeAReview(@PathVariable Integer id, @PathVariable Long userId) {
        reviewService.dislikeAReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeOfReview(@PathVariable Integer id, @PathVariable Long userId) {
        reviewService.deleteLikeOfReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislikeOfReview(@PathVariable Integer id, @PathVariable Long userId) {
        reviewService.deleteDislikeOfReview(id, userId);
    }
}
