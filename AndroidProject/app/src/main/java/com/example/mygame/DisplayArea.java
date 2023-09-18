package com.example.mygame;

import android.graphics.Rect;

import com.example.mygame.entitites.Entity;

/**
 * Simulate the area outside the screen.
 */
public class DisplayArea {
    public final Rect DISPLAY_RECT;
    private final int wPixels;
    private final int hPixels;
    private double xOffset;
    private double yOffset;
    private double displayCentreX;
    private double gameCentreX;
    private double displayCentreY;
    private double gameCentreY;
    private Entity centredEntity;

    /**
     * Constructs a display area to simulate the display outside the screen.
     * @param centredEntity the entity to be centred on the screen.
     * @param wPixels width of the screen
     * @param hPixels height of the screen
     */
    public DisplayArea(Entity centredEntity, int wPixels, int hPixels) {
        this.centredEntity = centredEntity;
        displayCentreX = wPixels/2.0;
        displayCentreY = hPixels/2.0;
        this.wPixels = wPixels;
        this.hPixels = hPixels;
        DISPLAY_RECT = new Rect(0, 0, wPixels, hPixels);
    }

    /**
     * Take x and convert it to the x for
     * display area where an entity is centred.
     * @param x x value to be converted
     * @return converted x
     */
    public float xToDisplay(double x) {
        return (float) (x + xOffset);
    }

    /**
     * Take y and convert it to the y for
     * display area where an entity is centred.
     * @param y y value to be converted
     * @return converted y
     */
    public float yToDisplay(double y) {
        return (float) (y + yOffset);
    }

    public Rect getGameRect() {
        return new Rect(
                (int) (gameCentreX - wPixels/2),
                (int) (gameCentreY - hPixels/2),
                (int) (gameCentreX + wPixels/2),
                (int) (gameCentreY + hPixels/2)
        );
    }

    /**
     * Update the offsets needed to keep
     * an entity centred in the display area.
     */
    public void update() {
        gameCentreX = centredEntity.getX();
        gameCentreY = centredEntity.getY();
        xOffset = displayCentreX - gameCentreX;
        yOffset = displayCentreY - gameCentreY;
    }
}
