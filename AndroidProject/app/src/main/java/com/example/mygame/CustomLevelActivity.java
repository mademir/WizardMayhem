package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.InputStream;
import java.net.URL;

/**
 * Activity to create a custom game level.
 */
public class CustomLevelActivity extends AppCompatActivity {

    private int[] mapBtnIDs;
    private RadioGroup mapBtns;
    private SeekBar enemyHP;
    private SeekBar playerHP;
    private ToggleButton enemy1;
    private ToggleButton enemy2;
    private ToggleButton enemy3;

    /**
     * Create references to the Views and assign their functionality.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_level);

        fetchImageFromUrl("https://www.pngplay.com/wp-content/uploads/1/Button-PNG-Royalty-Free-Photo.png");

        mapBtns = (RadioGroup) findViewById(R.id.radioBtns);
        mapBtnIDs = new int [] {
                R.id.radioButton,
                R.id.radioButton2,
                R.id.radioButton3
        };

        enemyHP = (SeekBar) findViewById(R.id.seekBar1);
        playerHP = (SeekBar) findViewById(R.id.seekBar2);
        enemy1 = (ToggleButton) findViewById(R.id.e1toggle);
        enemy2 = (ToggleButton) findViewById(R.id.e2toggle);
        enemy3 = (ToggleButton) findViewById(R.id.e3toggle);

        //Play button
        Button btnPlay = (Button) findViewById(R.id.btnCustomPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mapNo = 1;
                switch (mapBtns.getCheckedRadioButtonId()) {
                    case R.id.radioButton2:
                        mapNo = 2;
                        break;
                    case R.id.radioButton3:
                        mapNo = 3;
                        break;
                    default:
                        break;
                }

                Intent intent = new Intent(CustomLevelActivity.this, GameActivity.class);

                intent.putExtra("level", 4);
                intent.putExtra("map", mapNo);
                intent.putExtra("enemyHP", enemyHP.getProgress());
                intent.putExtra("playerHP", playerHP.getProgress());
                intent.putExtra("e1", enemy1.isChecked());
                intent.putExtra("e2", enemy2.isChecked());
                intent.putExtra("e3", enemy3.isChecked());

                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Fetch image from given URL and place it as text backgrounds.
     * @param url URL to fetch the image from
     */
    public void fetchImageFromUrl(String url) {
        Activity activity = this;
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputStream inputStream = (InputStream) new URL(url).getContent();
                            Drawable drawable = Drawable.createFromStream(inputStream, "newImage");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView textView1 = (TextView) findViewById(R.id.textView3);
                                    textView1.setBackground(drawable);
                                    TextView textView2 = (TextView) findViewById(R.id.textView2);
                                    textView2.setBackground(drawable);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    displayMessage("Could not load the image from URL!");
                                }
                            });
                        }
                    }
                }
        ).start();

    }

    /**
     * Displays a message as toast.
     * @param message message to be displayed
     */
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}