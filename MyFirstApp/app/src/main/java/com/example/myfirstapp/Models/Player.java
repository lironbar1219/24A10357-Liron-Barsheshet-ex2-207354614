package com.example.myfirstapp.Models;

public class Player {
    private String fullName;
    private int score;
    private double lat;
    private double lon;

    // Updated constructor to include score
    public Player(String fullName, int score, double lat, double lon) {
        this.fullName = fullName;
        this.score = score;
        this.lat = lat;
        this.lon = lon;
    }

    // Getters
    public String getFullName() { return fullName; }
    public int getScore() { return score; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }

    @Override
    public String toString() {
        return "Player{" +
                "fullName='" + fullName + '\'' +
                ", score=" + score +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
