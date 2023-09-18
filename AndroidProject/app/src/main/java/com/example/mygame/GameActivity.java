package com.example.mygame;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity that holds the actual game.
 */
public class GameActivity extends AppCompatActivity {
    public static final String TAG = "GameActivity";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String HIGH_SCORE = "highScore";
    private Game game;
    private int highScore;

    /**
     * Evaluates the values to create a game like level and initialises a new game.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int level = getIntent().getExtras().getInt("level"); // Get selected level

        // Get custom attributes
        int customMapNo = 1, customEnemyHP= 1, customPlayerHP = 1;
        boolean [] customEnemyToggle = new boolean[3];
        if (level == 4) {
            customMapNo = getIntent().getExtras().getInt("map");
            customEnemyHP = getIntent().getExtras().getInt("enemyHP");
            customPlayerHP = getIntent().getExtras().getInt("playerHP");
            customEnemyToggle[0] = getIntent().getExtras().getBoolean("e1");
            customEnemyToggle[1] = getIntent().getExtras().getBoolean("e2");
            customEnemyToggle[2] = getIntent().getExtras().getBoolean("e3");
        }

        loadHighScore();

        // Launch a new game
        game = new Game(this, this, highScore, level, customMapNo, customEnemyHP, customPlayerHP, customEnemyToggle);
        setContentView(game);
        Log.d(TAG, "onCreate: Starting");

        //Back navigation
        OnBackPressedCallback backPress = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                back();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, backPress);
    }

    /**
     * Loads the high score from the shared preferences.
     */
    private void loadHighScore() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highScore = sharedPreferences.getInt(HIGH_SCORE, 0);
    }

    /**
     * Pause the game on activity pause.
     */
    @Override
    protected void onPause() {
        game.pause();
        super.onPause();
    }

    /**
     * Back method to go back to the main activity
     */
    private void back(){
        Log.d(TAG, "back: Switching to main menu");
        startActivity(new Intent(GameActivity.this, MainActivity.class));
        finish();
    }

}
