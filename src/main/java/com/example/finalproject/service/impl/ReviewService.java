package com.example.finalproject.service.impl;

import com.example.finalproject.exception.InvalidArgumentException;
import com.example.finalproject.exception.NotFoundException;
import com.example.finalproject.model.Advertisement;
import com.example.finalproject.model.Buyer;
import com.example.finalproject.model.Review;
import com.example.finalproject.repository.AdvertisementRepo;
import com.example.finalproject.repository.BuyerRepo;
import com.example.finalproject.repository.PurchaseItemRepo;
import com.example.finalproject.repository.ReviewRepo;
import com.example.finalproject.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    BuyerRepo buyerRepo;

    @Autowired
    AdvertisementRepo advertisementRepo;

    @Autowired
    PurchaseItemRepo purchaseItemRepo;

    @Override
    public Review createReview(Review review) {
        Buyer buyer = buyerRepo.findById(review.getBuyer().getBuyerCode()).orElseThrow(() -> new NotFoundException("Buyer not found"));
        review.setBuyer(buyer);
        Advertisement advertisement = advertisementRepo.findById(review.getAdvertisement().getAdvertisementCode()).orElseThrow(() -> new NotFoundException("Advertisement not found"));
        review.setAdvertisement(advertisement);

        repeatedReviewValidation(review);

        purchaseItemRepo.findByBuyerCodeAndAdvertisementCodeWhenPurchaseOrderFinished(buyer.getBuyerCode(), advertisement.getAdvertisementCode()).orElseThrow(() -> new InvalidArgumentException(buyer.getName() + " hasn't bought any " + advertisement.getName() + " yet, so submitting a review is not allowed yet."));

        double average = calculateAverage(review, advertisement);
        advertisement.setAverageRating(average);

        return reviewRepo.save(review);
    }

    @Override
    public Review updateReview(Review review, Long reviewCode) {
        Review updatedReview = reviewRepo.findById(reviewCode).orElseThrow(() -> new NotFoundException("Review not found"));
        updatedReview.setRating(review.getRating());
        updatedReview.setComment(review.getComment());

        double average = calculateAverage(updatedReview, updatedReview.getAdvertisement());
        updatedReview.getAdvertisement().setAverageRating(average);

        return reviewRepo.save(updatedReview);
    }

    private void repeatedReviewValidation(Review review) {
        List<Review> reviewList = reviewRepo.findAll().stream()
                .filter(r -> r.getBuyer().getBuyerCode().equals(review.getBuyer().getBuyerCode()))
                .filter(r -> r.getAdvertisement().getAdvertisementCode().equals(review.getAdvertisement().getAdvertisementCode()))
                .collect(Collectors.toList());
        if (!reviewList.isEmpty())
            throw new InvalidArgumentException(review.getBuyer().getName() + " has already submitted a review on " + review.getAdvertisement().getName());
    }


    private double calculateAverage(Review review, Advertisement advertisement) {
        List<Review> reviewList = reviewRepo.findByAdvertisement(advertisement);
        reviewList.add(review);

        double sumRating = reviewList.stream()
                .mapToDouble(Review::getRating)
                .sum();

        BigDecimal average = new BigDecimal(Double.toString(sumRating / reviewList.size()));
        average = average.setScale(1, RoundingMode.HALF_UP);

        return average.doubleValue();
    }
}
