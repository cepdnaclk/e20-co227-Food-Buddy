package com.foodBudy_v2.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Roles available in the application")
public enum AppRole {
    ROLE_USER,
    ROLE_SELLER,
    ROLE_ADMIN
}
