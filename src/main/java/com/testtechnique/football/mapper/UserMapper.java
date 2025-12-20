/**
 * Mapper DTO <-> Entity pour UserAccount.
 */
package com.testtechnique.football.mapper;

import com.testtechnique.football.dto.UserRequest;
import com.testtechnique.football.dto.UserResponse;
import com.testtechnique.football.domain.User;

public interface UserMapper {
    User toEntity(UserRequest req);
    UserResponse toResponse(User ua);
}
