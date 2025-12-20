/**
 * Abstraction de la persistance pour Player.
 */
package com.testtechnique.football.repository;

import com.testtechnique.football.domain.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IPlayerRepository {
    List<Player> findAll();
    Optional<Player> findById(Long id);
    Player save(Player p);
    void deleteById(Long id);
}
