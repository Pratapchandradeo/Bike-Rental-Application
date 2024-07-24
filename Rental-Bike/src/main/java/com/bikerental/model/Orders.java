package com.bikerental.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "bike_id", nullable = false)
    private Bike bike;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private User vendor;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private Integer distanceTraveled;

    @Column(nullable = false)
    private Double extraCharges;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public enum Status {
        BOOKED, CANCELED, COMPLETED
    }

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }

    public void calculateTotalPrice() {
        long hours = java.time.Duration.between(startTime, endTime).toHours();

        this.totalPrice = hours * bike.getPricePerHour();

        if (distanceTraveled > bike.getDistanceThreshold()) {
            int extraDistance = distanceTraveled - bike.getDistanceThreshold();

            this.extraCharges = extraDistance * bike.getExtraChargePerKm();

            this.totalPrice += this.extraCharges;
        } else {
            this.extraCharges = 0.0;
        }

    }

    public void completeOrder() {
        this.status = Status.COMPLETED;
        this.paymentStatus = PaymentStatus.COMPLETED;
    }

}
