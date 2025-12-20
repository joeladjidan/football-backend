package com.testtechnique.football.service;

import com.testtechnique.football.domain.User;
import com.testtechnique.football.repository.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final IUserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public User create(User u) {
        // encode password unless already encoded (e.g., {noop} prefix allowed in SQL seed)
        if (u.getPassword() != null && !u.getPassword().startsWith("{noop}") && !u.getPassword().startsWith("{bcrypt}")) {
            u.setPassword(passwordEncoder.encode(u.getPassword()));
        }
        return repo.save(u);
    }

    public User update(Long id, User payload) {
        return repo.findById(id).map(existing -> {
            existing.setUsername(payload.getUsername());
            if (payload.getPassword() != null && !payload.getPassword().isBlank()) {
                if (!payload.getPassword().startsWith("{noop}") && !payload.getPassword().startsWith("{bcrypt}")) {
                    existing.setPassword(passwordEncoder.encode(payload.getPassword()));
                } else {
                    existing.setPassword(payload.getPassword());
                }
            }
            existing.setRoles(payload.getRoles());
            return repo.save(existing);
        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public org.springframework.data.domain.Page<User> searchByUsername(String q, org.springframework.data.domain.Pageable pageable) {
        if (q == null || q.isBlank()) {
            return repo.findAll(pageable);
        }
        return repo.findByUsernameContainingIgnoreCase(q, pageable);
    }
}
