package com.example.mygame.web;

import android.app.Activity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class HTTPRequest extends Thread{

    private static final String Score_URL ="https://musty6821.alwaysdata.net/scores.php";

    private final String name;
    private final int highScore;
    private final Activity activity;

    /**
     * Constructs an HTTP request to store the high score in the database
     * @param activity caller activity
     * @param name name of the user
     * @param highScore value of the high score to be stored
     */
    public HTTPRequest(Activity activity, String name, int highScore) {
        this.name = name;
        this.highScore = highScore;
        this.activity = activity;
    }

    /**
     * Send this request.
     */
    public void send(){
        start();
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            displayMessage("Could not upload score!");
        }
    }

    /**
     * Send request to store high score online.
     */
    @Override
    public void run() {
        super.run();
        try {
            String query = String.format("name=%s&score=%s", URLEncoder.encode(name, "UTF-8"), URLEncoder.encode(String.valueOf(highScore), "UTF-8"));
            URL url = new URL(Score_URL + "?" + query);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK) {
                displayMessage("Uploaded high score!");
            } else {
                displayMessage("Request failed with code: " + status);
            }
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            displayMessage("Could not upload score!");
            try {
                this.join();
            } catch (InterruptedException ex) {
                e.printStackTrace();
                displayMessage("Could not upload score!");
            }
        }
    }


    /**
     * Displays message as toast
     */
    public void displayMessage(String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
