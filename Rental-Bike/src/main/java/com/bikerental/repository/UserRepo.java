package com.bikerental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bikerental.model.User;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUserName(String username);

    
} 
