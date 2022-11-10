package com.example.finalproject.service;

import com.example.finalproject.dto.AdvertisementDTO;
import com.example.finalproject.model.Advertisement;

import java.util.List;

public interface IAdvertisementService {
    List<Advertisement> findAll();
    List<Advertisement>findAllByCategory(String category);

    List<Advertisement> findAllAdvertisementsByOrder(Long orderNumber);
}
