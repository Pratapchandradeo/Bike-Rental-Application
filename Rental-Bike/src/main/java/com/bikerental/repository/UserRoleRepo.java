package com.bikerental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bikerental.model.UserRole;

public interface UserRoleRepo extends JpaRepository<UserRole,Long>{
    
}
