package com.example.finalproject.service.impl;

import com.example.finalproject.exception.InvalidArgumentException;
import com.example.finalproject.exception.NotFoundException;
import com.example.finalproject.model.*;
import com.example.finalproject.model.Enum.Category;
import com.example.finalproject.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    ReviewService reviewService;
    @Mock
    ReviewRepo reviewRepo;
    @Mock
    BuyerRepo buyerRepo;
    @Mock
    AdvertisementRepo advertisementRepo;
    @Mock
    PurchaseItemRepo purchaseItemRepo;
    @Mock
    SectionRepo sectionRepo;

    private PurchaseItem purchaseItem;
    private Advertisement advertisement;
    private List<Advertisement> advertisementList;
    private Buyer buyer;
    private Section section;
    private Review review;
    private List<Review> reviewList;

    @BeforeEach
    void setUp() {
        buyer = Buyer.builder()
                .buyerCode(1L)
                .name("Neymar")
                .purchases(new ArrayList<>())
                .build();

        section = Section.builder()
                .sectionCode(1L)
                .category(Category.CONGELADO)
                .volume(100.0F)
                .accumulatedVolume(15.0F)
                .minTemperature(-22F)
                .maxTemperature(-18F)
                .build();

        advertisement = Advertisement.builder()
                .advertisementCode(2L)
                .name("Pizza")
                .price(BigDecimal.valueOf(4.0))
                .seller(Seller.builder().build())
                .build();
        advertisementList = new ArrayList<>();
        advertisementList.add(advertisement);

        purchaseItem = PurchaseItem.builder()
                .itemCode(1L)
                .price(BigDecimal.valueOf(22.0))
                .quantity(100)
                .advertisement(advertisement)
                .build();

        review = Review.builder()
                .reviewCode(1L)
                .dateTime(LocalDateTime.now())
                .rating(4)
                .comment("My comment")
                .buyer(buyer)
                .advertisement(advertisement)
                .build();
        reviewList = new ArrayList<>();
        reviewList.add(review);
    }

    @Test
    void createReview_saveAndReturnReview_whenSuccess() {
        // Arrange
        when(buyerRepo.findById(anyLong())).thenReturn(Optional.ofNullable(buyer));
        when(advertisementRepo.findById(anyLong())).thenReturn(Optional.ofNullable(advertisement));
        when(purchaseItemRepo.findByBuyerCodeAndAdvertisementCodeWhenPurchaseOrderFinished(buyer.getBuyerCode(), advertisement.getAdvertisementCode())).thenReturn(Optional.ofNullable(purchaseItem));
        // Act
        reviewService.createReview(review);
        // Assert
        verify(reviewRepo, times(1)).save(review);
    }

    @Test
    void createReview_returnInvalidArgumentException_whenBuyerHasNotBought() {
        // Arrange
        when(buyerRepo.findById(anyLong())).thenReturn(Optional.ofNullable(buyer));
        when(advertisementRepo.findById(anyLong())).thenReturn(Optional.ofNullable(advertisement));
        // Act ~ Assert
        assertThrows(InvalidArgumentException.class, () -> reviewService.createReview(review));
    }

    @Test
    void createReview_returnInvalidArgumentException_whenRepeatedReview() {
        // Arrange
        when(buyerRepo.findById(anyLong())).thenReturn(Optional.ofNullable(buyer));
        when(advertisementRepo.findById(anyLong())).thenReturn(Optional.ofNullable(advertisement));
        when(reviewRepo.findAll()).thenReturn(reviewList);
        // Act ~ Assert
        assertThrows(InvalidArgumentException.class, () -> reviewService.createReview(review));
    }

    @Test
    void updateReview_returnReview_whenSuccess() {
        // Arrange
        when(reviewRepo.findById(anyLong())).thenReturn(Optional.ofNullable(review));
        // Act
        reviewService.updateReview(review, review.getReviewCode());
        // Assert
        verify(reviewRepo, times(1)).save(review);
    }

    @Test
    void updateReview_returnNotFoundException_whenReviewDoesNotExist() {
        // Arrange
        when(reviewRepo.findById(anyLong())).thenReturn(Optional.empty());
        // Act ~ Assert
        assertThrows(NotFoundException.class, () -> reviewService.updateReview(review, review.getReviewCode()));
    }

    @Test
    void findTopRatedAdvertisementsByCategory_returnAdvertisementList_whenSuccess() {
        // Arrange
        when(advertisementRepo.findAllByCategory(anyString())).thenReturn(advertisementList);
        when(sectionRepo.findByCategory(anyString())).thenReturn(Optional.ofNullable(section));
        // Act
        List<Advertisement> advertisementListFiltered = reviewService.findTopRatedAdvertisementsByCategory("congelado", 1);
        // Assert
        Assertions.assertNotNull(advertisementListFiltered);
    }

    @Test
    void findTopRatedAdvertisementsByCategory_returnNotFoundException_whenCategoryDoesNotExist() {
        // Arrange
        when(sectionRepo.findByCategory(anyString())).thenReturn(Optional.empty());
        // Act ~ Assert
        assertThrows(NotFoundException.class, () -> reviewService.findTopRatedAdvertisementsByCategory("congelado", 1));
    }

    @Test
    void findTopRatedAdvertisementsByCategory_returnNotFoundException_whenAdvertisementListIsEmpty() {
        // Arrange
        when(advertisementRepo.findAllByCategory(anyString())).thenReturn(new ArrayList<>());
        when(sectionRepo.findByCategory(anyString())).thenReturn(Optional.ofNullable(section));
        // Act ~ Assert
        assertThrows(NotFoundException.class, () -> reviewService.findTopRatedAdvertisementsByCategory("congelado", 1));
    }

    @Test
    void findAllReviewsByBuyer_returnReviewListByBuyer_whenSuccess() {
        // Arrange
        when(buyerRepo.findById(anyLong())).thenReturn(Optional.ofNullable(buyer));
        when(reviewRepo.findByBuyer(any())).thenReturn(reviewList);
        // Act
        List<Review> reviewList = reviewService.findAllReviewsByBuyer(buyer.getBuyerCode());
        // Assert
        Assertions.assertNotNull(reviewList);
    }

    @Test
    void findAllReviewsByBuyer_returnNotFoundException_whenBuyerDoesNotExist() {
        // Arrange
        when(buyerRepo.findById(anyLong())).thenReturn(Optional.empty());
        // Act ~ Assert
        assertThrows(NotFoundException.class, () -> reviewService.findAllReviewsByBuyer(buyer.getBuyerCode()));
    }

    @Test
    void findAllReviewsByBuyer_returnNotFoundException_whenBuyerHasNoReviews() {
        // Arrange
        when(buyerRepo.findById(anyLong())).thenReturn(Optional.ofNullable(buyer));
        when(reviewRepo.findByBuyer(any())).thenReturn(new ArrayList<>());
        // Act ~ Assert
        assertThrows(NotFoundException.class, () -> reviewService.findAllReviewsByBuyer(buyer.getBuyerCode()));
    }

    @Test
    void deleteReview_whenSuccess() {
        // Arrange
        when(reviewRepo.findById(anyLong())).thenReturn(Optional.ofNullable(review));
        // Act
        reviewService.deleteReview(review.getReviewCode());
        // Assert
        verify(reviewRepo, times(1)).delete(review);
    }

    @Test
    void deleteReview_returnNotFoundException_whenReviewCodeDoesNotExist() {
        // Arrange
        when(reviewRepo.findById(anyLong())).thenReturn(Optional.empty());
        // Act ~ Assert
        assertThrows(NotFoundException.class, () -> reviewService.deleteReview(review.getReviewCode()));
    }
}