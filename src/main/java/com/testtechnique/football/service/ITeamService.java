/**
 * Interface de service pour la gestion des équipes (contrat).
 */
package com.testtechnique.football.service;

import com.testtechnique.football.domain.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ITeamService {
    Page<Team> findAll(Pageable pageable);
    Optional<Team> findById(Long id);
    Team addTeam(Team team);
    Team updateTeam(Long id, Team team);
    void deleteTeam(Long id);
}
