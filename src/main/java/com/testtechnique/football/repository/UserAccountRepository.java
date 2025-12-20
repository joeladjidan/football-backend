/**
 * Repository Spring Data JPA pour l’entité UserAccount.
 * <p>
 * @author Joël ADJIDAN
 * @since 2025-12-11
 */

package com.testtechnique.football.repository;

import com.testtechnique.football.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
