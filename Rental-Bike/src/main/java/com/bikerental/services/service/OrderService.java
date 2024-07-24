package com.bikerental.services.service;

import java.time.LocalDateTime;
import java.util.List;

import com.bikerental.dto.OrderDTO;
import com.bikerental.model.Orders;

public interface OrderService {
    Orders bookBike(Long userId, Long bikeId, Long vendorId, LocalDateTime startTime, LocalDateTime endTime,Integer distanceTraveled);
    Orders cancelOrder(Long orderId);
    List<Orders> getAllOrdersForUser(Long userId);
    List<Orders> getAllOrdersForVendor(Long vendorId);
    Orders updateOrder(OrderDTO updatedOrder);
    List<Orders> getAllOrders();
    Orders completeOrder(Long orderId);

}
