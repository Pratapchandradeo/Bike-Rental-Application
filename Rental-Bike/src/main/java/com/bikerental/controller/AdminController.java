package com.bikerental.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bikerental.exception.UserFoundException;
import com.bikerental.exception.UserNotFoundException;
import com.bikerental.model.Orders;
import com.bikerental.model.Role;
import com.bikerental.model.User;
import com.bikerental.services.serviceImple.OrderServiceImpl;
import com.bikerental.services.serviceImple.UserServiceImpl;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody User user) throws UserFoundException {
        try {
            Role role = new Role();
            role.setRoleId(45L);
            role.setRoleName("Admin");
            // Wrap the role in a Set
            Set<Role> roles = new HashSet<>();
            roles.add(role);

            user.setProfilePicture(null);
            user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRoles(roles);
            User saveUser = userService.createUser(user);

            return new ResponseEntity<>(saveUser, HttpStatus.ACCEPTED);
        } catch (UserFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    
    @PostMapping("/{id}/uploadProfilePicture")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable("id") Long userId,
            @RequestParam("profilePicture") MultipartFile profilePicture) {
                try {
                    userService.saveUserProfilePicture(userId, profilePicture);
                    return new ResponseEntity<>("Profile picture uploaded successfully", HttpStatus.OK);
                } catch (AccessDeniedException e) {
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
                } catch (IOException e) {
                    return new ResponseEntity<>("Failed to upload profile picture", HttpStatus.INTERNAL_SERVER_ERROR);
                } catch (Exception e) {
                    return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
                }
    }

    @GetMapping("/{id}/profilePicture")
    public ResponseEntity<?> getProfilePicture(@PathVariable("id") Long userId) {
        try {
            Optional<User> userOptional = userService.getUserById(userId);
            if (userOptional.isPresent() && userService.hasPermission(userId)) {
                User user = userOptional.get();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "image/jpeg");
                return new ResponseEntity<>(user.getProfilePicture(), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No admin in this id ", HttpStatus.NOT_FOUND);
            }
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{userName}")
    public ResponseEntity<?> getUser(@PathVariable String userName) {
        try {
            User user = this.userService.getUserByUserName(userName);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            this.userService.deleteUser(userId);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            User updateUser = this.userService.updateUser(user);
            return new ResponseEntity<>(updateUser, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Orders>> getAllOrders() {
        try {
            List<Orders> orders = orderServiceImpl.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
