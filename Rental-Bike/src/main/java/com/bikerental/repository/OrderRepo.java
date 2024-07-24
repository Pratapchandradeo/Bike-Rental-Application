package com.bikerental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bikerental.model.Orders;

public interface OrderRepo extends JpaRepository<Orders,Long>{

    List<Orders> findAllByUserId(Long userId);

    List<Orders> findAllByVendorId(Long vendorId);
    
}
