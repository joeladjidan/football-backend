/**
 * Service métier pour la gestion des équipes (liste paginée, création).
 * <p>
 * @author Joël ADJIDAN
 * @since 2025-12-11
 */

package com.testtechnique.football.service;

import com.testtechnique.football.domain.Team;
import com.testtechnique.football.domain.Player;
import com.testtechnique.football.repository.ITeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements ITeamService {
    private final ITeamRepository teamRepository;
    public TeamServiceImpl(ITeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }
    
    /**
     * Retourne la liste paginée des équipes.
     * @param pageable paramètres de pagination/tri
     * @return page d'équipes
     */
    public Page<Team> findAll(Pageable pageable) {
        return teamRepository.findAll(pageable);
    }

    /**
     * Retourne une équipe par son id.
     */
    public java.util.Optional<Team> findById(Long id) {
        return teamRepository.findById(id);
    }

    /**
     * Crée une équipe avec validation des champs obligatoires et contrôle de doublon.
     * @param team entité équipe à créer
     * @return équipe persistée
     * @throws IllegalArgumentException si champs manquants ou doublon sur le nom
     */
    @Transactional
    public Team addTeam(Team team) {

            if (team.getName() == null || team.getAcronym() == null || team.getBudget() == null) {
                throw new IllegalArgumentException("Les champs name, acronym et budget sont obligatoires");
            }
            if (teamRepository.existsByName(team.getName())) {
                throw new IllegalArgumentException("Une équipe avec ce nom existe déjà");
            }
            // Cascade ALL gère l'ajout des joueurs si présents
            return teamRepository.save(team);
        }

    /**
     * Met à jour une équipe existante. Vérifie l'existence et les contraintes.
     * @param id identifiant de l'équipe à mettre à jour
     * @param update données mises à jour (name, acronym, budget, players)
     * @return équipe mise à jour
     * @throws java.util.NoSuchElementException si non trouvée
     * @throws IllegalArgumentException si validation échoue
     */
    @Transactional
    public Team updateTeam(Long id, Team update) {
        Team existing = teamRepository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Equipe non trouvée"));

        if (update.getName() == null || update.getAcronym() == null || update.getBudget() == null) {
            throw new IllegalArgumentException("Les champs name, acronym et budget sont obligatoires");
        }

        // Vérifier doublon de nom sur une autre entité
        if (!existing.getName().equals(update.getName()) && teamRepository.existsByName(update.getName())) {
            throw new IllegalArgumentException("Une équipe avec ce nom existe déjà");
        }

        existing.setName(update.getName());
        existing.setAcronym(update.getAcronym());
        existing.setBudget(update.getBudget());

        // Mettre à jour la collection players en place pour respecter orphanRemoval
        List<Player> incoming = update.getPlayers();
        if (incoming == null || incoming.isEmpty()) {
            // Remove all existing players (orphanRemoval will handle DB deletes)
            existing.getPlayers().clear();
        } else {
            // Collect ids of incoming players (those that already exist)
            Set<Long> incomingIds = incoming.stream()
                    .map(Player::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // Remove existing players that are not present in incoming -> orphanRemoval
            existing.getPlayers().removeIf(p -> p.getId() != null && !incomingIds.contains(p.getId()));

            // Update existing players and add new ones
            for (Player ip : incoming) {
                if (ip.getId() != null) {
                    // find matching existing player by id
                    Player match = existing.getPlayers().stream()
                            .filter(ep -> ip.getId().equals(ep.getId()))
                            .findFirst()
                            .orElse(null);
                    if (match != null) {
                        match.setName(ip.getName());
                        match.setPosition(ip.getPosition());
                    } else {
                        // incoming references an id we don't have in-memory: attach to existing
                        ip.setTeam(existing);
                        existing.getPlayers().add(ip);
                    }
                } else {
                    // new player, attach and add
                    ip.setTeam(existing);
                    existing.getPlayers().add(ip);
                }
            }
        }

        return teamRepository.save(existing);
    }

    /**
     * Supprime une équipe par son id.
     * @param id identifiant à supprimer
     * @throws java.util.NoSuchElementException si l'équipe n'existe pas
     */
    @Transactional
    public void deleteTeam(Long id) {
        // vérification d'existence
        if (!teamRepository.findById(id).isPresent()) {
            throw new java.util.NoSuchElementException("Equipe non trouvée");
        }
        teamRepository.deleteById(id);
    }
}
