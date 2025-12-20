
package com.testtechnique.football.web;

import com.testtechnique.football.domain.Team;
import com.testtechnique.football.service.TeamServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TeamController.class)
@Import(TeamControllerWebMvcTest.SecurityTestConfig.class)
class TeamControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamServiceImpl teamService;

    @Test
    void whenAnonymous_then401() throws Exception {
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"AC Test\",\"acronym\":\"ACT\",\"budget\":1000}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void whenRoleUser_then403() throws Exception {
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"AC Test\",\"acronym\":\"ACT\",\"budget\":1000}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void whenRoleAdmin_then201() throws Exception {
        Team saved = new Team();
        saved.setId(1L);
        saved.setName("OGC Nice");
        saved.setAcronym("OGCN");
        saved.setBudget(new BigDecimal("120000000"));
        when(teamService.addTeam(any(Team.class))).thenReturn(saved);

        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"OGC Nice\",\"acronym\":\"OGCN\",\"budget\":120000000}"))
                .andExpect(status().isCreated());

        verify(teamService, times(1)).addTeam(any(Team.class));
    }

    @TestConfiguration
    @EnableMethodSecurity
    static class SecurityTestConfig {
        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .anyRequest().authenticated()
                );
            return http.build();
        }
    }
}
