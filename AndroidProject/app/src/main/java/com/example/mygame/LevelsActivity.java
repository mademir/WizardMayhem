package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Levels activity to display all the levels playable including custoom level.
 */
public class LevelsActivity extends AppCompatActivity {

    /**
     * Creates references to the button views and assigns their functionality to launch games.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        //Level 1 button
        Button btnLvl1 = (Button) findViewById(R.id.btnLevel1);
        btnLvl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchGame(1);
            }
        });
        //Level 2 button
        Button btnLvl2 = (Button) findViewById(R.id.btnLevel2);
        btnLvl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchGame(2);
            }
        });
        //Level 3 button
        Button btnLvl3 = (Button) findViewById(R.id.btnLevel3);
        btnLvl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchGame(3);
            }
        });
        //Custom level button
        Button btnLvlCustom = (Button) findViewById(R.id.btnLevelCustom);
        btnLvlCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               switchToLevelMaker();
            }
        });
        //Back button
        Button btnBack = (Button) findViewById(R.id.btnLevelsBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }

    /**
     * Back function to go back to the main activity.
     */
    private void back() {
        startActivity(new Intent(LevelsActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Switch to the custom level activity to generate a custom level.
     */
    private void switchToLevelMaker() {
        startActivity(new Intent(LevelsActivity.this, CustomLevelActivity.class));
        finish();
    }

    /**
     * Launch the game by switching to the game activity.
     * @param level level to launch the game in.
     */
    private void launchGame(int level) {
        Intent intent = new Intent(LevelsActivity.this, GameActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
        finish();
    }
}