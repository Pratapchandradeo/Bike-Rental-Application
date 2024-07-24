package com.bikerental.controller;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikerental.dto.BikeDTO;
import com.bikerental.model.Bike;
import com.bikerental.services.serviceImple.BikeServiceImpl;

@RestController
@RequestMapping("/bike")
public class BikeController {

    @Autowired
    private BikeServiceImpl bikeServiceImpl;

     @PostMapping("/create")
    public ResponseEntity<?> createBike(@ModelAttribute BikeDTO bikeDTO) {
        try {
            Bike bike = bikeServiceImpl.createBike(bikeDTO);
            return ResponseEntity.ok(bike);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create bike: " + e.getMessage());
        }
    }

    @PutMapping("/update/{bikeId}")
    public ResponseEntity<?> updateBike(@PathVariable Long bikeId, @ModelAttribute BikeDTO bikeDTO) {
        try {
            Bike bike = bikeServiceImpl.updateBike(bikeId, bikeDTO);
            return ResponseEntity.ok(bike);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update bike: " + e.getMessage());
        }
    }
    
    @GetMapping("/")
    public ResponseEntity<?> getAllBike(){
        return new ResponseEntity<>(this.bikeServiceImpl.getAllBikes(), HttpStatus.OK);
    }
    @GetMapping("/{bikeId}")
    public ResponseEntity<?> getById(@PathVariable Long bikeId){
        Bike bike = this.bikeServiceImpl.getBikeById(bikeId);
        return new ResponseEntity<>(bike,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{bikeId}")
    public ResponseEntity<?> deleteBike(@PathVariable Long bikeId){
        String result = this.bikeServiceImpl.deleteBike(bikeId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    
}
