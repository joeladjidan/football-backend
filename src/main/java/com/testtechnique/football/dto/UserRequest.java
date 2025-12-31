package com.testtechnique.football.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class UserRequest {
    @NotBlank
    public String username;

    @NotBlank
    public String password;

    // Accept a JSON array of roles (e.g. ["ROLE_ADMIN","ROLE_USER"]) or null
    public List<String> roles;

    public UserRequest() {}
    public UserRequest(String username, String password, List<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
