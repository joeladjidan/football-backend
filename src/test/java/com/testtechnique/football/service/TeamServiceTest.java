package com.testtechnique.football.service;

import com.testtechnique.football.domain.Player;
import com.testtechnique.football.domain.Team;
import com.testtechnique.football.repository.ITeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    ITeamRepository teamRepository;

    @InjectMocks
    TeamServiceImpl teamService;

    Team team;

    @BeforeEach
    void setUp() {
        team = new Team();
        team.setName("AC Test");
        team.setAcronym("ACT");
        team.setBudget(new BigDecimal("1000"));
    }

    @Test
    void addTeam_ok_saves_with_players() {
        Player p = new Player();
        p.setName("Jean");
        p.setPosition("FW");
        team.setPlayers(List.of(p));
        when(teamRepository.existsByName("AC Test")).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenAnswer(inv -> inv.getArgument(0));

        Team saved = teamService.addTeam(team);
        assertEquals("AC Test", saved.getName());
        assertEquals(1, saved.getPlayers().size());
        assertNotNull(saved.getPlayers().get(0).getTeam());
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    void findAll_returns_list() {
        Team t1 = new Team();
        t1.setName("Team1");
        t1.setAcronym("T1");
        t1.setBudget(new BigDecimal("1000"));

        Team t2 = new Team();
        t2.setName("Team2");
        t2.setAcronym("T2");
        t2.setBudget(new BigDecimal("2000"));

        Page<Team> page = new PageImpl<>(Arrays.asList(t1, t2), PageRequest.of(0, 10), 2);
        when(teamRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Team> res = teamService.findAll(PageRequest.of(0, 10));

        assertThat(res.getContent()).hasSize(2);
        assertEquals("Team1", res.getContent().get(0).getName());
        assertEquals("Team2", res.getContent().get(1).getName());
        verify(teamRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void addTeam_missing_fields_throws() {
        Team t = new Team();
        assertThrows(IllegalArgumentException.class, () -> teamService.addTeam(t));
    }

    @Test
    void addTeam_duplicate_name_throws() {
        when(teamRepository.existsByName("AC Test")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> teamService.addTeam(team));
    }

    @Test
    void findAll_pageable_returns_page() {
        Team t1 = new Team();
        t1.setName("Team1");
        t1.setAcronym("T1");
        t1.setBudget(new BigDecimal("1000"));

        Team t2 = new Team();
        t2.setName("Team2");
        t2.setAcronym("T2");
        t2.setBudget(new BigDecimal("2000"));

        Page<Team> page = new PageImpl<>(Arrays.asList(t1, t2), PageRequest.of(0, 10), 2);

        when(teamRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Team> res = teamService.findAll(PageRequest.of(0, 10));

        assertThat(res.getContent()).hasSize(2);
        assertEquals(2, res.getTotalElements());
        assertEquals("Team1", res.getContent().get(0).getName());
        verify(teamRepository, times(1)).findAll(any(Pageable.class));
    }
}
