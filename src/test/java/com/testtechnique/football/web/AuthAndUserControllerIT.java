package com.testtechnique.football.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testtechnique.football.dto.UserRequest;
import com.testtechnique.football.domain.User;
import java.util.List;
import com.testtechnique.football.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthAndUserControllerIT {

    @Autowired MockMvc mvc;
    @Autowired UserAccountRepository repo;
    @Autowired PasswordEncoder encoder;
    @Autowired ObjectMapper om;

    @BeforeEach
    void setup() { repo.deleteAll(); }

    @Test
    void register_and_login_and_update_me() throws Exception {
        // Register
        UserRequest reg = new UserRequest(); reg.username = "alice"; reg.password = "pwd123";
        mvc.perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(reg)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("alice"));

        // Login
        UserRequest login = new UserRequest(); login.username = "alice"; login.password = "pwd123";
        String resp = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(login)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String token = om.readTree(resp).get("token").asText();

        // Update me
        UserRequest update = new UserRequest(); update.username = "alice2"; update.password = "pwd456";
        mvc.perform(put("/api/users/me").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(update)).header("Authorization", "Bearer "+token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice2"));
    }

    @Test
    void admin_only_create_and_delete() throws Exception {
        // create admin directly in repo
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(encoder.encode("adminpwd"));
        admin.setRoles("ROLE_ADMIN");
        repo.save(admin);

        // login admin
        UserRequest login = new UserRequest(); login.username = "admin"; login.password = "adminpwd";
        String resp = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(login)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String token = om.readTree(resp).get("token").asText();

        // admin create user
        UserRequest newUser = new UserRequest(); newUser.username = "bob"; newUser.password = "bobpwd"; newUser.roles = List.of("ROLE_USER");
        String created = mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(newUser)).header("Authorization", "Bearer "+token))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long id = om.readTree(created).get("id").asLong();

        // admin delete user
        mvc.perform(delete("/api/users/"+id).header("Authorization", "Bearer "+token)).andExpect(status().isNoContent());
    }
}

