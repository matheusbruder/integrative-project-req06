package com.example.finalproject.service;

import com.example.finalproject.model.Advertisement;
import com.example.finalproject.model.Review;

import java.util.List;

public interface IReviewService {
    /**
     * It creates a review for an advertisement, and calculates the average rating of the advertisement.
     *
     * @param review the review object that is being created
     * @return Review
     */
    Review createReview(Review review);

    /**
     * This function updates a review by taking in a review object and a review code. It then finds the review by the
     * review code and sets the new rating and new comment to the review. Then calculates the updated
     * average rating and sets to the advertisement. It then saves the updated review and returns it.
     *
     * @param review     The review object that is being updated.
     * @param reviewCode The code of the review to be updated.
     * @return The updated review is being returned.
     */
    Review updateReview(Review review, Long reviewCode);

    /**
     * It takes a category and a limit as parameters, validates them, finds all the advertisements in the given category,
     * sorts them by their average rating and returns the top-rated advertisements.
     *
     * @param category The category of the advertisement.
     * @param limit    The number of advertisements to return.
     * @return List of advertisements
     */
    List<Advertisement> findTopRatedAdvertisementsByCategory(String category, Integer limit);

    /**
     * This function returns a list of reviews for a buyer with the given buyer code.
     *
     * @param buyerCode The buyer's code
     * @return A list of reviews for a buyer
     */
    List<Review> findAllReviewsByBuyer(Long buyerCode);

    /**
     * It deletes a review from the database.
     *
     * @param reviewCode The unique code of the review to be deleted.
     */
    void deleteReview(Long reviewCode);
}
