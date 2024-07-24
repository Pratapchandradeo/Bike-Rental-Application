package com.bikerental.services.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.bikerental.exception.UserFoundException;
import com.bikerental.exception.UserNotFoundException;
import com.bikerental.model.User;

public interface UserService {

    User createUser(User user) throws UserFoundException;
    User getUserByUserName(String userName) throws UserNotFoundException;
    void deleteUser(Long userId) throws UserNotFoundException;
    User updateUser(User user) throws UserNotFoundException;
    User saveUserProfilePicture(Long userId, MultipartFile profilePicture) throws IOException;
    Optional<User> getUserById(Long userId);
   
}
