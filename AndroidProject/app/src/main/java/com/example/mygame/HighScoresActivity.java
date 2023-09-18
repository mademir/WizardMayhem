package com.example.mygame;

import static com.example.mygame.GameActivity.SHARED_PREFS;
import static com.example.mygame.MainActivity.NAME_PREF;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * High score activity to display the online high score table.
 */
public class HighScoresActivity extends AppCompatActivity {
    String data = "";

    private static final String Json_URL ="https://musty6821.alwaysdata.net/scores.json";
    private String playerName;
    private Context activiy;

    /**
     * Set the references to the views and define their functionality.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores_activity);

        this.activiy = this;
        loadName(); // Load player name from shared prefs

        //Done button
        Button btnDone = (Button) findViewById(R.id.done_highScores);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDoneClick();
            }
        });

        new Thread(new Runnable() {
            public void run() {
                data = readURL(Json_URL);
                Log.d("HTTPRequest.java", data);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        List<AbstractMap.SimpleEntry<String, String>> entryList = parseString(data);
                        addToTable(entryList);
                    }
                });

            }
        }).start();
    }

    /**
     * Adds entries to table.
     * @param entryList entries to add to table
     */
    private void addToTable(List<AbstractMap.SimpleEntry<String, String>> entryList) {
        TableLayout stk = (TableLayout) findViewById(R.id.hs_table);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" Name ");
        tv0.setTextSize(25);
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" High Score ");
        tv1.setTextSize(25);
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);

        tbrow0.setGravity(Gravity.CENTER);
        stk.addView(tbrow0);
        for (int i = 0; i < entryList.size(); i++) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText(entryList.get(i).getKey());
            t1v.setTextColor(Color.GREEN);
            t1v.setTextSize(25);
            t1v.setGravity(Gravity.BOTTOM);
            t1v.setWidth(600);
            t1v.setMaxLines(1);
            t1v.setMaxHeight(100);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText(entryList.get(i).getValue());
            t2v.setTextColor(Color.YELLOW);
            t2v.setTextSize(25);
            t2v.setGravity(Gravity.CENTER_HORIZONTAL);
            tbrow.addView(t2v);

            if (entryList.get(i).getKey().equals(playerName)){
                tbrow.setBackgroundColor(Color.BLUE); // Highlight this player
            }
            tbrow.setGravity(Gravity.CENTER);
            stk.addView(tbrow);
        }
    }

    /**
     * Parses json string to get names and scores
     * @param input json string
     * @return names and scores
     */
    public static List<AbstractMap.SimpleEntry<String, String>> parseString(String input) {
        if (input.isEmpty()) return new ArrayList<AbstractMap.SimpleEntry<String, String>>();
        input = input.replaceAll("\\{", "").replaceAll("\\}", ""); // Remove braces from input string
        String[] pairs = input.split(","); // Split into individual "name:score" pairs
        List<AbstractMap.SimpleEntry<String, String>> entryList = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
        for (String pair : pairs) {
            String[] parts = pair.split(":"); // Split each pair into name and score
            AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<String, String>(parts[0].replaceAll("\"", ""), parts[1].replaceAll("\"", ""));
            entryList.add(entry);
        }
        return entryList;
    }

    /**
     * Reads json string from url
     * @param json_url url to read from
     * @return json string
     */
    private String readURL(String json_url) {
        try {
            String res = "";
            URL url = null;
            try {
                url = new URL(json_url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                displayMessage();
            }
            BufferedReader read = null;
            try {
                read = new BufferedReader(
                        new InputStreamReader(url.openStream()));
            } catch (IOException e) {
                e.printStackTrace();
                displayMessage();
            }
            String i = "";
            while (true) {
                try {
                    if (!((i = read.readLine()) != null)) break;
                    res += i;
                } catch (IOException e) {
                    e.printStackTrace();
                    displayMessage();
                }
            }
            try {
                read.close();
            } catch (IOException e) {
                e.printStackTrace();
                displayMessage();
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            displayMessage();
            return "";
        }
    }

    /**
     * Go back to main activity when pressed done button
     */
    private void onDoneClick() {
        startActivity(new Intent(HighScoresActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Load player name from shared prefs
     */
    private void loadName() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        playerName = sharedPreferences.getString(NAME_PREF, "");
    }

    /**
     * Displays messages instead of crashing.
     */
    public void displayMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activiy, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
