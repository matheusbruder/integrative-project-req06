package com.example.finalproject.repository;

import com.example.finalproject.model.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PurchaseItemRepo extends JpaRepository<PurchaseItem, Long> {

    @Query(value = "select pi.item_code, pi.price, pi.quantity, pi.advertisement_code, pi.purchase_code " +
                    "from purchase_item pi\n" +
                    "inner join purchase_order po\n" +
                    "on pi.purchase_code = po.purchase_code\n" +
                    "where po.order_status = 'FINALIZADO' " +
            "           and po.buyer_code = :buyerCode " +
            "           and pi.advertisement_code = :advertisementCode\n" +
                    "limit 1;", nativeQuery = true)
    Optional<PurchaseItem> findByBuyerCodeAndAdvertisementCodeWhenPurchaseOrderFinished(Long buyerCode, Long advertisementCode);
}
