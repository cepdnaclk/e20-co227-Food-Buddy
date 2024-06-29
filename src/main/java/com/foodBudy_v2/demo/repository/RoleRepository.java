package com.foodBudy_v2.demo.repository;

import com.foodBudy_v2.demo.model.AppRole;
import com.foodBudy_v2.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
