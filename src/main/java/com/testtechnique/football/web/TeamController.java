/**
 * Contrôleur REST exposant les endpoints /api/teams (GET paginé/trié, POST création).
 * <p>
 * @author Joël ADJIDAN
 * @since 2025-12-11
 */

package com.testtechnique.football.web;

import com.testtechnique.football.domain.Player;
import com.testtechnique.football.domain.Team;
import com.testtechnique.football.dto.TeamRequest;
import com.testtechnique.football.dto.TeamResponse;
import com.testtechnique.football.dto.PlayerResponse;
import com.testtechnique.football.service.ITeamService;
import com.testtechnique.football.mapper.TeamMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final ITeamService teamService;
    private final TeamMapper teamMapper;

    public TeamController(ITeamService teamService, TeamMapper teamMapper) {
        this.teamService = teamService;
        this.teamMapper = teamMapper;
    }

    /**
     * Retourne une page d'équipes (pagination + tri sur name, acronym, budget via Pageable)
     */
    @GetMapping
    public Page<TeamResponse> list(Pageable pageable) {
        return teamService.findAll(pageable).map(teamMapper::toResponse);
    }

    /**
     * Retourne une équipe par son id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getOne(@PathVariable Long id) {
        Optional<Team> opt = teamService.findById(id);
        return opt.map(teamMapper::toResponse).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Met à jour une équipe existante.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TeamRequest payload) {
        try {
            Team toUpdate = teamMapper.toEntity(payload);
            Team updated = teamService.updateTeam(id, toUpdate);
            return ResponseEntity.ok(teamMapper.toResponse(updated));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Ajoute une équipe avec ou sans joueurs.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeamResponse> add(@RequestBody TeamRequest payload) {
        Team t = teamMapper.toEntity(payload);
        Team saved = teamService.addTeam(t);
        return ResponseEntity.status(HttpStatus.CREATED).body(teamMapper.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            teamService.deleteTeam(id);
            return ResponseEntity.noContent().build();
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // mapping done by TeamMapper
}
