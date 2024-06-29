package com.foodBudy_v2.demo.security;

import com.foodBudy_v2.demo.model.AppRole;
import com.foodBudy_v2.demo.model.AppUser;
import com.foodBudy_v2.demo.model.Category;
import com.foodBudy_v2.demo.model.Role;
import com.foodBudy_v2.demo.repository.CategoryRepository;
import com.foodBudy_v2.demo.repository.RoleRepository;
import com.foodBudy_v2.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class UserDataInitializer {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private CategoryRepository categoryRepository;
    @Autowired
    public UserDataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
    }

    @Bean
    public CommandLineRunner initUserData(){
        return args -> {
            //************************************************************
            // Create roles (if they do not exist)
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            // users are assigned a set of rules, therefore create sets

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(userRole, sellerRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);

            //******************************************************************
            // create users if not already present
            if (!userRepository.existsByUsername("user1")){
                AppUser user1 = new AppUser();
                user1.setUsername("user1");
                user1.setEmail("user1@gmail.com");
                user1.setPassword(passwordEncoder.encode("upassword1"));
                //user1.setRoles(userRoles);
                userRepository.save(user1);
            }
            if (!userRepository.existsByUsername("seller1")){
                AppUser seller1 = new AppUser();
                seller1.setUsername("seller1");
                seller1.setEmail("seller1@gmail.com");
                seller1.setPassword(passwordEncoder.encode("spassword1"));
                //seller1.setRoles(sellerRoles);
                userRepository.save(seller1);
            }
            if (!userRepository.existsByUsername("admin1")){
                AppUser admin1 = new AppUser();
                admin1.setUsername("admin1");
                admin1.setEmail("admin1@gmail.com");
                admin1.setPassword(passwordEncoder.encode("apassword1"));
                //admin1.setRoles(adminRoles);
                userRepository.save(admin1);
            }

            //************************************************
            // update roles for created users
            userRepository.findByUsername("user1").ifPresent(user ->{
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUsername("seller1").ifPresent(seller ->{
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            });

            userRepository.findByUsername("admin1").ifPresent(admin ->{
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });


            //***********************************************
            // create few categories
            Category category1 = new Category();
            category1.setCategoryName("category1");

            Category category2 = new Category();
            category2.setCategoryName("category2");

            Category category3 = new Category();
            category3.setCategoryName("category3");

            categoryRepository.save(category1);
            categoryRepository.save(category2);
            categoryRepository.save(category3);

        };
    }
}
