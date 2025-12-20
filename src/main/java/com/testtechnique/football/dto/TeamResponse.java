package com.testtechnique.football.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO pour la sortie d'une équipe
 */
public class TeamResponse {
    public Long id;
    public String name;
    public String acronym;
    public BigDecimal budget;
    public List<PlayerResponse> players;

    public TeamResponse() {}

    public TeamResponse(Long id, String name, String acronym, BigDecimal budget, List<PlayerResponse> players) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.budget = budget;
        this.players = players;
    }
}

