/**
 * Interface du service utilisateur (contrat) pour découpler la couche web de l'implémentation.
 */
package com.testtechnique.football.service;

import com.testtechnique.football.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    User create(User u);
    User update(Long id, User payload);
    void delete(Long id);
    Page<User> searchByUsername(String q, Pageable pageable);
}
