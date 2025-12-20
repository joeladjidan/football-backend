package com.testtechnique.football.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRequest {
    @NotBlank
    public String username;

    @NotBlank
    public String password;

    public String roles; // CSV: ROLE_USER,ROLE_ADMIN

    public UserRequest() {}
    public UserRequest(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
