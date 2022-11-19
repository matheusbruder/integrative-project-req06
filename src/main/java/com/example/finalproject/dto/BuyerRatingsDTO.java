package com.example.finalproject.dto;

import com.example.finalproject.model.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BuyerRatingsDTO {

    private final String advertisementName;
    private final int rating;
    private final String comment;
    private final LocalDateTime dateTime;

    public BuyerRatingsDTO(Review review) {
        this.advertisementName = review.getAdvertisement().getName();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.dateTime = review.getDateTime();
    }

    private static BuyerRatingsDTO convertToResponse(Review review) {
        return new BuyerRatingsDTO(review);
    }

    public static List<BuyerRatingsDTO> convertListToResponse(List<Review> reviewList) {
        return reviewList.stream().map(BuyerRatingsDTO::convertToResponse)
                .collect(Collectors.toList());
    }
}
