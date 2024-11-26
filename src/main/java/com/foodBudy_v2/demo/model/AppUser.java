package com.foodBudy_v2.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Schema(description = "User entity")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user" , example = "42")
    private Long userId;

    @Column(unique = true, nullable = false, length = 30)
    @Schema(description = "Unique user name of the User" , example = "User123")
    private String username;

    @Column(nullable = false)
    @Schema(description = "Password set by the user" , example = "password1223!@")
    private String password;

    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "Email address of the User" , example = "haritha1234@gmal.com")
    private String email;


    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_Id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))

    @Schema(description = "Roles assigned to the User")
    private Set<Role> roles = new HashSet<>();


    @JsonIgnore
    @OneToOne(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @Schema(description = "The shop owned by the user, ignored in the JSON response")
    private Shop shop;

}
