package com.testtechnique.football.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testtechnique.football.dto.UserRequest;
import com.testtechnique.football.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserAccountRepository repo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ObjectMapper om;

    @BeforeEach
    void clean() { repo.deleteAll(); }

    @Test
    void register_public_creates_user() throws Exception {
        UserRequest req = new UserRequest();
        req.username = "jules";
        req.password = "pwd123";

        mvc.perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }
}

