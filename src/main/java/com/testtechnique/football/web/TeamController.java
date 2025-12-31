/**
 * REST controller exposing the /api/teams endpoints (paged/sorted GET, POST create).
 * <p>
 * This controller handles CRUD operations for football teams.
 * It allows listing, retrieving, adding, updating and deleting teams.
 * Sensitive operations such as update and delete require the ADMIN role.
 * </p>
 *
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

    /**
     * Constructor to inject required dependencies.
     *
     * @param teamService Service responsible for managing teams.
     * @param teamMapper Mapper to convert between entities and DTOs.
     */
    public TeamController(ITeamService teamService, TeamMapper teamMapper) {
        this.teamService = teamService;
        this.teamMapper = teamMapper;
    }

    /**
     * Returns a page of teams (pagination + sorting on name, acronym, budget via Pageable).
     *
     * @param pageable Pagination and sorting information.
     * @return A page containing teams represented as DTOs.
     */
    @GetMapping
    public Page<TeamResponse> list(Pageable pageable) {
        return teamService.findAll(pageable).map(teamMapper::toResponse);
    }

    /**
     * Returns a team by its id.
     *
     * @param id Team identifier.
     * @return Response containing the team or a 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getOne(@PathVariable Long id) {
        Optional<Team> opt = teamService.findById(id);
        return opt.map(teamMapper::toResponse).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing team.
     *
     * @param id Identifier of the team to update.
     * @param payload Team data to update.
     * @return Response containing the updated team or an error status.
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
     * Adds a team with or without players.
     *
     * @param payload Team data to add.
     * @return Response containing the created team.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeamResponse> add(@RequestBody TeamRequest payload) {
        Team t = teamMapper.toEntity(payload);
        Team saved = teamService.addTeam(t);
        return ResponseEntity.status(HttpStatus.CREATED).body(teamMapper.toResponse(saved));
    }

    /**
     * Deletes a team by its identifier.
     *
     * @param id Identifier of the team to delete.
     * @return Empty response with an appropriate status code.
     */
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
}
