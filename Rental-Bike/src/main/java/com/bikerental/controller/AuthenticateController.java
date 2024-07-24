package com.bikerental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bikerental.config.JwtUtils;
import com.bikerental.security.JwtRequest;
import com.bikerental.security.JwtResponse;
import com.bikerental.services.serviceImple.UserDetailsServiceImpl;

@RestController
@CrossOrigin("*")
public class AuthenticateController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl detailsServiceImpl;

    @Autowired
    private JwtUtils jwtUtils;


    @PostMapping("/authentication")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticate(jwtRequest.getUserName(), jwtRequest.getPassword());
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            throw new Exception("User not found !!!");
        }

        UserDetails details = this.detailsServiceImpl.loadUserByUsername(jwtRequest.getUserName());
        String token  = this.jwtUtils.generateToken(details);
        System.out.println(token);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String userName, String password) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (DisabledException e) {
            throw new Exception("USER DESABLED"+e.getMessage());
        }catch(BadCredentialsException e){
            throw new Exception("Invalid Credentials"+e.getMessage());
        }
    }
}
