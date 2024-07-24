package com.bikerental.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BikeDTO {
    private Long id; // Only used for updates
    private String bikeName;
    private Double pricePerHour;
    private String description;
    private Boolean available;
    private Double extraChargePerKm;
    private Integer distanceThreshold;
    private String pickupPoint;
    private String receivePoint;
    private MultipartFile image;
    private Long dealerId;
}
