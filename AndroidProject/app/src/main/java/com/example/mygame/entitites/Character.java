package com.example.mygame.entitites;

import android.content.Context;
import android.graphics.Canvas;

import com.example.mygame.DisplayArea;
import com.example.mygame.graphics.Animator;
import com.example.mygame.panels.HealthBar;

public abstract class Character extends Entity {

    protected Animator animator;
    protected HealthBar healthBar;
    protected double xSpeed, ySpeed = 0.0;
    protected boolean direction = true; // true for right, false for left
    public double directionX = 1.0;
    public double directionY = 0;

    /**
     * Constructs a character with the given location and size.
     * @param context caller context
     * @param x x pos of character
     * @param y y pos of character
     * @param w width of the character
     * @param h height of the character
     */
    public Character(Context context, double x, double y, double w, double h) {
        super(context, x, y, w, h);
    }

    /**
     * Generic draw method for characters.
     * Draws the animators and health bars of characters.
     * @param canvas Canvas to draw on
     * @param displayArea display area of the game
     */
    public void draw(Canvas canvas, DisplayArea displayArea) {
        animator.draw(canvas, displayArea, getState(), direction);
        healthBar.draw(canvas, displayArea);
    }

    /**
     * Updates direction of the characters
     */
    protected void update_directions() {
        // Update direction
        if (xSpeed != 0 || ySpeed != 0) {
            // Normalize velocity to get direction (unit vector of velocity)
            double distance = Math.sqrt(Math.pow(0 - xSpeed, 2) + Math.pow(0 - ySpeed, 2));
            directionX = xSpeed/distance;
            directionY = ySpeed/distance;
        }
    }

}
