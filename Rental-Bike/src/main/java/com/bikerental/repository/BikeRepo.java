package com.bikerental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bikerental.model.Bike;

public interface BikeRepo extends JpaRepository<Bike,Long>{

    List<Bike> findAllByAvailable(boolean available);
}
