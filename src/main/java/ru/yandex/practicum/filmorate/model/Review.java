package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"reviewId"})
@SuperBuilder
public class Review {

    private Integer reviewId;

    @NotNull
    @NotBlank
    private String content;

    @NotNull
    private Boolean isPositive;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer filmId;

    @Builder.Default
    private Integer useful = 0;

}
