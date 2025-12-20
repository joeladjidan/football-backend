/**
 * Repository Spring Data JPA pour l’entité Player.
 * <p>
 * @author Joël ADJIDAN
 * @since 2025-12-11
 */

package com.testtechnique.football.repository;

import com.testtechnique.football.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {}
