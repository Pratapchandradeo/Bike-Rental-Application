package com.bikerental.services.serviceImple;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bikerental.dto.BikeDTO;
import com.bikerental.exception.BikeUnavailableException;
import com.bikerental.model.Bike;
import com.bikerental.model.User;
import com.bikerental.repository.BikeRepo;
import com.bikerental.repository.UserRepo;
import com.bikerental.services.service.BikeService;

import jakarta.transaction.Transactional;

@Service
public class BikeServiceImpl  implements BikeService {

    @Autowired
    private BikeRepo bikeRepo;
    @Autowired
    private UserRepo userRepo;

    @Override
    public List<Bike> getAllBikes() {
        List<Bike> listOfBikes = this.bikeRepo.findAll();
        return listOfBikes;
    }

    @Override
    public Bike getBikeById(Long bikeId) throws BikeUnavailableException {
        Bike bike = this.bikeRepo.findById(bikeId).get();
        if (bike == null) {
            throw new BikeUnavailableException("No such bike in this id");
        }
        return bike;
    }

    @Override
    @Transactional
    public Bike createBike(BikeDTO bikeDTO) throws IOException {
        Bike bike = new Bike();
        mapDtoToBike(bikeDTO, bike);
        return bikeRepo.save(bike);
    }

    @Override
    @Transactional
    public Bike updateBike(Long bikeId, BikeDTO bikeDTO) throws IOException {
        Bike bike = bikeRepo.findById(bikeId)
            .orElseThrow(() -> new IllegalArgumentException("Bike with id " + bikeId + " not found."));
        mapDtoToBike(bikeDTO, bike);
        return bikeRepo.save(bike);
    }

  

    @Override
    public String deleteBike(Long bikeId) throws BikeUnavailableException {
        Bike bike = this.bikeRepo.findById(bikeId).get();
        if(bike == null){
            throw new BikeUnavailableException("There is no bike in this id");
        }
        this.bikeRepo.delete(bike);
        return "Bike Delete Sussesfully";
    }
    
    private void mapDtoToBike(BikeDTO bikeDTO, Bike bike) throws IOException {
        bike.setBikeName(bikeDTO.getBikeName());
        bike.setPricePerHour(bikeDTO.getPricePerHour());
        bike.setDescription(bikeDTO.getDescription());
        bike.setAvailable(bikeDTO.getAvailable());
        bike.setExtraChargePerKm(bikeDTO.getExtraChargePerKm());
        bike.setDistanceThreshold(bikeDTO.getDistanceThreshold());
        bike.setPickupPoint(bikeDTO.getPickupPoint());
        bike.setReceivePoint(bikeDTO.getReceivePoint());
        System.out.println(this.userRepo.findById(bikeDTO.getDealerId()).get());
        User user =this.userRepo.findById(bikeDTO.getDealerId()).get();
       
        bike.setDealer(user);
        if (bikeDTO.getImage() != null && !bikeDTO.getImage().isEmpty()) {
            bike.setImage(bikeDTO.getImage().getBytes());
        }
    }
}
