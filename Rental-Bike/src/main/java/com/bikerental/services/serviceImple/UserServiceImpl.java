package com.bikerental.services.serviceImple;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bikerental.exception.UserFoundException;
import com.bikerental.exception.UserNotFoundException;
import com.bikerental.model.Role;
import com.bikerental.model.User;
import com.bikerental.repository.RoleRepo;
import com.bikerental.repository.UserRepo;
import com.bikerental.services.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public User createUser(User user) throws UserFoundException {
        User local = this.userRepo.findByUserName(user.getUsername());

        if (local != null) {
            System.out.println("User is already there!!");
            throw new UserFoundException("User with this user name already there! Try with another one. ");
        } else {
            // Ensure roles are managed entities
            Set<Role> managedRoles = new HashSet<>();
            for (Role role : user.getRoles()) {
                Role managedRole = roleRepo.findById(role.getRoleId())
                        .orElseThrow(() -> new RuntimeException("Role not found"));
                managedRoles.add(managedRole);
            }
            user.setRoles(managedRoles);
        }
        try {
            local = this.userRepo.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error saving user");
        }
        return local;
    }

    @Override
    public User getUserByUserName(String userName) throws UserNotFoundException, AccessDeniedException {
        User data = this.userRepo.findByUserName(userName);
        if (data == null) {
            throw new UserNotFoundException("There is no user with this username!");
        }
        if (hasPermission(data.getId())) {
            return data;
        }
        throw new AccessDeniedException("You do not have permission to access this user's details");
    }

    @Override
    public void deleteUser(Long userId) throws UserNotFoundException, AccessDeniedException {
        if (!hasPermission(userId)) {
            throw new AccessDeniedException("You do not have permission to delete this user");
        }
        Optional<User> userOptional = this.userRepo.findById(userId);
        if (userOptional.isPresent()) {
            this.userRepo.deleteById(userId);
        } else {
            throw new UserNotFoundException("No user found to delete");
        }
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException, AccessDeniedException {
        if (!hasPermission(user.getId())) {
            throw new AccessDeniedException("You do not have permission to update this user");
        }
        Optional<User> data = this.userRepo.findById(user.getId());
        if (data.isPresent()) {
            return userRepo.save(user);
        } else {
            throw new UserNotFoundException("No user found to update");
        }
    }

    @Override
    public User saveUserProfilePicture(Long userId, MultipartFile profilePicture) throws IOException, AccessDeniedException {
       
        if (!hasPermission(userId)) {
           
            throw new AccessDeniedException("You do not have permission to update this user's profile picture");
        }
       
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setProfilePicture(profilePicture.getBytes());
            return userRepo.save(user);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public Optional<User> getUserById(Long userId) throws AccessDeniedException {
       
        if (!hasPermission(userId)) {
            System.out.println(userId+"###########");
            throw new AccessDeniedException("You do not have permission to access this user's details");
        }
        return userRepo.findById(userId);
    }

    // Method to get the current authenticated user
    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userRepo.findByUserName(userDetails.getUsername());
        }
        throw new RuntimeException("User not authenticated");
    }

    // Method to check if the current user has permission
    public boolean hasPermission(Long userId) {
        User currentUser = getCurrentAuthenticatedUser();
        // Allow access if the current user is the target user or if the current user has the 'Admin' role
        return currentUser.getId().equals(userId) || currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equals("Admin"));
    }
}
