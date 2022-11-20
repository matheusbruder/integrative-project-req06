package com.example.finalproject.dto;

import com.example.finalproject.model.Review;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class ReviewUpdateDTO {

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private int rating;

    private String comment;

    public static Review convertToReview(ReviewUpdateDTO reviewUpdateDTO) {
        return Review.builder()
                .dateTime(LocalDateTime.now())
                .rating(reviewUpdateDTO.getRating())
                .comment(reviewUpdateDTO.getComment())
                .build();
    }
}
