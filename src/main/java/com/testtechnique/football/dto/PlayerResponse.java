package com.testtechnique.football.dto;

/**
 * DTO pour la sortie d'un joueur
 */
public class PlayerResponse {
    public Long id;
    public String name;
    public String position;

    public PlayerResponse() {}

    public PlayerResponse(Long id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }
}

