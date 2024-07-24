package com.bikerental.services.serviceImple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.bikerental.model.User;
import com.bikerental.repository.UserRepo;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Attempting to load user by username: " + username);
        User user = userRepo.findByUserName(username);
        System.out.println("User found: " + user);

        if (user == null) {
            System.out.println("User not found in UserDetailsServiceImpl");
            throw new UsernameNotFoundException("No user found !!");
        }

        System.out.println("Returning user details for user: " + user.getUsername());
        return user;
    }
}
