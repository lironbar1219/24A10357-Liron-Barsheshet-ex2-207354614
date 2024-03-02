package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfirstapp.Interfaces.Callback_GoBackFromFragment;
import com.example.myfirstapp.Interfaces.Callback_highScoreClicked;
import com.example.myfirstapp.Views.MapFragment;
import com.example.myfirstapp.Views.ScoreFragment;

public class ScoreLocationActivity  extends AppCompatActivity implements Callback_GoBackFromFragment {
    private FrameLayout score_frame;
    private FrameLayout location_frame;

    private MapFragment mapFragment;
    private ScoreFragment scoreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_location);
        findViews();
        findViews();

        // Initialize mapFragment before using it
        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.location_frame, mapFragment).commit();

        // Now it's safe to use mapFragment
        scoreFragment = new ScoreFragment();
        scoreFragment.setCallbackHighScoreClicked((lat, lon) -> mapFragment.zoom(lat, lon));
        scoreFragment.setCallbackGoBackFromFragment(this);

        getSupportFragmentManager().beginTransaction().add(R.id.score_frame, scoreFragment).commit();
        //getSupportFragmentManager().beginTransaction().add(R.id.location_frame, mapFragment).commit();
    }

    private void findViews() {
        score_frame = findViewById(R.id.score_frame);
        location_frame = findViewById(R.id.location_frame);

    }

    @Override
    public void goBack() {
        // Create an Intent to go back to MenuActivity
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}
