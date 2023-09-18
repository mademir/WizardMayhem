package com.example.mygame.panels;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.mygame.DisplayArea;

/**
 * This class is to simulate a joystick to control the player.
 */
public class Joystick {

    private int outerX;
    private int outerY;
    public int innerX;
    public int innerY;

    private int outerRad;
    private int innerRad;

    private Paint innerPaint;
    private Paint outerPaint;
    private boolean isPressed = false;
    private double touchDistToCentre;
    private double actuatorX;
    private double actuatorY;

    /**
     * Constructs a joystick to control the player.
     * @param x x pos of the joystick on the screen
     * @param y y pos of the joystick on the screen
     * @param outerRad radius for the outer circle
     * @param innerRad radius for the inner circle
     */
    public Joystick(int x, int y, int outerRad, int innerRad) {

        // Set centres for inner and outer circles of the joystick
        outerX = x;
        outerY = y;
        innerX = x;
        innerY = y;

        // Set circle radii
        this.outerRad = outerRad;
        this.innerRad = innerRad;

        // Set circle paints
        innerPaint = new Paint();
        innerPaint.setColor(Color.LTGRAY);
        innerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        outerPaint = new Paint();
        outerPaint.setColor(Color.CYAN);
        outerPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * Display joystick on the canvas.
     * @param canvas
     */
    public void draw(Canvas canvas, DisplayArea displayArea) {
        // Display outer circle
        canvas.drawCircle(
                outerX, outerY,
                outerRad, outerPaint
        );

        // Display inner circle
        canvas.drawCircle(
                innerX, innerY,
                innerRad, innerPaint
        );
    }

    /**
     * Updates the joystick circle position
     */
    public void update() {
        updateInnerCircle();
    }

    /**
     * Update the position of the inner circle.
     */
    private void updateInnerCircle() {
        innerX = (int) (outerX + actuatorX* outerRad);
        innerY = (int) (outerY + actuatorY* outerRad);
    }

    /**
     * Set the position of the actuator according to the touch position.
     * Limit the actuator position to the outer circle area.
     * @param touchX
     * @param touchY
     */
    public void setActuator(double touchX, double touchY) {
        // Calculate deltas
        double dx = touchX - outerX;
        double dy = touchY - outerY;
        double deltaDist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        if(deltaDist < outerRad) {
            actuatorX = dx/ outerRad;
            actuatorY = dy/ outerRad;
        } else {
            actuatorX = dx/deltaDist;
            actuatorY = dy/deltaDist;
        }
    }

    /**
     * Checks the touch position to see if it is in the outer circle area.
     * @param touchX
     * @param touchY
     * @return true if pressed, false otherwise
     */
    public boolean isPressed(double touchX, double touchY) {
        touchDistToCentre = Math.sqrt(Math.pow(outerX - touchX, 2) + Math.pow(outerY - touchY, 2));
        return touchDistToCentre < outerRad;
    }

    /**
     * Resets the actuator's position.
     */
    public void resetActuator() {
        actuatorX = 0;
        actuatorY = 0;
    }

    // Getters and setters

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public double getActuatorX() {
        return actuatorX;
    }

    public double getActuatorY() {
        return actuatorY;
    }
}
