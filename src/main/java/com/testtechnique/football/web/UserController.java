package com.testtechnique.football.web;

import com.testtechnique.football.dto.UserRequest;
import com.testtechnique.football.dto.UserResponse;
import com.testtechnique.football.service.UserService;
import com.testtechnique.football.domain.User;
import com.testtechnique.football.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService svc;
    private final UserMapper userMapper;

    public UserController(UserService svc, UserMapper userMapper) {
        this.svc = svc;
        this.userMapper = userMapper;
    }

    @GetMapping
    public org.springframework.data.domain.Page<UserResponse> list(@RequestParam(value = "q", required = false) String q, org.springframework.data.domain.Pageable pageable) {
        return svc.searchByUsername(q, pageable).map(userMapper::toResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id) {
        return svc.findById(id).map(u -> ResponseEntity.ok(userMapper.toResponse(u))).orElse(ResponseEntity.notFound().build());
    }

    // Public registration endpoint (no auth required)
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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody UserRequest req, BindingResult br) {
        if (br.hasErrors()) { return ResponseEntity.badRequest().body(br.getAllErrors()); }
        User ua = userMapper.toEntity(req);
        User saved = svc.create(ua);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UserRequest req, BindingResult br) {
        if (br.hasErrors()) { return ResponseEntity.badRequest().body(br.getAllErrors()); }
        User ua = userMapper.toEntity(req);
        User updated = svc.update(id, ua);
        return ResponseEntity.ok(userMapper.toResponse(updated));
    }

    // Allow user to update their own profile
    @PutMapping("/me")
    public ResponseEntity<?> updateMe(Authentication auth, @Valid @RequestBody UserRequest req, BindingResult br) {
        if (br.hasErrors()) { return ResponseEntity.badRequest().body(br.getAllErrors()); }
        String username = auth.getName();
        User existing = svc.findByUsername(username).orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        User ua = userMapper.toEntity(req);
        ua.setRoles(existing.getRoles()); // cannot change roles via /me
        User updated = svc.update(existing.getId(), ua);
        return ResponseEntity.ok(userMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}
