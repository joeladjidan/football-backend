/**
 * Implémentation manuelle du mapper Team (simple, sans MapStruct).
 */
package com.testtechnique.football.mapper;

import com.testtechnique.football.domain.Player;
import com.testtechnique.football.domain.Team;
import com.testtechnique.football.dto.PlayerResponse;
import com.testtechnique.football.dto.TeamRequest;
import com.testtechnique.football.dto.TeamResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeamMapperImpl implements TeamMapper {
    @Override
    public Team toEntity(TeamRequest req) {
        Team t = new Team();
        t.setName(req.name);
        t.setAcronym(req.acronym);
        t.setBudget(req.budget);
        if (req.players != null) {
            t.setPlayers(req.players.stream().map(p -> {
                Player pl = new Player();
                pl.setName(p.name);
                pl.setPosition(p.position);
                pl.setTeam(t);
                return pl;
            }).collect(Collectors.toList()));
        }
        return t;
    }

    @Override
    public TeamResponse toResponse(Team t) {
        List<PlayerResponse> players = t.getPlayers() == null ? null : t.getPlayers().stream()
                .map(p -> new PlayerResponse(p.getId(), p.getName(), p.getPosition()))
                .collect(Collectors.toList());
        return new TeamResponse(t.getId(), t.getName(), t.getAcronym(), t.getBudget(), players);
    }
}
