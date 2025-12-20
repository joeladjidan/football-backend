/**
 * Adaptateur qui implémente ITeamRepository en déléguant au Spring Data TeamRepository.
 */
package com.testtechnique.football.repository;

import com.testtechnique.football.domain.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TeamRepositoryAdapter implements ITeamRepository {
    private final TeamRepository delegate;
    public TeamRepositoryAdapter(TeamRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Page<Team> findAll(Pageable pageable) {
        return delegate.findAll(pageable);
    }

    @Override
    public Optional<Team> findById(Long id) {
        return delegate.findById(id);
    }

    @Override
    public Team save(Team team) {
        return delegate.save(team);
    }

    @Override
    public boolean existsByName(String name) {
        return delegate.existsByName(name);
    }

    @Override
    public void deleteById(Long id) {
        delegate.deleteById(id);
    }
}
