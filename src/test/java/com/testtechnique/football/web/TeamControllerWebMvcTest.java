// java
package com.testtechnique.football.web;

import com.testtechnique.football.config.SecurityTestConfig;
import com.testtechnique.football.domain.Team;
import com.testtechnique.football.mapper.TeamMapper;
import com.testtechnique.football.security.JwtService;
import com.testtechnique.football.service.TeamServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TeamController.class)
@Import(SecurityTestConfig.class)
class TeamControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamServiceImpl teamService;

    @MockBean
    private TeamMapper teamMapper;

    @MockBean
    private JwtService jwtService;

    @Test
    void whenAnonymous_then401() throws Exception {
        String payload = "{ \"name\": \"FC Test\" }";
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void whenRoleUser_then403() throws Exception {
        String payload = "{ \"name\": \"FC Test\" }";
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void whenRoleAdmin_then201() throws Exception {
        when(teamService.addTeam(any())).thenReturn(mock(Team.class));

        String payload = "{ \"name\": \"FC Test\" }";
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());
    }


}
