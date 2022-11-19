package com.example.finalproject.service;

import com.example.finalproject.model.Advertisement;
import com.example.finalproject.model.Review;

import java.util.List;

public interface IReviewService {

    Review createReview(Review review);
    Review updateReview(Review review, Long reviewCode);
    List<Advertisement> findTopRatedAdvertisementsByCategory(String category, Integer limit);
    void deleteReview(Long reviewCode);
}
