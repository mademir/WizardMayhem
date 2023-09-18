package com.example.mygame.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.mygame.DisplayArea;
import com.example.mygame.R;
import com.example.mygame.entitites.Entity;
import com.example.mygame.entitites.Player;

/**
 * This class is to display the character health below them
 */
public class HealthBar {
    private final int healthTypeColor;
    private Entity player;
    private Paint borderPaint, healthPaint;
    private int width, height, margin; // pixel value

    /**
     * Constructs a health bar for the entity.
     * Sets the color according to the type of the entity.
     * @param context caller context
     * @param player the entity the health bar is for
     * @param healthTypeColor color for the type of the entity
     */
    public HealthBar(Context context, Entity player, int healthTypeColor) {
        this.player = player;
        this.width = 100;
        this.height = 20;
        this.margin = 2;
        this.healthTypeColor = healthTypeColor;

        this.borderPaint = new Paint();
        int borderColor = ContextCompat.getColor(context, R.color.healthBarBorder);
        borderPaint.setColor(borderColor);

        this.healthPaint = new Paint();
        int healthColor = ContextCompat.getColor(context, healthTypeColor);
        healthPaint.setColor(healthColor);
    }

    /**
     * Draw the health bar with its current values
     * @param canvas canvas to draw on
     * @param gameDisplay display area of the game
     */
    public void draw(Canvas canvas, DisplayArea gameDisplay) {
        float x = (float) player.getX();
        float y = (float) player.getY();
        float distanceToPlayer = -160;
        float healthPointPercentage = (float) player.getHP()/player.MAX_HP;

        // Draw border
        float borderLeft, borderTop, borderRight, borderBottom;
        borderLeft = x - width/2;
        borderRight = x + width/2;
        borderBottom = y - distanceToPlayer;
        borderTop = borderBottom - height;
        canvas.drawRect(
                (float) gameDisplay.xToDisplay(borderLeft),
                (float) gameDisplay.yToDisplay(borderTop),
                (float) gameDisplay.xToDisplay(borderRight),
                (float) gameDisplay.yToDisplay(borderBottom),
                borderPaint);

        // Draw health
        float healthLeft, healthTop, healthRight, healthBottom, healthWidth, healthHeight;
        healthWidth = width - 2*margin;
        healthHeight = height - 2*margin;
        healthLeft = borderLeft + margin;
        healthRight = healthLeft + healthWidth*healthPointPercentage;
        healthBottom = borderBottom - margin;
        healthTop = healthBottom - healthHeight;
        canvas.drawRect(
                (float) gameDisplay.xToDisplay(healthLeft),
                (float) gameDisplay.yToDisplay(healthTop),
                (float) gameDisplay.xToDisplay(healthRight),
                (float) gameDisplay.yToDisplay(healthBottom),
                healthPaint);
    }
}
