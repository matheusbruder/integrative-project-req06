package com.example.finalproject.controller;

import com.example.finalproject.dto.AdvertisementDTO;
import com.example.finalproject.dto.ReviewCreateDTO;
import com.example.finalproject.dto.ReviewDTO;
import com.example.finalproject.dto.ReviewUpdateDTO;
import com.example.finalproject.model.Review;
import com.example.finalproject.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/fresh-products")
public class ReviewController {

    @Autowired
    IReviewService reviewService;

    @PostMapping("/review")
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewCreateDTO reviewCreateDTO) {
        Review review = ReviewCreateDTO.convertToReview(reviewCreateDTO);
        return new ResponseEntity<>(ReviewDTO.convertToResponse(reviewService.createReview(review)), HttpStatus.CREATED);
    }

    @PutMapping("/review/{reviewCode}")
    public ResponseEntity<ReviewDTO> updateReview(@Valid
                                                  @RequestBody ReviewUpdateDTO reviewUpdateDTO,
                                                  @PathVariable Long reviewCode) {
        Review review = ReviewUpdateDTO.convertToReview(reviewUpdateDTO);
        return new ResponseEntity<>(ReviewDTO.convertToResponse(reviewService.updateReview(review, reviewCode)), HttpStatus.CREATED);
    }

    @GetMapping("/review/list")
    public ResponseEntity<List<AdvertisementDTO>> findTopRatedAdvertisementsByCategory(@RequestParam String category,
                                                                                       @RequestParam(required = false) Integer limit) {
        return new ResponseEntity<>(AdvertisementDTO.convertListToResponse(reviewService.findTopRatedAdvertisementsByCategory(category, limit)), HttpStatus.OK);
    }

    @DeleteMapping("/review/{reviewCode}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewCode) {
        reviewService.deleteReview(reviewCode);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
