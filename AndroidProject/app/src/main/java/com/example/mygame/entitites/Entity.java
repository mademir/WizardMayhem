package com.example.mygame.entitites;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.mygame.DisplayArea;
import com.example.mygame.R;

/**
 * Main entity class that represents all entities.
 */
public abstract class Entity {
    private final Context context;
    protected double x;
    protected double y;
    protected double w;
    protected double h;
    protected Paint paint;
    protected EntityState state = EntityState.IDLE;
    public int MAX_HP;
    protected int hp;

    /**
     * Creates an entity with the provided location and size.
     * @param context caller context
     * @param x x pos of entity
     * @param y y pos of entity
     * @param w width of the entity
     * @param h height of the entity
     */
    public Entity(Context context, double x, double y, double w, double h) {
        this.context = context;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.player));
    }

    /**
     * Gets the distance between 2 entities
     * @param obj1 entity 1
     * @param obj2 entity 2
     * @return the distance in double
     */
    protected static double getDistanceBetweenObjects(Entity obj1, Entity obj2) {
        return Math.sqrt(Math.pow(obj2.getX() - obj1.getX(), 2) + Math.pow(obj2.getY() - obj1.getY(), 2));
    }

    /**
     * Checks if 2 entities are colliding
     * @param obj1 entity 1
     * @param obj2 entity 2
     * @return boolean representing is colliding
     */
    public static boolean isColliding(Entity obj1, Entity obj2) {
        double distance = getDistanceBetweenObjects(obj1, obj2);
        double distanceToCollision = obj1.w + obj2.w;
        return (distance < distanceToCollision);
    }

    /**
     * Generic draw method for the entities that draws a circle.
     * @param canvas canvas to draw on
     * @param displayArea display area of the game
     */
    public void draw(Canvas canvas, DisplayArea displayArea) {
        canvas.drawCircle(
                displayArea.xToDisplay(x),
                displayArea.yToDisplay(y),
                (float) w, paint);
    }

    /**
     * Abstract update function all entities must have
     */
    public abstract void update();

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    public EntityState getState() {
        return state;
    }
    public void setState(EntityState state) {
        this.state = state;
    }

    public int getHP() {
        return hp;
    }
    public int setHP(int hp) {
        return this.hp = hp;
    }

}
