package com.bikerental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private Long bikeId;
    private Long vendorId;
    private String startTime;
    private String endTime;
    private Integer distanceTraveled;
}
