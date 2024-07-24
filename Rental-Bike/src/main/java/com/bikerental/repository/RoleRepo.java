package com.bikerental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bikerental.model.Role;

public interface RoleRepo extends JpaRepository<Role,Long>{
    
}
