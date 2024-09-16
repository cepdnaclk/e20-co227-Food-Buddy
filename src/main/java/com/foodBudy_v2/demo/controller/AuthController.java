package com.foodBudy_v2.demo.controller;

import com.foodBudy_v2.demo.model.AppUser;
import com.foodBudy_v2.demo.security.payload.LoginRequest;
import com.foodBudy_v2.demo.security.payload.SignupRequest;
import com.foodBudy_v2.demo.security.payload.UserInfoResponse;
import com.foodBudy_v2.demo.security.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
    @Operation(summary = "Authenticate a existing user" , description = "Allows the user to log in with user name and password and returns a JWT token and user details" )
    @RequestBody(description = "User name and the password of the user" , required = true)
    public ResponseEntity<UserInfoResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        UserInfoResponse response = authenticationService.authenticateUser(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    @Operation(summary = "SignUp", description = "Create a new user")
    @RequestBody(description = "Data transfer object for Signing up", required = true)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        UserInfoResponse response = authenticationService.registerUser(signupRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    @Operation(summary = "Get User Information" , description = "Retrieve information about the user")
    public ResponseEntity<UserInfoResponse> getUserDetails(Authentication authentication){
        UserInfoResponse response = authenticationService.getUserDetails(authentication);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
