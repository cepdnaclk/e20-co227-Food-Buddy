package com.foodBudy_v2.demo.utils;

import com.foodBudy_v2.demo.model.AppUser;
import com.foodBudy_v2.demo.repository.UserRepository;
import com.foodBudy_v2.demo.security.payload.UserInfoResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public AuthUtil(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public String loggedInEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AppUser user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        return user.getEmail();
    }

    public Long loggedInUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AppUser user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        return user.getUserId();
    }

    public AppUser loggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AppUser user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        return user;
    }

    public UserInfoResponse loggedInUserResponse(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AppUser user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        return modelMapper.map(user, UserInfoResponse.class);
    }


}
