/**
 * Service contract for user-related operations.
 *
 * Implementations provide user management functionality (CRUD, search) and
 * decouple the web layer from persistence concerns.
 */
package com.testtechnique.football.service;

import com.testtechnique.football.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    /**
     * Retrieve all users.
     * @return list of users
     */
    List<User> findAll();

    /**
     * Find a user by id.
     * @param id user identifier
     * @return optional user
     */
    Optional<User> findById(Long id);

    /**
     * Find a user by username.
     * @param username the username to search
     * @return optional user
     */
    Optional<User> findByUsername(String username);

    /**
     * Create a new user.
     * @param u user data
     * @return created user
     */
    User create(User u);

    /**
     * Update an existing user.
     * @param id id of the user to update
     * @param payload new user data
     * @return updated user
     */
    User update(Long id, User payload);

    /**
     * Delete a user by id.
     * @param id user id
     */
    void delete(Long id);

    /**
     * Search users by username with pagination.
     * @param q search query
     * @param pageable pagination information
     * @return page of users matching query
     */
    Page<User> searchByUsername(String q, Pageable pageable);
}
