package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final EditText editTextName = findViewById(R.id.editTextName);
        Button buttonSubmitName = findViewById(R.id.buttonSubmitName);

        buttonSubmitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();

                // Check if the name is not empty
                if (!name.isEmpty()) {
                    // Name is valid, proceed to MenuActivity and pass the name
                    Intent intent = new Intent(StartActivity.this, MenuActivity.class);
                    intent.putExtra("playerName", name);
                    startActivity(intent);
                } else {
                    // Set an error on the EditText if the name is empty
                    editTextName.setError("Please enter your name.");
                }
            }
        });
    }
}
