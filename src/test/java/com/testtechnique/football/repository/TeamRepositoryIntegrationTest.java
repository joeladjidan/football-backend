package com.testtechnique.football.repository;

import com.testtechnique.football.domain.Player;
import com.testtechnique.football.domain.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@Rollback
public class TeamRepositoryIntegrationTest {

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void save_and_load_team_with_players() {
        // Arrange
        Team t = new Team();
        t.setName("Integration Team");
        t.setAcronym("IT");
        t.setBudget(new BigDecimal("12345.67"));

        Player p1 = new Player();
        p1.setName("Player One");
        p1.setPosition("FW");
        p1.setTeam(t);

        Player p2 = new Player();
        p2.setName("Player Two");
        p2.setPosition("MF");
        p2.setTeam(t);

        t.setPlayers(List.of(p1, p2));

        // Act
        Team saved = teamRepository.save(t);
        teamRepository.flush();

        // Detach and reload (but within same transaction, use findById)
        Team found = teamRepository.findById(saved.getId()).orElse(null);

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Integration Team");
        assertThat(found.getAcronym()).isEqualTo("IT");
        assertThat(found.getBudget()).isEqualByComparingTo(new BigDecimal("12345.67"));
        assertThat(found.getPlayers()).isNotNull();
        assertThat(found.getPlayers()).hasSize(2);
        assertThat(found.getPlayers()).extracting("name").containsExactlyInAnyOrder("Player One", "Player Two");
        // verify bidirectional mapping
        found.getPlayers().forEach(pl -> assertThat(pl.getTeam().getId()).isEqualTo(found.getId()));
    }
}

