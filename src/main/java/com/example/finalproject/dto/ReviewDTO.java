package com.example.finalproject.dto;

import com.example.finalproject.model.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewDTO {
    private final Long reviewCode;
    private final LocalDateTime dateTime;
    private final String advertisementName;
    private final String buyerName;
    private final int rating;
    private final String comment;

    public ReviewDTO(Review review) {
        this.reviewCode = review.getReviewCode();
        this.dateTime = review.getDateTime();
        this.advertisementName = review.getAdvertisement().getName();
        this.buyerName = review.getBuyer().getName();
        this.rating = review.getRating();
        this.comment = review.getComment();
    }

    public static ReviewDTO convertToResponse(Review review) {
        return new ReviewDTO(review);
    }
}
