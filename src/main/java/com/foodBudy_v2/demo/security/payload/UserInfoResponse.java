package com.foodBudy_v2.demo.security.payload;

import com.foodBudy_v2.demo.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String jwtToken;

    private String username;
    private List<String> roles;



    public UserInfoResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles =roles.stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toList());
    }
}