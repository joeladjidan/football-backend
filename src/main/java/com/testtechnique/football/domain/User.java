package com.testtechnique.football.domain;

import jakarta.persistence.*;

/**
 * JPA entity representing an application user (username, password, roles).
 *
 * Maps to table `users`. Roles are stored as a CSV string (e.g. "ROLE_USER,ROLE_ADMIN").
 *
 * @author Joël ADJIDAN
 * @since 2025-12-11
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Authorities stored as CSV string, e.g. "ROLE_USER,ROLE_ADMIN".
     */
    @Column(nullable = false, length = 255)
    private String roles;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
}
