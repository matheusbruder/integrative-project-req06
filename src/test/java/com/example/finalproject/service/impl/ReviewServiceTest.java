package com.example.finalproject.service.impl;

import com.example.finalproject.exception.InvalidArgumentException;
import com.example.finalproject.exception.NotFoundException;
import com.example.finalproject.model.*;
import com.example.finalproject.model.Enum.Category;
import com.example.finalproject.model.Enum.OrderStatus;
import com.example.finalproject.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    private PurchaseOrder purchaseOrder;
    private PurchaseItem purchaseItem;
    private Advertisement advertisement;
    private List<Advertisement> advertisementList;
    private List<PurchaseItem> purchaseItemList;
    private Buyer buyer;
    private Batch batch;
    private Batch batchII;
    private InboundOrder inboundOrder;
    private Section section;
    private Warehouse warehouse;
    private List<Batch> batchList;
    private Review review;
    private List<Review> reviewList;

    @BeforeEach
    void setUp() {

        buyer = Buyer.builder()
                .buyerCode(1L)
                .name("Neymar")
                .purchases(new ArrayList<>())
                .build();

        warehouse = Warehouse.builder()
                .warehouseCode(1L)
                .sections(new ArrayList<>())
                .volume(10.0F)
                .build();

        section = Section.builder()
                .sectionCode(1L)
                .warehouse(warehouse)
                .category(Category.CONGELADO)
                .volume(100.0F)
                .accumulatedVolume(15.0F)
                .minTemperature(-22F)
                .maxTemperature(-18F)
                .build();

        batchList = new ArrayList<>();

        inboundOrder = InboundOrder.builder()
                .orderCode(3L)
                .orderDate(LocalDate.of(2022, 11, 14))
                .section(section)
                .batchStock(batchList)
                .build();

        batch = Batch.builder()
                .batchCode(3L)
                .inboundOrder(inboundOrder)
                .advertisement(advertisement)
                .currentTemperature(-20.0F)
                .productQuantity(100)
                .manufacturingDateTime(LocalDateTime.of(2019, 1, 20, 22, 34))
                .volume(12.0F)
                .dueDate(LocalDate.of(2023, 1, 5))
                .price(BigDecimal.valueOf(45.0D))
                .build();

        batchII = Batch.builder()
                .batchCode(3L)
                .inboundOrder(inboundOrder)
                .advertisement(advertisement)
                .currentTemperature(-20.0F)
                .productQuantity(20)
                .manufacturingDateTime(LocalDateTime.of(2019, 1, 20, 22, 34))
                .volume(12.0F)
                .dueDate(LocalDate.of(2023, 1, 5))
                .price(BigDecimal.valueOf(45.0D))
                .build();

        batchList.add(batch);
        batchList.add(batchII);

        advertisement = Advertisement.builder()
                .advertisementCode(2L)
                .name("Pizza")
                .price(BigDecimal.valueOf(4.0))
                .batches(batchList)
                .seller(Seller.builder().build())
                .build();

        purchaseItem = PurchaseItem.builder()
                .itemCode(1L)
                .price(BigDecimal.valueOf(22.0))
                .quantity(100)
                .purchaseOrder(purchaseOrder)
                .advertisement(advertisement)
                .build();

        advertisementList = new ArrayList<>();
        advertisementList.add(advertisement);
        purchaseItemList = new ArrayList<>();
        purchaseItemList.add(purchaseItem);

        purchaseOrder = PurchaseOrder.builder()
                .purchaseCode(1L)
                .dateTime(LocalDateTime.of(2019, 1, 20, 22, 34))
                .orderStatus(OrderStatus.ABERTO)
                .buyer(buyer)
                .purchaseItems(purchaseItemList)
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
        when(buyerRepo.findById(anyLong())).thenReturn(Optional.ofNullable(buyer));
        when(advertisementRepo.findById(anyLong())).thenReturn(Optional.ofNullable(advertisement));
        when(purchaseItemRepo.findByBuyerCodeAndAdvertisementCodeWhenPurchaseOrderFinished(buyer.getBuyerCode(), advertisement.getAdvertisementCode())).thenReturn(Optional.ofNullable(purchaseItem));

        reviewService.createReview(review);

        verify(reviewRepo, times(1)).save(review);
    }

    @Test
    void createReview_returnInvalidArgumentException_whenBuyerHasNotBought() {
        when(buyerRepo.findById(anyLong())).thenReturn(Optional.ofNullable(buyer));
        when(advertisementRepo.findById(anyLong())).thenReturn(Optional.ofNullable(advertisement));

        assertThrows(InvalidArgumentException.class, () -> reviewService.createReview(review));
    }

    @Test
    void createReview_returnInvalidArgumentException_whenRepeatedReview() {
        when(buyerRepo.findById(anyLong())).thenReturn(Optional.ofNullable(buyer));
        when(advertisementRepo.findById(anyLong())).thenReturn(Optional.ofNullable(advertisement));
        when(reviewRepo.findAll()).thenReturn(reviewList);

        assertThrows(InvalidArgumentException.class, () -> reviewService.createReview(review));
    }

    @Test
    void updateReview_returnReview_whenSuccess() {
        when(reviewRepo.findById(anyLong())).thenReturn(Optional.ofNullable(review));

        reviewService.updateReview(review, review.getReviewCode());

        verify(reviewRepo, times(1)).save(review);
    }

    @Test
    void updateReview_returnNotFoundException_whenReviewDoesNotExist() {
        when(reviewRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.updateReview(review, review.getReviewCode()));
    }

    @Test
    void findTopRatedAdvertisementsByCategory_returnAdvertisementList_whenSuccess() {
        when(advertisementRepo.findAllByCategory(anyString())).thenReturn(advertisementList);
        when(sectionRepo.findByCategory(anyString())).thenReturn(Optional.ofNullable(section));

        List<Advertisement> advertisementListFiltered = reviewService.findTopRatedAdvertisementsByCategory("congelado", 1);

        Assertions.assertNotNull(advertisementListFiltered);
    }

    @Test
    void findTopRatedAdvertisementsByCategory_returnNotFoundException_whenCategoryDoesNotExist() {
        when(sectionRepo.findByCategory(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.findTopRatedAdvertisementsByCategory("congelado", 1));
    }

    @Test
    void findTopRatedAdvertisementsByCategory_returnNotFoundException_whenAdvertisementListIsEmpty() {
        when(advertisementRepo.findAllByCategory(anyString())).thenReturn(new ArrayList<>());
        when(sectionRepo.findByCategory(anyString())).thenReturn(Optional.ofNullable(section));


        assertThrows(NotFoundException.class, () -> reviewService.findTopRatedAdvertisementsByCategory("congelado", 1));
    }

    @Test
    void findAllReviewsByBuyer_returnReviewListByBuyer_whenSuccess() {
        when(buyerRepo.findById(anyLong())).thenReturn(Optional.ofNullable(buyer));
        when(reviewRepo.findByBuyer(any())).thenReturn(reviewList);

        List<Review> reviewList = reviewService.findAllReviewsByBuyer(buyer.getBuyerCode());

        Assertions.assertNotNull(reviewList);
    }

    @Test
    void findAllReviewsByBuyer_returnNotFoundException_whenBuyerDoesNotExist() {
        when(buyerRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.findAllReviewsByBuyer(buyer.getBuyerCode()));
    }

    @Test
    void findAllReviewsByBuyer_returnNotFoundException_whenBuyerHasNoReviews() {
        when(buyerRepo.findById(anyLong())).thenReturn(Optional.ofNullable(buyer));
        when(reviewRepo.findByBuyer(any())).thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> reviewService.findAllReviewsByBuyer(buyer.getBuyerCode()));
    }

    @Test
    void deleteReview_whenSuccess() {
        when(reviewRepo.findById(anyLong())).thenReturn(Optional.ofNullable(review));

        reviewService.deleteReview(review.getReviewCode());

        verify(reviewRepo, times(1)).delete(review);
    }

    @Test
    void deleteReview_returnNotFoundException_whenReviewCodeDoesNotExist() {
        when(reviewRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.deleteReview(review.getReviewCode()));
    }
}