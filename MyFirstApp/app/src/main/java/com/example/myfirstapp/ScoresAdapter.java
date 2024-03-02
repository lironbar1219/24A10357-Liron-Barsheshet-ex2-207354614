package com.example.myfirstapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.Interfaces.Callback_highScoreClicked;
import com.example.myfirstapp.Models.Player;
import com.google.android.material.button.MaterialButton;

import java.util.Collections;
import java.util.List;

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder> {

    private List<Player> players;
    private Callback_highScoreClicked callback;

    public ScoresAdapter(List<Player> players, Callback_highScoreClicked callback) {
        Collections.sort(players, (player1, player2) -> Integer.compare(player2.getScore(), player1.getScore()));

        if (players.size() > 10) {
            players = players.subList(0, 10);
        }

        this.players = players;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MaterialButton button = (MaterialButton) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_item, parent, false);
        return new ViewHolder(button);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Player player = players.get(position);
        holder.playerButton.setText(player.getFullName() + ", " + player.getScore());
        holder.playerButton.setOnClickListener(v -> callback.highScoreClicked(player.getLat(), player.getLon()));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialButton playerButton;

        ViewHolder(MaterialButton button) {
            super(button);
            this.playerButton = button;
        }
    }
}

