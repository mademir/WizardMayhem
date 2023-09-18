package com.example.mygame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Game loop to run the main loop of the game on a separate thread.
 */
public class GameLoop extends Thread{
    public static final double MAX_UPS = 60.0;
    private static final double UPS_FREQUENCY = 1E+3/MAX_UPS;
    private double avgUPS;
    private double avgFPS;
    private boolean isRunning = false;
    private SurfaceHolder surfaceHolder;
    private Game game;

    /**
     * Initialise a game loop ti run the main loop of the game on a separate thread.
     * @param game the game of the game loop
     * @param surfaceHolder surface holder to enable drawing
     */
    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
        this.game = game;
        this.surfaceHolder = surfaceHolder;
    }

    public double getAvgFPS() {
        return avgFPS;
    }

    public double getAvgUPS() {
        return avgUPS;
    }

    /**
     * Starts the game loop for the game.
     */
    public void startLoop() {
        isRunning = true;
        start();
    }

    /**
     * Stops the game loop of the game and waits
     * for the thread to finish.
     */
    public void stopLoop() {
        isRunning = false;

        // Wait for thread join

        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            game.displayMessage("Something went wrong!"); //Feedback to user instead of crashing
        }
    }

    /**
     * Run method of the game loop thread. Calls update and draw.
     * Calculates FPS and UPS to keep them within their specified target values.
     */
    @Override
    public void run() {
        super.run();
        Canvas canvas = null;

        // Declare variables to keep track of time, update and frames
        int updateCt = 0;
        int frameCt = 0;
        long startT;
        long sleepT;
        long elapsedT;

        startT = System.currentTimeMillis(); // Save the start time

        // Actual game loop
        while(isRunning) {
            // Update and render
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    game.update();
                    updateCt++;

                    game.draw(canvas);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                game.displayMessage("Something went wrong!"); //Feedback to user instead of crashing
            } finally {
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCt++;
                    } catch(Exception e) {
                        e.printStackTrace();
                        game.displayMessage("Something went wrong!"); //Feedback to user instead of crashing
                    }
                }
            }

            // Sleep if going over max UPS
            elapsedT = System.currentTimeMillis() - startT;
            sleepT = (long) (updateCt* UPS_FREQUENCY - elapsedT);
            if(sleepT > 0) {
                try {
                    sleep(sleepT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    game.displayMessage("Something went wrong!"); //Feedback to user instead of crashing
                }
            }

            // Skip frames to keep up with target UPS
            while(sleepT < 0 && updateCt < MAX_UPS-1) {
                game.update();
                updateCt++;
                elapsedT = System.currentTimeMillis() - startT;
                sleepT = (long) (updateCt* UPS_FREQUENCY - elapsedT);
            }

            // Calculate average UPS and FPS
            elapsedT = System.currentTimeMillis() - startT;
            if(elapsedT >= 1000) {
                avgUPS = updateCt / (elapsedT/1000);
                avgFPS = frameCt / (elapsedT/1000);
                updateCt = 0;
                frameCt = 0;
                startT = System.currentTimeMillis();
            }
        }
    }
}
