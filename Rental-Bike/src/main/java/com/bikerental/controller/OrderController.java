package com.bikerental.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bikerental.dto.OrderDTO;
import com.bikerental.model.Orders;
import com.bikerental.services.serviceImple.OrderServiceImpl;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @PostMapping("/book/")
    public ResponseEntity<?> bookBike(
            @RequestBody OrderDTO orderDTO) {
        
        LocalDateTime start = LocalDateTime.parse(orderDTO.getStartTime());
        LocalDateTime end = LocalDateTime.parse(orderDTO.getEndTime());
        System.out.println(orderDTO.toString());

        try {
            Orders order = orderService.bookBike(orderDTO.getUserId(), orderDTO.getBikeId(), orderDTO.getVendorId(), start, end,orderDTO.getDistanceTraveled());
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try {
            Orders canceledOrder = orderService.cancelOrder(orderId);
            return ResponseEntity.ok(canceledOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Orders>> getAllOrdersForUser(@PathVariable Long userId) {
        try {
            List<Orders> orders = orderService.getAllOrdersForUser(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Orders> updateOrder(@RequestBody OrderDTO orderDTO) {
        try {
            Orders order = orderService.updateOrder(orderDTO);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

     @PostMapping("/complete")
    public ResponseEntity<Orders> completeOrder(@RequestParam Long orderId) {
        Orders completedOrder = orderService.completeOrder(orderId);
        return ResponseEntity.ok(completedOrder);
    }
}