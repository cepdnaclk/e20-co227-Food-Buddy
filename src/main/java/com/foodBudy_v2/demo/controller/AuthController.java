package com.foodBudy_v2.demo.controller;

import com.foodBudy_v2.demo.model.AppUser;
import com.foodBudy_v2.demo.security.payload.LoginRequest;
import com.foodBudy_v2.demo.security.payload.SignupRequest;
import com.foodBudy_v2.demo.security.payload.UserInfoResponse;
import com.foodBudy_v2.demo.security.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        UserInfoResponse response = authenticationService.authenticateUser(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        UserInfoResponse response = authenticationService.registerUser(signupRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUserDetails(Authentication authentication){
        UserInfoResponse response = authenticationService.getUserDetails(authentication);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
