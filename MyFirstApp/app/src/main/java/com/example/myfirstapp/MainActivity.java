package com.example.myfirstapp;

import static java.lang.Thread.sleep;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myfirstapp.Logic.GameManager;
import com.example.myfirstapp.Models.Player;
import com.example.myfirstapp.R;
import com.example.myfirstapp.Utillities.SharedPreferencesManager;
import com.example.myfirstapp.Utillities.SoundExecutor;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private String playerName;
    private MaterialButton main_BTN_right;
    private MaterialButton main_BTN_left;

    private ShapeableImageView[] main_IMG_hearts;

    private ShapeableImageView[][] mainMatrix;

    private MaterialTextView main_LBL_score;

    private Vibrator vibrator;

    private static final long DELAY = 1000;   //400
    private static final long DELAY_FALLING = 700; ///200

    private static final long DELAY_fast = 600;   //400
    private static final long DELAY_FALLING_fast = 300; ///200

    private static String mode;

    private SoundExecutor successSound;
    private SoundExecutor failSound;
    private GameManager gameManager;
    final Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, DELAY);
            dropObject();
        }
    };

    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, DELAY_FALLING);
            changeObjectsPos();
        }
    };

    Runnable runnable_fast = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, DELAY_fast);
            dropObject();
        }
    };

    Runnable runnable2_fast = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, DELAY_FALLING_fast);
            changeObjectsPos();
        }
    };


    private void dropObject() {
        gameManager.dropObject();
    }


    public void toastAndVibrate(String text) {
        toast(text);
        vibrate();
    }

    private void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(500);
        }
    }

    private void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }


    private int numRows = 7;
    private int numColumns = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mainMatrix = new ShapeableImageView[numRows][numColumns];
        findViews();
        gameManager = new GameManager(main_IMG_hearts.length, numRows, numColumns);

        main_BTN_left.setOnClickListener(view -> leftClicked());
        main_BTN_right.setOnClickListener(view -> rightClicked());

        Intent intent = getIntent();
        mode = intent.getStringExtra("MODE");
        playerName = intent.getStringExtra("playerName");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            // Permission already granted
            getLastLocation();
        }

        if (mode.equals("FAST")) {
            handler.postDelayed(runnable_fast, 0);
            handler.postDelayed(runnable2_fast, DELAY_FALLING);
        } else {
            handler.postDelayed(runnable, 0);
            handler.postDelayed(runnable2, DELAY_FALLING);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        failSound = new SoundExecutor(this);
        successSound = new SoundExecutor(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        failSound.stopSoundFail();
        failSound.stopSoundSuccess();
    }

    private void leftClicked() {
        gameManager.moveLeft();
        refreshUI();
    }

    private void rightClicked() {
        gameManager.moveRight();
        refreshUI();
    }

    private void refreshUI() {
        changeSpaceShipPos();
    }

    private void changeObjectsPos() {
        gameManager.moveDownAllObjects();
        int posI;
        int posJ;
        Boolean isCoin;
        for (int i = 0; i < gameManager.getFallingObjects().size(); i++) {
            posI = gameManager.getFallingObjects().get(i).getposI();
            posJ = gameManager.getFallingObjects().get(i).getposJ();
            isCoin = gameManager.getFallingObjects().get(i).getIsCoin();
            if (isCoin) {
                if (posI < numRows) {
                    if (posI > 0) {
                        mainMatrix[posI - 1][posJ].setImageResource(R.drawable.coin);
                        mainMatrix[posI - 1][posJ].setVisibility(View.INVISIBLE);
                    }
                    if (posI == numRows - 1) {
                        if (mainMatrix[posI][posJ].getVisibility() == View.VISIBLE) {
                            // Coin took:
                            successSound.playSoundSuccess();
                            upScore();
                        }
                    } else {
                        mainMatrix[posI][posJ].setImageResource(R.drawable.coin);
                        mainMatrix[posI][posJ].setVisibility(View.VISIBLE);
                    }
                }
            } else {
                if (posI < numRows) {
                    if (posI > 0) {
                        mainMatrix[posI - 1][posJ].setImageResource(R.drawable.stone);
                        mainMatrix[posI - 1][posJ].setVisibility(View.INVISIBLE);
                    }
                    if (posI == numRows - 1) {
                        if (mainMatrix[posI][posJ].getVisibility() == View.VISIBLE) {
                            // Collusion:
                            collusionHappened(gameManager, "BOOOOM", mode);
                        }
                    } else {
                        mainMatrix[posI][posJ].setImageResource(R.drawable.stone);
                        mainMatrix[posI][posJ].setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private void upScore() {
        int score = Integer.parseInt(main_LBL_score.getText().toString());
        main_LBL_score.setText(main_LBL_score.getText());
        score += 5;
        main_LBL_score.setText(String.valueOf(score));
    }

    private void collusionHappened(GameManager gameManager, String text, String mode) {
        failSound.playSoundFail();
        gameManager.setLife(gameManager.getLife() - 1);
        main_BTN_left.setEnabled(false);
        main_BTN_right.setEnabled(false);
        toastAndVibrate(text);
        downLife();

        handler.removeCallbacks(runnable);
        handler.removeCallbacks(runnable2);
        handler.removeCallbacks(runnable_fast);
        handler.removeCallbacks(runnable2_fast);

        if (gameManager.getLife() == 0) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();

                            int score = Integer.parseInt(main_LBL_score.getText().toString());
                            Player player = new Player(playerName, score, lat, lon);
                            SharedPreferencesManager.addPlayer(this, player);

                            Intent intent = new Intent(this, MenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        } else {
            new Handler().postDelayed(() -> {
                main_BTN_left.setEnabled(true);
                main_BTN_right.setEnabled(true);

                if(mode.equals("SLOW")) {
                    handler.postDelayed(runnable, 0);
                    handler.postDelayed(runnable2, DELAY_FALLING);
                } else {
                    handler.postDelayed(runnable_fast, 0);
                    handler.postDelayed(runnable2_fast, DELAY_FALLING);
                }
            }, 2000);
        }
    }

    private void downLife() {
        if(gameManager.getLife() < main_IMG_hearts.length) {
            main_IMG_hearts[gameManager.getLife()].setVisibility(View.INVISIBLE);
        } else {
            for(int i = 0; i < main_IMG_hearts.length; i++) {
                main_IMG_hearts[i].setVisibility(View.VISIBLE);
            }
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_PERMISSION);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with getting location
                getLastLocation();
            } else {
                // Permission denied, handle as appropriate
            }
        }
    }

    private void changeSpaceShipPos() {
        mainMatrix[6][0].setVisibility(View.INVISIBLE);
        mainMatrix[6][1].setVisibility(View.INVISIBLE);
        mainMatrix[6][2].setVisibility(View.INVISIBLE);
        mainMatrix[6][3].setVisibility(View.INVISIBLE);
        mainMatrix[6][4].setVisibility(View.INVISIBLE);
        mainMatrix[6][gameManager.getPosSpaceShip()].setVisibility(View.VISIBLE);
    }

    private void getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            // Use the location object to get latitude and longitude
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();

                        }
                    });
        }
    }

    private void findViews() {
        main_IMG_hearts = new ShapeableImageView[] {
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };
        main_BTN_right = findViewById(R.id.main_BTN_right);
        main_BTN_left = findViewById(R.id.main_BTN_left);

        main_LBL_score = findViewById(R.id.main_LBL_score);

        mainMatrix[0][0] = findViewById(R.id.main_IMG_pos00);
        mainMatrix[0][1] = findViewById(R.id.main_IMG_pos01);
        mainMatrix[0][2] = findViewById(R.id.main_IMG_pos02);
        mainMatrix[0][3] = findViewById(R.id.main_IMG_pos03);
        mainMatrix[0][4] = findViewById(R.id.main_IMG_pos04);
        mainMatrix[1][0] = findViewById(R.id.main_IMG_pos10);
        mainMatrix[1][1] = findViewById(R.id.main_IMG_pos11);
        mainMatrix[1][2] = findViewById(R.id.main_IMG_pos12);
        mainMatrix[1][3] = findViewById(R.id.main_IMG_pos13);
        mainMatrix[1][4] = findViewById(R.id.main_IMG_pos14);
        mainMatrix[2][0] = findViewById(R.id.main_IMG_pos20);
        mainMatrix[2][1] = findViewById(R.id.main_IMG_pos21);
        mainMatrix[2][2] = findViewById(R.id.main_IMG_pos22);
        mainMatrix[2][3] = findViewById(R.id.main_IMG_pos23);
        mainMatrix[2][4] = findViewById(R.id.main_IMG_pos24);
        mainMatrix[3][0] = findViewById(R.id.main_IMG_pos30);
        mainMatrix[3][1] = findViewById(R.id.main_IMG_pos31);
        mainMatrix[3][2] = findViewById(R.id.main_IMG_pos32);
        mainMatrix[3][3] = findViewById(R.id.main_IMG_pos33);
        mainMatrix[3][4] = findViewById(R.id.main_IMG_pos34);
        mainMatrix[4][0] = findViewById(R.id.main_IMG_pos40);
        mainMatrix[4][1] = findViewById(R.id.main_IMG_pos41);
        mainMatrix[4][2] = findViewById(R.id.main_IMG_pos42);
        mainMatrix[4][3] = findViewById(R.id.main_IMG_pos43);
        mainMatrix[4][4] = findViewById(R.id.main_IMG_pos44);
        mainMatrix[5][0] = findViewById(R.id.main_IMG_pos50);
        mainMatrix[5][1] = findViewById(R.id.main_IMG_pos51);
        mainMatrix[5][2] = findViewById(R.id.main_IMG_pos52);
        mainMatrix[5][3] = findViewById(R.id.main_IMG_pos53);
        mainMatrix[5][4] = findViewById(R.id.main_IMG_pos54);
        mainMatrix[6][0] = findViewById(R.id.main_IMG_pos60);
        mainMatrix[6][1] = findViewById(R.id.main_IMG_pos61);
        mainMatrix[6][2] = findViewById(R.id.main_IMG_pos62);
        mainMatrix[6][3] = findViewById(R.id.main_IMG_pos63);
        mainMatrix[6][4] = findViewById(R.id.main_IMG_pos64);
    }









}
