/**
 * Implémentation du mapper User.
 */
package com.testtechnique.football.mapper;

import com.testtechnique.football.dto.UserRequest;
import com.testtechnique.football.dto.UserResponse;
import com.testtechnique.football.domain.User;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User toEntity(UserRequest req) {
        User ua = new User();
        ua.setUsername(req.username);
        ua.setPassword(req.password);
        // convert list of roles to CSV string for storage; fallback to ROLE_USER
        if (req.roles == null) {
            ua.setRoles("ROLE_USER");
        } else if (req.roles instanceof List) {
            List<String> rl = req.roles;
            String joined = rl.stream().collect(Collectors.joining(","));
            ua.setRoles(joined.isEmpty() ? "ROLE_USER" : joined);
        } else {
            ua.setRoles(String.valueOf(req.roles));
        }
        return ua;
    }

    @Override
    public UserResponse toResponse(User ua) {
        return new UserResponse(ua.getId(), ua.getUsername(), ua.getRoles());
    }
}
