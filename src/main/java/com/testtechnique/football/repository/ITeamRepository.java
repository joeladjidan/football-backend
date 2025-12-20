/**
 * Abstraction de la persistance pour Team (adapter autour de Spring Data).
 */
package com.testtechnique.football.repository;

import com.testtechnique.football.domain.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ITeamRepository {
    Page<Team> findAll(Pageable pageable);
    Optional<Team> findById(Long id);
    Team save(Team team);
    boolean existsByName(String name);
    void deleteById(Long id);
}
