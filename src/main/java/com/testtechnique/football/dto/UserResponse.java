package com.testtechnique.football.dto;

public class UserResponse {
    public Long id;
    public String username;
    public String roles;

    public UserResponse() {}
    public UserResponse(Long id, String username, String roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}

