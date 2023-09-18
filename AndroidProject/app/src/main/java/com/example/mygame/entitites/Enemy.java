package com.example.mygame.entitites;

import android.content.Context;

import com.example.mygame.GameLoop;
import com.example.mygame.R;
import com.example.mygame.graphics.Animator;
import com.example.mygame.panels.HealthBar;

/**
 * Enemy is a character which always moves in the direction of the player.
 * The Enemy class is an extension of a Circle, which is an extension of a GameObject
 */
public class Enemy extends Character {

    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND*0.5;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private Player player;

    /**
     * Enemy is an overload constructor used for spawning enemies in random locations
     *
     * @param context caller context
     * @param player Player this enemy is the enemy of
     * @param hp health points
     */
    public Enemy(Context context, Player player, Animator animator, int hp) {
        super(
                context,
                100 + Math.random()*3000,
                100 + Math.random()*3000,
                100,
                200

        );
        this.player = player;
        this.animator = animator;
        animator.entity = this;
        this.hp = MAX_HP = hp;
        this.healthBar = new HealthBar(context, this, R.color.healthBarHealthEnemy);
    }

    /**
     * Updates the enemy attributes to follow the player.
     */
    public void update() {
        // Calculate vector from enemy to player (in x and y)
        double distanceToPlayerX = player.getX() - x;
        double distanceToPlayerY = player.getY() - y;

        // Calculate (absolute) distance between enemy (this) and player
        double distanceToPlayer = Entity.getDistanceBetweenObjects(this, player);

        // Calculate direction from enemy to player
        double directionX = distanceToPlayerX/distanceToPlayer;
        double directionY = distanceToPlayerY/distanceToPlayer;

        // Set velocity in the direction to the player
        if(distanceToPlayer > 0) { // Avoid division by zero
            xSpeed = directionX*(MAX_SPEED+(MAX_HP/2.0));
            ySpeed = directionY*(MAX_SPEED+(MAX_HP/2.0));
        } else {
            xSpeed = 0;
            ySpeed = 0;
        }

        x += xSpeed;
        y += ySpeed;

        if (xSpeed != 0) direction = xSpeed > 0;
        update_directions();
    }
}

