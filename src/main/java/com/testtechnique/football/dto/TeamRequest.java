package com.testtechnique.football.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO pour la création/entrée d'une équipe
 */
public class TeamRequest {
    public String name;
    public String acronym;
    public BigDecimal budget;
    public List<PlayerRequest> players;

    public TeamRequest() {}

    public TeamRequest(String name, String acronym, BigDecimal budget, List<PlayerRequest> players) {
        this.name = name;
        this.acronym = acronym;
        this.budget = budget;
        this.players = players;
    }
}

