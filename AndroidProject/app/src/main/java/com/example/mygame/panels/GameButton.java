package com.example.mygame.panels;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.mygame.R;

/**
 * Simulates buttons to interact during the game.
 */
public class GameButton {
    private final int BUTTON_PIXEL_SIZE = 128;
    private final double x;
    private final double y;
    private final double radius;
    private final Bitmap bitmap;

    /**
     * A button that can be used during a game with draw and pressed check functionality.
     * @param bitmap button image
     * @param x x position of button
     * @param y y position of button
     * @param radius radius of the button
     */
    public GameButton(Bitmap bitmap, double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.bitmap = bitmap;
    }

    /**
     * Draw this button on the canvas.
     * @param canvas canvas to draw on
     */
    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap,
                new Rect(0, 0, BUTTON_PIXEL_SIZE, BUTTON_PIXEL_SIZE),
                new Rect((int) (x-radius), (int) (y-radius), (int) (x+radius), (int) (y+radius)),
                null
        );
    }

    /**
     * Check if button is pressed.
     * @param x x position of the touch
     * @param y y position of the touch
     * @return boolean stating if button is pressed
     */
    public boolean isPressed(double x, double y){
        double distance = Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
        return distance <= radius;
    }

    /**
     * Recycle the memory used by the bitmap image for this button.
     */
    public void recycleMemory() {
        bitmap.recycle();
    }
}
