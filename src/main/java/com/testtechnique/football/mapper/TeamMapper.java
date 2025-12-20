/**
 * Mapper DTO <-> Entity pour Team.
 */
package com.testtechnique.football.mapper;

import com.testtechnique.football.domain.Team;
import com.testtechnique.football.dto.TeamRequest;
import com.testtechnique.football.dto.TeamResponse;

public interface TeamMapper {
    Team toEntity(TeamRequest req);
    TeamResponse toResponse(Team t);
}
