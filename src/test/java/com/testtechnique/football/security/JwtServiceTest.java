package com.testtechnique.football.security;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void generate_and_extract_username_ok() {
        Environment mockEnv = Mockito.mock(Environment.class);
        JwtService jwtService = new JwtService(mockEnv);
        // secret base64: base64("a-very-long-secret-key-32-bytes-minimum")
        ReflectionTestUtils.setField(jwtService, "secretBase64", "YS12ZXJ5LWxvbmctc2VjcmV0LWtleS0zMi1ieXRlcy1taW5pbXVt");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3600000L);

        User user = new User("alice", "{noop}pwd", List.of());
        String token = jwtService.generateToken(user);
        assertNotNull(token);
        assertEquals("alice", jwtService.extractUsername(token));
    }
}
