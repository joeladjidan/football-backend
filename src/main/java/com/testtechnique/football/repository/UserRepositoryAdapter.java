/**
 * Adapter déléguant IUserRepository vers UserAccountRepository (Spring Data)
 */
package com.testtechnique.football.repository;

import com.testtechnique.football.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements IUserRepository {
    private final UserAccountRepository delegate;
    public UserRepositoryAdapter(UserAccountRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<User> findAll() { return delegate.findAll(); }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return delegate.findAll(pageable);
    }

    @Override
    public Optional<User> findById(Long id) { return delegate.findById(id); }

    @Override
    public Optional<User> findByUsername(String username) { return delegate.findByUsername(username); }

    @Override
    public Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable) {
        return delegate.findByUsernameContainingIgnoreCase(username, pageable);
    }

    @Override
    public User save(User ua) { return delegate.save(ua); }

    @Override
    public void deleteById(Long id) { delegate.deleteById(id); }
}
