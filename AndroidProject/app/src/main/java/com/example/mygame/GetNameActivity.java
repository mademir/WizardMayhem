package com.example.mygame;

import static com.example.mygame.GameActivity.HIGH_SCORE;
import static com.example.mygame.GameActivity.SHARED_PREFS;
import static com.example.mygame.MainActivity.NAME_PREF;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity to get the name of the user for online high score table.
 */
public class GetNameActivity extends AppCompatActivity {

    /**
     * References the views and defines their functionality.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_name_activity);

        // Text input
        EditText name_input = (EditText) findViewById(R.id.name_input);

        // Save name button
        Button btnSave = (Button) findViewById(R.id.btnSaveName);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveClick(name_input);
            }
        });
    }

    /**
     * On click for the button to save the name.
     * @param name_input the name inputted
     */
    private void onSaveClick(EditText name_input) {
        String name = name_input.getText().toString();
        if (name != "") {
            saveName(name);
            startActivity(new Intent(GetNameActivity.this, MainActivity.class));
            finish();
        }
    }

    /**
     * Save the name in the shared preferences.
     * @param name name to save
     */
    private void saveName(String name) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(NAME_PREF, name);
        editor.apply();

    }
}
