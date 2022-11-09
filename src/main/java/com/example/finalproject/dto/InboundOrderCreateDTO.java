package com.example.finalproject.dto;

import com.example.finalproject.model.Batch;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
public class InboundOrderCreateDTO {
    @NotNull
    @Positive
    private Long sectionCode;

    @NotNull
    private Long warehouseCode;


    @NotNull
    @Valid
    private List<Batch> batchStock;
}