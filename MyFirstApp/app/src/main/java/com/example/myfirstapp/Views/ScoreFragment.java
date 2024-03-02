package com.example.myfirstapp.Views;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfirstapp.Interfaces.Callback_GoBackFromFragment;
import com.example.myfirstapp.Interfaces.Callback_highScoreClicked;
import com.example.myfirstapp.MainActivity;
import com.example.myfirstapp.MenuActivity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.Models.Player;
import com.example.myfirstapp.ScoresAdapter;
import com.example.myfirstapp.Utillities.SharedPreferencesManager;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ScoreFragment extends Fragment {
    private RecyclerView scoresList;

    private MaterialButton goBackBtn;
    private Callback_highScoreClicked callbackHighScoreClicked;
    private Callback_GoBackFromFragment callbackGoBackFromFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        scoresList = view.findViewById(R.id.scores_list);
        scoresList.setLayoutManager(new LinearLayoutManager(getContext()));
        goBackBtn = view.findViewById(R.id.score_btn_go_back);
        goBackBtn.setOnClickListener(v -> goBack());

        // Retrieve the best players list here. For demonstration, let's assume it's passed or set somehow.
        List<Player> bestPlayers = SharedPreferencesManager.getBestPlayers(getContext()).getBestPlayers();

        ScoresAdapter adapter = new ScoresAdapter(bestPlayers, new Callback_highScoreClicked() {
            @Override
            public void highScoreClicked(double lat, double lon) {
                if(callbackHighScoreClicked != null) {
                    callbackHighScoreClicked.highScoreClicked(lat, lon);
                }
            }
        });
        scoresList.setAdapter(adapter);

        return view;
    }

    public void setCallbackHighScoreClicked(Callback_highScoreClicked callback) {
        this.callbackHighScoreClicked = callback;
    }

    public void setCallbackGoBackFromFragment(Callback_GoBackFromFragment callback) {
        this.callbackGoBackFromFragment = callback;
    }

    private void goBack() {
        if (callbackGoBackFromFragment != null) {
            callbackGoBackFromFragment.goBack();
        }
    }
}
