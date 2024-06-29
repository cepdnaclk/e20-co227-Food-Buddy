package com.foodBudy_v2.demo.security.services;

import com.foodBudy_v2.demo.exception.APIException;
import com.foodBudy_v2.demo.model.AppRole;
import com.foodBudy_v2.demo.model.AppUser;
import com.foodBudy_v2.demo.model.Role;
import com.foodBudy_v2.demo.repository.RoleRepository;
import com.foodBudy_v2.demo.repository.UserRepository;
import com.foodBudy_v2.demo.security.jwt.JwtUtils;
import com.foodBudy_v2.demo.security.payload.LoginRequest;
import com.foodBudy_v2.demo.security.payload.SignupRequest;
import com.foodBudy_v2.demo.security.payload.UserInfoResponse;
import com.foodBudy_v2.demo.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    private ModelMapper modelMapper;

    private AuthUtil authUtil;

    @Autowired
    public AuthenticationServiceImpl(JwtUtils jwtUtils, AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, ModelMapper modelMapper, AuthUtil authUtil) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.authUtil = authUtil;
    }




    @Override
    public UserInfoResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            throw new APIException("Bad credentials");
        }

        // once the user is authenticated with username and password,
        // the authentication object is put into security context for future references
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get the user details from the authentication object
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(), jwtToken, userDetails.getUsername(), roles);
        return response;

    }

    @Override
    public UserInfoResponse registerUser(SignupRequest signupRequest) {
        // check the availability of the username and email
        if (userRepository.existsByUsername(signupRequest.getUsername())){
            throw new APIException("username is already taken!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())){
            throw new  APIException("Email is already in use!");
        }

        // create a new user account
        AppUser user = new AppUser();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));


        // assigning the roles*******************************************

        // Actual roles of Role class
        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseThrow(() -> new APIException("Role is not found"));

//        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
//                .orElseThrow(() -> new APIException("Role is not found"));
//
//        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
//                .orElseThrow(() -> new APIException("Role is not found"));

        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return modelMapper.map(user, UserInfoResponse.class);

    }

    @Override
    public UserInfoResponse getUserDetails(Authentication authentication) {
        return authUtil.loggedInUserResponse();
    }
}
