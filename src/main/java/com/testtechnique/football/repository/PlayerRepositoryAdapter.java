/**
 * Adapter pour IPlayerRepository qui délègue au PlayerRepository Spring Data.
 */
package com.testtechnique.football.repository;

import com.testtechnique.football.domain.Player;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PlayerRepositoryAdapter implements IPlayerRepository {
    private final PlayerRepository delegate;
    public PlayerRepositoryAdapter(PlayerRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<Player> findAll() { return delegate.findAll(); }

    @Override
    public Optional<Player> findById(Long id) { return delegate.findById(id); }

    @Override
    public Player save(Player p) { return delegate.save(p); }

    @Override
    public void deleteById(Long id) { delegate.deleteById(id); }
}
