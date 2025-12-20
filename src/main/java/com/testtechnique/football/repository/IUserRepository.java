/**
 * Abstraction de la persistance pour les users.
 */
package com.testtechnique.football.repository;

import com.testtechnique.football.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    List<User> findAll();
    Page<User> findAll(Pageable pageable);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    User save(User ua);
    void deleteById(Long id);
}
