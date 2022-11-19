package com.example.finalproject.controller;

import com.example.finalproject.dto.ReviewCreateDTO;
import com.example.finalproject.dto.ReviewDTO;
import com.example.finalproject.model.Review;
import com.example.finalproject.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}