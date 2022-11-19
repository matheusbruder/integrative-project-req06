package com.example.finalproject.repository;

import com.example.finalproject.model.Advertisement;
import com.example.finalproject.model.Buyer;
import com.example.finalproject.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review, Long> {

    List<Review> findByAdvertisement(Advertisement advertisement);

    List<Review> findByBuyer(Buyer buyer);
}
