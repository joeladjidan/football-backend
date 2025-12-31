package com.testtechnique.football.web;

import com.testtechnique.football.dto.UserRequest;
import com.testtechnique.football.dto.UserResponse;
import com.testtechnique.football.service.IUserService;
import com.testtechnique.football.domain.User;
import com.testtechnique.football.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

/**
 * REST controller exposing the /api/users endpoints.
 * <p>
 * Handles user-related operations such as listing, retrieving, creating,
 * updating and deleting users. Some endpoints are restricted to ADMIN role.
 * </p>
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService svc;
    private final UserMapper userMapper;

    /**
     * Constructor to inject required services and mapper.
     *
     * @param svc Service handling user operations.
     * @param userMapper Mapper to convert between DTOs and entities.
     */
    public UserController(IUserService svc, UserMapper userMapper) {
        this.svc = svc;
        this.userMapper = userMapper;
    }

    @GetMapping
    public org.springframework.data.domain.Page<UserResponse> list(@RequestParam(value = "q", required = false) String q, org.springframework.data.domain.Pageable pageable) {
        return svc.searchByUsername(q, pageable).map(userMapper::toResponse);
    }

    /**
     * Returns a user by id.
     *
     * @param id User identifier
     * @return ResponseEntity with the user DTO or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id) {
        return svc.findById(id).map(u -> ResponseEntity.ok(userMapper.toResponse(u))).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Public registration endpoint (no authentication required).
     *
     * @param req  User registration payload
     * @param br   Binding result for validation errors
     * @return Created user response or validation errors
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest req, BindingResult br) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(br.getAllErrors());
        }
        User ua = userMapper.toEntity(req);
        // default role applied by mapper
        User saved = svc.create(ua);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(saved));
    }

    /**
     * Creates a user (ADMIN only).
     *
     * @param req User payload
     * @param br  Binding result for validation
     * @return Created user DTO or validation errors
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody UserRequest req, BindingResult br) {
        if (br.hasErrors()) { return ResponseEntity.badRequest().body(br.getAllErrors()); }
        User ua = userMapper.toEntity(req);
        User saved = svc.create(ua);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(saved));
    }

    /**
     * Updates a user (ADMIN only).
     *
     * @param id  Identifier of the user to update
     * @param req User payload
     * @param br  Binding result for validation
     * @return Updated user DTO or validation errors
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UserRequest req, BindingResult br) {
        if (br.hasErrors()) { return ResponseEntity.badRequest().body(br.getAllErrors()); }
        User ua = userMapper.toEntity(req);
        User updated = svc.update(id, ua);
        return ResponseEntity.ok(userMapper.toResponse(updated));
    }

    /**
     * Allows the authenticated user to update their own profile.
     * Roles cannot be changed via this endpoint.
     *
     * @param auth Authentication object of the current user
     * @param req  User payload
     * @param br   Binding result for validation
     * @return Updated user DTO or validation errors
     */
    @PutMapping("/me")
    public ResponseEntity<?> updateMe(Authentication auth, @Valid @RequestBody UserRequest req, BindingResult br) {
        if (br.hasErrors()) { return ResponseEntity.badRequest().body(br.getAllErrors()); }
        String username = auth.getName();
        User existing = svc.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        User ua = userMapper.toEntity(req);
        ua.setRoles(existing.getRoles()); // cannot change roles via /me
        User updated = svc.update(existing.getId(), ua);
        return ResponseEntity.ok(userMapper.toResponse(updated));
    }

    /**
     * Deletes a user by id (ADMIN only).
     *
     * @param id Identifier of the user to delete
     * @return No content response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}
