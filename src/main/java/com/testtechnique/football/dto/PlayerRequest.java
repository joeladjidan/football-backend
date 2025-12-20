package com.testtechnique.football.dto;

/**
 * DTO pour la création/entrée d'un joueur
 */
public class PlayerRequest {
    public String name;
    public String position; // GK, DF, MF, FW

    public PlayerRequest() {}

    public PlayerRequest(String name, String position) {
        this.name = name;
        this.position = position;
    }
}

