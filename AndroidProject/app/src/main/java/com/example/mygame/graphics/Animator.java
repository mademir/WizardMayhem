package com.example.mygame.graphics;

import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.mygame.DisplayArea;
import com.example.mygame.entitites.Entity;
import com.example.mygame.entitites.EntityState;

/**
 * Animator class is to simulate Entity animations.
 */
public class Animator {
    public Entity entity = null;
    private Sprite[][] spriteArrays;
    private int updateDelay;
    private static final int MAX_UPDATE_DELAY = 5;
    private int idleFrameCt = 0;
    private int runFrameCt = 0;
    private int attackFrameCt = 1;
    private float deadFrameCt = 0;

    /**
     * Iterates through the given sprite arrays to simulate animation.
     * @param spriteArrays frames
     */
    public Animator(Sprite[][] spriteArrays) {
        this.spriteArrays = spriteArrays;
    }

    /**
     * Draws the next frame on the canvas.
     * @param canvas canvas to draw on to
     * @param displayArea the display area
     */
    public void draw(Canvas canvas, DisplayArea displayArea, EntityState state, boolean direction) {
        boolean shouldSkip = --updateDelay > 0;
        Sprite sprite = spriteArrays[EntityState.toInt(state)][getFrameCt(state, shouldSkip)];

        sprite.draw(
                canvas,
                (int) (displayArea.xToDisplay(entity.getX())),
                (int) (displayArea.yToDisplay(entity.getY())),
                !direction
        );

    }

    /**
     * Return the index for the next frame for the given animation
     * @param state animation type
     * @return index for the next frame
     */
    private int getFrameCt(EntityState state, boolean shouldSkip) {
        switch (state) {
            case IDLE:
                if (!shouldSkip){
                    if (idleFrameCt < SpriteSheet.MAX_FRAMES - 1) {
                        idleFrameCt++;
                    }
                    else {
                        idleFrameCt = 0;
                    }
                    updateDelay = MAX_UPDATE_DELAY; // Reset delay
                }
                // Reset other animations
                runFrameCt = 0;
                attackFrameCt = 1;
                return idleFrameCt;
            case RUNNING:
                if (!shouldSkip) {
                    if (runFrameCt < SpriteSheet.MAX_FRAMES - 1) {
                        runFrameCt++;
                    } else {
                        runFrameCt = 0;
                    }
                    updateDelay = MAX_UPDATE_DELAY; // Reset delay
                }
                // Reset other animations
                idleFrameCt = 0;
                attackFrameCt = 1;
                return runFrameCt;
            case ATTACKING:
                if (!shouldSkip) {
                    if (attackFrameCt < SpriteSheet.MAX_FRAMES - 5) {
                        attackFrameCt++;
                    } else {
                        attackFrameCt = 1;
                    }
                    updateDelay = MAX_UPDATE_DELAY; // Reset delay
                }
                // Reset other animations
                runFrameCt = 0;
                idleFrameCt = 0;
                return attackFrameCt;
            case DEAD:
                if (!shouldSkip) {
                    if (deadFrameCt < SpriteSheet.MAX_FRAMES - 2) deadFrameCt+=0.2;
                }
                return (int) deadFrameCt;
            default:
                return 0;
        }
    }

}