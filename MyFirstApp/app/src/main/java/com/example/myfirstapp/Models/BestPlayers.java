package com.example.myfirstapp.Models;

import java.util.ArrayList;

public class BestPlayers {
    private ArrayList<Player> bestPlayers;

    public BestPlayers() {
        this.bestPlayers = new ArrayList<>();
    }

    public ArrayList<Player> getBestPlayers() {
        return bestPlayers;
    }

    @Override
    public String toString() {
        return "BestPlayers{" +
                "bestPlayers=" + bestPlayers +
                '}';
    }
}
