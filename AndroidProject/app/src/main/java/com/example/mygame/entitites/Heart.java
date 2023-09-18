package com.example.mygame.entitites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.mygame.DisplayArea;

/**
 * A collectable heart entity to regenerate health
 */
public class Heart extends Entity{
    private final Bitmap bitmap;
    private final DisplayArea displayArea;

    private long sinCt = 0;

    /**
     * Creates a collectable heart of type entity.
     * @param context caller context
     * @param bitmap image for the heart
     */
    public Heart(Context context, DisplayArea displayArea, Bitmap bitmap) {
        super(
                context,
                100 + Math.random()*3000,
                100 + Math.random()*3000,
                50,
                50
        );
        this.bitmap = bitmap;
        this.displayArea = displayArea;
    }

    /**
     * Draws the heart on the canvas.
     * @param canvas canvas to draw on
     */
    public void draw(Canvas canvas) {
        float nx = displayArea.xToDisplay(x);
        float ny = displayArea.yToDisplay(y);
        canvas.drawBitmap(bitmap,
                new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                new Rect((int) (nx-w), (int) (ny-h), (int) (nx+w), (int) (ny+h)),
                null
        );
    }

    /**
     * Moves the heart in a sine wave motion
     */
    @Override
    public void update() {
        sinCt++;
        y += (Math.sin(sinCt/20.0));
    }
}
