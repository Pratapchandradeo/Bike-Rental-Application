package com.bikerental.services.service;

import java.io.IOException;
import java.util.List;

import com.bikerental.dto.BikeDTO;
import com.bikerental.exception.BikeUnavailableException;
import com.bikerental.model.Bike;

public interface BikeService {
    List<Bike> getAllBikes();
    Bike getBikeById(Long bikeId) throws BikeUnavailableException;
    Bike createBike(BikeDTO bikeDTO) throws IOException;
    Bike updateBike(Long bikeId, BikeDTO bikeDTO) throws IOException ;
    String deleteBike(Long bikeId) throws BikeUnavailableException;
}
