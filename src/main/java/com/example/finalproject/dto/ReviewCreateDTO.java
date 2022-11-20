package com.example.finalproject.dto;

import com.example.finalproject.model.Advertisement;
import com.example.finalproject.model.Buyer;
import com.example.finalproject.model.Review;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
public class ReviewCreateDTO {

    @NotNull
    @Positive
    private Long advertisementCode;

    @NotNull
    private Long buyerCode;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private int rating;

    private String comment;

    public static Review convertToReview(ReviewCreateDTO reviewCreateDTO) {
        return Review.builder()
                .dateTime(LocalDateTime.now())
                .advertisement(Advertisement.builder().advertisementCode(reviewCreateDTO.getAdvertisementCode()).build())
                .buyer(Buyer.builder().buyerCode(reviewCreateDTO.getBuyerCode()).build())
                .rating(reviewCreateDTO.getRating())
                .comment(reviewCreateDTO.getComment())
                .build();
    }
}
