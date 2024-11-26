package com.foodBudy_v2.demo.security.services;

import com.foodBudy_v2.demo.security.payload.LoginRequest;
import com.foodBudy_v2.demo.security.payload.SignupRequest;
import com.foodBudy_v2.demo.security.payload.UserInfoResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


public interface AuthenticationService {
    UserInfoResponse authenticateUser(LoginRequest loginRequest);

    UserInfoResponse registerUser(SignupRequest signupRequest);

    UserInfoResponse getUserDetails(Authentication authentication);
}
