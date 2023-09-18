package com.example.mygame;

import static com.example.mygame.GameActivity.HIGH_SCORE;
import static com.example.mygame.GameActivity.SHARED_PREFS;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mygame.web.HTTPRequest;

/**
 * The main activity of the application.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String NAME_PREF = "playerName";
    public String playerName;
    private int highScore;

    /**
     * Create references to the buttons and assign their on click functionality.
     * Also try loading the user name from the shared preferences for high score table.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting");

        loadHighScore();

        //Play button
        Button btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });

        //High Scores button
        Button btnHS = (Button) findViewById(R.id.btnHighScores);
        btnHS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHighScoreClick();
            }
        });

        //Back navigation
        OnBackPressedCallback backPress = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "handleOnBackPressed: Exiting app");
                System.exit(0);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, backPress);

        loadName(); // Load player name from shared prefs
    }

    /**
     * On clicking the high score button, attempt to upload the user high score
     * and then launch the high score activity if the user name is defined,
     * otherwise, direct user to get name activity to get user name.
     */
    private void onHighScoreClick() {
        if (playerName == "") {
            //If no name declared, ask for one and save it in shared prefs
            startActivity(new Intent(MainActivity.this, GetNameActivity.class));
            finish();
        }
        else {
            // If name declared, upload high score and show table
            HTTPRequest request = new HTTPRequest(this, playerName, highScore);
            request.send();
            startActivity(new Intent(MainActivity.this, HighScoresActivity.class));
            finish();
        }
    }

    /**
     * Switch to levels activity to choose a level to play upon pressing play button.
     */
    private void play(){
        startActivity(new Intent(MainActivity.this, LevelsActivity.class));
        finish();
    }

    /**
     * Load the name from the shared preferences.
     */
    private void loadName() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        playerName = sharedPreferences.getString(NAME_PREF, "");
    }

    /**
     * Load user high score from the shared preferences.
     */
    private void loadHighScore() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highScore = sharedPreferences.getInt(HIGH_SCORE, 0);
    }
}