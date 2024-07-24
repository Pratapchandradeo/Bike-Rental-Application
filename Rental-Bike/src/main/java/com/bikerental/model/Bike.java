package com.bikerental.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dealer_id", nullable = false)
    private User dealer;

    @Column(nullable = false)
    private String bikeName;

    @Column(nullable = false)
    private Double pricePerHour;

    private String description;

    @Column(nullable = false)
    private Boolean available;

    @Column(nullable = false)
    private Double extraChargePerKm;

    @Column(nullable = false)
    private Integer distanceThreshold; // e.g., 200 km

    @Column(nullable = false)
    private String pickupPoint;

    @Column(nullable = false)
    private String receivePoint;

    @Column(columnDefinition = "LONGBLOB",nullable = true)
    private byte[] image;
}
