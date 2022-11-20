package com.example.finalproject.service.impl;

import com.example.finalproject.model.*;
import com.example.finalproject.model.Enum.Category;
import com.example.finalproject.model.Enum.OrderStatus;
import com.example.finalproject.repository.AdvertisementRepo;
import com.example.finalproject.repository.BuyerRepo;
import com.example.finalproject.repository.PurchaseItemRepo;
import com.example.finalproject.repository.ReviewRepo;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    ReviewService reviewService;
    @Mock
    ReviewRepo reviewRepo;
    @Mock
    private BuyerRepo buyerRepo;
    @Mock
    private AdvertisementRepo advertisementRepo;
    @Mock
    private PurchaseItemRepo purchaseItemRepo;

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
    }

    @Test
    void createReview_saveAndReturnReview_whenSuccess() {
    }

    @Test
    void updateReview() {
    }

    @Test
    void findTopRatedAdvertisementsByCategory() {
    }

    @Test
    void findAllReviewsByBuyer() {
    }

    @Test
    void deleteReview() {
    }
}