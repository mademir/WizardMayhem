package com.example.mygame.entitites;

import android.content.Context;

import com.example.mygame.GameLoop;
import com.example.mygame.R;
import com.example.mygame.graphics.Animator;
import com.example.mygame.panels.HealthBar;
import com.example.mygame.panels.Joystick;

/**
 * Player of the game that is controlled by the user.
 * Player can cast spells and is tracked by the enemies.
 */
public class Player extends Character{
    public static final double SPEED_PIXELS_PER_SECOND = 450;
    public static final int MAX_ATTACK_FOR_UPDATES = 25;
    private static final double SPEED_MULTIPLIER = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private final Joystick joystick;
    public int attackForUpdates = 0;

    /**
     * Constructs the player with the given attributes for location and size.
     * Also connects the animator for the player as well as the joystick.
     * @param context caller context
     * @param joystick joystick that controls the player
     * @param x x pos of player
     * @param y y pos of player
     * @param w width of the player
     * @param h height of the player
     * @param animator animator of the player
     * @param playerHP player health points
     */
    public Player(Context context, Joystick joystick, double x, double y, double w, double h, Animator animator, int playerHP) {
        super(context, x, y, w, h);
        this.joystick = joystick;
        this.animator = animator;
        animator.entity = this;
        hp = MAX_HP = playerHP;
        this.healthBar = new HealthBar(context, this, R.color.healthBarHealthPlayer);
    }

    /**
     * Update player attributes according to the joystick info.
     */
    @Override
    public void update() {
        xSpeed = joystick.getActuatorX() * SPEED_MULTIPLIER;
        ySpeed = joystick.getActuatorY() * SPEED_MULTIPLIER;
        x += xSpeed;
        y += ySpeed;

        if (xSpeed != 0) direction = xSpeed > 0;

        // Update enemy state
        if (getState() != EntityState.DEAD) {
            if (attackForUpdates-- > 0) {
                state = EntityState.ATTACKING;
            } else {
                // Set state
                if (xSpeed == 0 && ySpeed == 0) {
                    state = EntityState.IDLE;
                } else {
                    state = EntityState.RUNNING;
                }
            }
        }

        update_directions();
    }
}
