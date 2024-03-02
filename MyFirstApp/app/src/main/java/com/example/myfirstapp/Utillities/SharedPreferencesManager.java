package com.example.myfirstapp.Utillities;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.myfirstapp.Models.BestPlayers;
import com.example.myfirstapp.Models.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SharedPreferencesManager {
    private static final String DB_FILE = "DB_FILE";
    private static final String BEST_PLAYERS_KEY = "BestPlayers";

    // Save the list of best players
    public static void saveBestPlayers(Context context, BestPlayers bestPlayers) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(bestPlayers);
        editor.putString(BEST_PLAYERS_KEY, json);
        editor.apply();
    }

    // Retrieve the list of best players
    public static BestPlayers getBestPlayers(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(BEST_PLAYERS_KEY, null);
        Type type = new TypeToken<BestPlayers>() {}.getType();
        BestPlayers bestPlayers = gson.fromJson(json, type);
        if (bestPlayers == null) {
            return new BestPlayers();
        }
        return bestPlayers;
    }

    // Add a player to the list and ensure it's sorted and doesn't exceed 10 entries
    public static void addPlayer(Context context, Player player) {
        BestPlayers bestPlayers = getBestPlayers(context);
        ArrayList<Player> playersList = bestPlayers.getBestPlayers();
        playersList.add(player);
        // Sort by score in descending order
        Collections.sort(playersList, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Integer.compare(p2.getScore(), p1.getScore());
            }
        });
        // Keep only the top 10 scores
        if (playersList.size() > 10) {
            playersList.subList(10, playersList.size()).clear();
        }
        saveBestPlayers(context, bestPlayers);
    }
}
