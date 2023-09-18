package com.example.mygame.entitites;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.example.mygame.DisplayArea;
import com.example.mygame.GameLoop;
import com.example.mygame.R;

import java.util.Vector;

/**
 * Spells are cast by a character.
 * They travel in a limited distance and reduce enemy health at hit.
 */
public class Spell extends Entity {
    public static final double SPEED_PIXELS_PER_SECOND = 1500.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private final double velocityX;
    private final double velocityY;

    /**
     * Construct a spell from the spell caster in the given direction.
     * @param context caller context
     * @param spellcaster caster of the spell
     * @param spellDirection direction of the spell
     */
    public Spell(Context context, Character spellcaster, Vector<Double> spellDirection) {
        super(
                context,
                spellcaster.getX() + ((!spellcaster.direction) ? 1 : -1)*80,
                spellcaster.getY()-100,
                30,
                30
        );
        velocityX = spellDirection.get(0)*MAX_SPEED;
        velocityY = spellDirection.get(1)*MAX_SPEED;

        // Set spell color
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.spell));
    }

    /**
     * Check if the spell is colliding with the given entity
     * @param entity entity to check
     * @return boolean representing is colliding
     */
    public boolean isColliding(Entity entity) {
        int enemyPaddingX = 100;
        int enemyPaddingY = 200;
        Rect sr = new Rect((int) x, (int) y, (int) w, (int) h);
        Rect er = new Rect(
                (int) entity.x - enemyPaddingX,
                (int) entity.y - enemyPaddingY,
                (int) entity.w + enemyPaddingX*2,
                (int) entity.h + enemyPaddingY*2
        );
        return sr.intersect(er);
    }

    /**
     * Update method of the spell that makes it move in
     * its direction within specified distance limit.
     */
    @Override
    public void update() {
        x += velocityX;
        y += velocityY;
        paint.setColor(Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
        //sin wave size and rgb
    }

    /**
     * Draw the spell.
     * @param canvas canvas to draw on
     * @param displayArea display area of the game
     */
    public void draw(Canvas canvas, DisplayArea displayArea) {
        canvas.drawCircle(
                (float) displayArea.xToDisplay(x),
                (float) displayArea.yToDisplay(y),
                (float) w,
                paint
        );
    }
}
