package com.example.finalproject.service;

import com.example.finalproject.model.Review;

public interface IReviewService {

    Review createReview(Review review);
    Review updateReview(Review review, Long reviewCode);

    void deleteReview(Long reviewCode);
}
