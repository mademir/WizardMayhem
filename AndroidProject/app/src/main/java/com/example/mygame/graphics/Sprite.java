package com.example.mygame.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.nio.file.attribute.UserPrincipal;

/**
 * Sprite class to handle each frame in a sprite sheet.
 */
public class Sprite {

    private final SpriteSheet sheet;
    private final Rect rect;

    public Sprite(SpriteSheet spriteSheet, Rect rect) {
        this.sheet = spriteSheet;
        this.rect = rect;
    }

    public int getWidth() {
        return rect.width();
    }
    public int getHeight() {
        return rect.height();
    }

    /**
     * Draws sprite to canvas centred at x, y
     * @param canvas canvas to draw on
     * @param x x position to centre sprite at
     * @param y y position to centre sprite at
     * @param flip whether to flip the image
     */
    public void draw(Canvas canvas, int x, int y, boolean flip) {
        // Create a new matrix and set it to flip horizontally
        Matrix flipMatrix = new Matrix();
        flipMatrix.setScale(-1, 1);
        if (flip) {
            // Apply the matrix transformation to the canvas
            canvas.concat(flipMatrix);
            x = SpriteSheet.SPRITE_W - (x + getWidth());
        }
        canvas.drawBitmap(
                sheet.getBitmap(), rect,
                new Rect(x-getWidth()/2, y-getHeight()/2, x+getWidth()/2, y+getHeight()/2),
                null
        );
        if (flip) canvas.concat(flipMatrix);    // Revert flip
    }

    /**
     * Draws sprite to canvas centred at x, y
     * @param canvas canvas to draw on
     * @param x x position to centre sprite at
     * @param y y position to centre sprite at
     */
    public void draw(Canvas canvas, int x, int y) {
        draw(canvas, x, y, false);
    }
}
