package com.example.myfirstapp;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MenuActivity extends AppCompatActivity {

    private MaterialButton button_arrows_slow;
    private MaterialButton button_arrows_fast;
    private MaterialButton button_sensors;
    private MaterialButton button_records;
    private  MaterialButton button_exit;
    private String playerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        playerName = getIntent().getStringExtra("playerName");
        findViews();

        // Listeners:

        button_arrows_slow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("MODE", "SLOW");
                intent.putExtra("playerName", playerName);
                startActivity(intent);
            }
        });

        button_arrows_fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("MODE", "FAST");
                intent.putExtra("playerName", playerName);
                startActivity(intent);
            }
        });

        button_sensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivitySensors.class);
                intent.putExtra("playerName", playerName);
                startActivity(intent);
            }
        });

        button_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ScoreLocationActivity.class);
                startActivity(intent);
            }
        });

        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);   // Simulate pressing on home button.
            }
        });
    }

    private void findViews() {
        button_arrows_slow = findViewById(R.id.button_play_with_arrows_slow);
        button_arrows_fast = findViewById(R.id.button_play_with_arrows_fast);
        button_sensors = findViewById(R.id.button_play_with_sensors);
        button_records = findViewById(R.id.button_records);
        button_exit = findViewById(R.id.button_exit);
    }

}