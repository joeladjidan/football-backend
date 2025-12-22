package com.testtechnique.football.web;

import com.testtechnique.football.config.SecurityTestConfig;
import com.testtechnique.football.domain.Team;
import com.testtechnique.football.dto.TeamResponse;
import com.testtechnique.football.mapper.TeamMapper;
import com.testtechnique.football.security.JwtService;
import com.testtechnique.football.service.TeamServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
@Import(SecurityTestConfig.class)
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamServiceImpl teamService;

    @MockBean
    private TeamMapper teamMapper;

    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    public void list_shouldReturnPagedTeams() throws Exception {
        Team team = new Team();
        team.setId(1L);
        team.setName("OGC Nice");
        team.setAcronym("OGC");
        team.setBudget(new BigDecimal("5000000.00"));

        PageImpl<Team> page = new PageImpl<>(List.of(team), PageRequest.of(0, 10), 1);

        Mockito.when(teamService.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        // Stub du mapper : retourne un TeamResponse non-null pour chaque Team
        TeamResponse response = new TeamResponse();
        response.setId(1L);
        response.setName("OGC Nice");
        response.setAcronym("OGC");
        response.setBudget(new BigDecimal("5000000.00"));

        Mockito.when(teamMapper.toResponse(Mockito.any(Team.class))).thenReturn(response);

        mockMvc.perform(get("/api/teams")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("OGC Nice"))
                .andExpect(jsonPath("$.content[0].acronym").value("OGC"))
                .andExpect(jsonPath("$.totalPages").value(1));
    }
}
