package com.example.finalproject.dto;

import com.example.finalproject.model.Review;
import lombok.Getter;

import java.util.List;

@Getter
public class BuyerReviewDTO {

    private final String buyerName;
    private final List<BuyerRatingsDTO> ratings;

    BuyerReviewDTO(List<Review> reviewList) {
        this.buyerName = reviewList.get(0).getBuyer().getName();
        this.ratings = BuyerRatingsDTO.convertListToResponse(reviewList);
    }

    public static BuyerReviewDTO convertToResponse(List<Review> reviewList) {
        return new BuyerReviewDTO(reviewList);
    }
}
