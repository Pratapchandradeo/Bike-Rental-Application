package com.bikerental.services.serviceImple;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bikerental.dto.OrderDTO;
import com.bikerental.exception.ResourceNotFoundException;
import com.bikerental.model.Bike;
import com.bikerental.model.Orders;
import com.bikerental.model.User;
import com.bikerental.repository.BikeRepo;
import com.bikerental.repository.OrderRepo;
import com.bikerental.repository.UserRepo;
import com.bikerental.services.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private BikeRepo bikeRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Override
    public Orders bookBike(Long userId, Long bikeId, Long vendorId, LocalDateTime startTime, LocalDateTime endTime,Integer distanceTraveled) {
        
       Optional<Bike> bike = bikeRepo.findById(bikeId);
    
        Optional<User> user = userRepo.findById(userId);
        
        Optional<User> vendor = userRepo.findById(vendorId);

        if (!bike.isPresent())
        {
            throw new RuntimeException("Booking failed: Check bike availability and user IDs.");
        }
        if ( !user.isPresent() ) {
            throw new RuntimeException("User with this user id not Available");
        }
        if (!vendor.isPresent()) {
            throw new RuntimeException("Vender with this id not Available");
        }
        if(!bike.get().getAvailable()){
            throw new RuntimeException("Bike with this id not Available for Now Alerdy Booked");
        }
        
        Orders order = new Orders();
        order.setUser(user.get());
        
        order.setBike(bike.get());
       
        order.setVendor(vendor.get());
        
       
        order.setStartTime(startTime);
       
        order.setEndTime(endTime);
        order.setDistanceTraveled(distanceTraveled);
        order.setStatus(Orders.Status.BOOKED);
        
        order.setPaymentStatus(Orders.PaymentStatus.PENDING);
        
        order.calculateTotalPrice();
       
        order.setTotalPrice(order.getTotalPrice() * 1.18); // Adding 18% GST
       

        bike.get().setAvailable(false); // Mark the bike as unavailable
        
        System.out.println(order.toString());
        return orderRepo.save(order);
    }

    @Override
    public Orders cancelOrder(Long orderId) {
       Optional<Orders> orderOpt = orderRepo.findById(orderId);
        if (!orderOpt.isPresent()) {
            throw new RuntimeException("Order not found.");
        }

        Orders order = orderOpt.get();
        LocalDateTime now = LocalDateTime.now();
        long hoursUntilStart = ChronoUnit.HOURS.between(now, order.getStartTime());

        if (order.getStatus() == Orders.Status.CANCELED) {
            throw new RuntimeException("Order already canceled.");
        }

        double totalWithoutGst = order.getTotalPrice() / 1.18; // Remove the GST from the total price
        double refundAmount;

        if (hoursUntilStart < 2) {
            // Less than 2 hours before start time: charge 25% of the total amount without GST
            double cancellationFee = totalWithoutGst * 0.25;
            refundAmount = totalWithoutGst - cancellationFee;
        } else {
            // More than 2 hours before start time: full refund without GST
            refundAmount = order.getTotalPrice();
        }

        // Update order status and set the refund amount as the new total price
        order.setStatus(Orders.Status.CANCELED);
        order.setTotalPrice(refundAmount);
        order.setPaymentStatus(Orders.PaymentStatus.REFUNDED);

        // Make the bike available again
        Bike bike = order.getBike();
        bike.setAvailable(true);
        bikeRepo.save(bike);

        return orderRepo.save(order);
    }

    public List<Orders> getAllOrdersForUser(Long userId) {
        List<Orders> order =  this.orderRepo.findAllByUserId(userId);
        if(order.isEmpty()){
            throw new ResourceNotFoundException("There is no orders");
        }
        return orderRepo.findAllByUserId(userId);
    }

    public List<Orders> getAllOrdersForVendor(Long vendorId) {
        List<Orders> order = this.orderRepo.findAllByVendorId(vendorId);
        if(order.isEmpty()){
            throw new ResourceNotFoundException("There is no orders");
        }
        return orderRepo.findAllByVendorId(vendorId);
    }

    public Orders updateOrder(OrderDTO updatedOrder) {
        Optional<Orders> order = this.orderRepo.findById(updatedOrder.getId());
        if(!order.isPresent()){
            throw new ResourceNotFoundException("There is no order in this details");
        }
        Orders existingOrder = order.get();

        // Update the existing order with the new details
        existingOrder.setStartTime(LocalDateTime.parse(updatedOrder.getStartTime()));
        existingOrder.setEndTime(LocalDateTime.parse(updatedOrder.getEndTime()));
        existingOrder.setDistanceTraveled(updatedOrder.getDistanceTraveled());

        // Assuming that user, bike, and vendor are not changed during update
        existingOrder.calculateTotalPrice();
        existingOrder.setTotalPrice(existingOrder.getTotalPrice() * 1.18); // Adding 18% GST

        return orderRepo.save(existingOrder);
    }

    @Override
    public List<Orders> getAllOrders() {
       return this.orderRepo.findAll();
    }

    @Override
    public Orders completeOrder(Long orderId) {
        Optional<Orders> orderOpt = orderRepo.findById(orderId);
        if (!orderOpt.isPresent()) {
            throw new RuntimeException("Order not found.");
        }

        Orders order = orderOpt.get();
        if (order.getStatus() == Orders.Status.COMPLETED) {
            throw new RuntimeException("Order is already completed.");
        }

        order.completeOrder();

        // Make the bike available again
        Bike bike = order.getBike();
        bike.setAvailable(true);
        bikeRepo.save(bike);

        // Notify vendor logic here (could be an email, SMS, etc.)
        notifyVendor(order.getVendor(), order);

        return orderRepo.save(order);
    }

    private void notifyVendor(User vendor, Orders order) {
        // Add your notification logic here (email, SMS, etc.)
        System.out.println("Notification sent to vendor: " + vendor.getEmail() + " for order: " + order.getId());
    }
    
}
