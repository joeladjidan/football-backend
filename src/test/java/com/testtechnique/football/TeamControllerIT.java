
package com.testtechnique.football;

import com.testtechnique.football.domain.Team;
import com.testtechnique.football.service.TeamServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamControllerIT {

    @Autowired
    private TeamServiceImpl teamService;

    @Test
    void createAndListTeam() {
        Team t = new Team();
        t.setName("OGC Nice");
        t.setAcronym("OGCN");
        t.setBudget(new BigDecimal("120000000"));
        Team saved = teamService.addTeam(t);
        assertNotNull(saved.getId());
        Page<Team> page = teamService.findAll(PageRequest.of(0, 10));
        assertTrue(page.getTotalElements() >= 1);
    }
}
