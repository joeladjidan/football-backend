/**
 * Implémentation du mapper User.
 */
package com.testtechnique.football.mapper;

import com.testtechnique.football.dto.UserRequest;
import com.testtechnique.football.dto.UserResponse;
import com.testtechnique.football.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User toEntity(UserRequest req) {
        User ua = new User();
        ua.setUsername(req.username);
        ua.setPassword(req.password);
        ua.setRoles(req.roles == null ? "ROLE_USER" : req.roles);
        return ua;
    }

    @Override
    public UserResponse toResponse(User ua) {
        return new UserResponse(ua.getId(), ua.getUsername(), ua.getRoles());
    }
}
