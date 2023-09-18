package com.example.mygame;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mygame.GameActivity.HIGH_SCORE;
import static com.example.mygame.GameActivity.SHARED_PREFS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.mygame.entitites.Enemy;
import com.example.mygame.entitites.Entity;
import com.example.mygame.entitites.EntityState;
import com.example.mygame.entitites.Heart;
import com.example.mygame.entitites.Player;
import com.example.mygame.entitites.Spell;
import com.example.mygame.graphics.Animator;
import com.example.mygame.graphics.Sprite;
import com.example.mygame.graphics.SpriteSheet;
import com.example.mygame.map.Tilemap;
import com.example.mygame.panels.GameButton;
import com.example.mygame.panels.GameOver;
import com.example.mygame.panels.Joystick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Game class manages the objects inside the game, updates their states and renders them.
 */
public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private static final int SPELL_DISTANCE = 550;
    private final boolean[] customEnemyToggle;
    private final int customEnemyHP;
    private final List<Heart> heartsList;
    private final int heartsCt;
    private double UPDATES_PER_SPAWN;
    private double updatesUntilNextSpawn = 30;
    private final Player player;
    private final GameOver gameOver;
    private final GameButton btnBack;
    private final ArrayList<Sprite[][]> enemySpriteList;
    private final int level;
    private GameLoop gameLoop;
    private Context context;
    private Activity activity;
    private Joystick joystick;
    private DisplayArea displayArea;
    private Tilemap tilemap;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private int spellCt;
    private int joystickID = 0;
    private List<Spell> spells = new ArrayList<Spell>();
    private float lastTouchX;
    private float lastTouchY;
    private Vector<Double> spellDirection;
    private int score;
    private boolean isGameOver = false;
    private int highScore;
    private boolean recycling = false;

    /**
     * Main game object that manages the overall game. Creates a loop to continuously call
     * draw and update methods to manage the entities in the game and display them. The game can have
     * different levels. The game dynamic and logic changes according to its level.
     * @param context context of the calling activity
     * @param activity the calling activity
     * @param highScore high score of the player
     * @param level current level of the game
     * @param customMapNo map number if the level is custom
     * @param customEnemyHP enemy HP  if the level is custom
     * @param customPlayerHP player HP if the level is custom
     * @param customEnemyToggle if the level is custom, bool array of which enemies to include
     */
    public Game(Context context, Activity activity, int highScore, int level, int customMapNo, int customEnemyHP, int customPlayerHP, boolean[] customEnemyToggle) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.highScore = highScore;
        this.level = level;

        this.customEnemyHP = customEnemyHP;
        this.customEnemyToggle = customEnemyToggle;

        // Get the surface holder and add a callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        setFocusable(true);

        joystick = new Joystick(1900, 800, 100, 50);

        // Initialise player animation sprites
        Sprite[][] playerSprites = new Sprite[4][SpriteSheet.MAX_FRAMES];
        playerSprites[0] = new SpriteSheet(context, R.drawable.player_idle).getSprites();
        playerSprites[1] = new SpriteSheet(context, R.drawable.player_run).getSprites();
        playerSprites[2] = new SpriteSheet(context, R.drawable.player_attack).getSprites();
        playerSprites[3] = new SpriteSheet(context, R.drawable.player_dead).getSprites();
        Animator playerAnimation = new Animator(playerSprites);

        // Initialise player
        int playerHP = 10;
        if (level <= 3) playerHP = (int) (10/level);
        else playerHP = customPlayerHP;
        player = new Player(context, joystick, 1800, 1800, 75, 150, playerAnimation, playerHP);

        // Initialise enemy animators
        enemySpriteList = new ArrayList<Sprite [][]>(3);
        Sprite[][] enemy1sprites = new Sprite[1][SpriteSheet.MAX_FRAMES];
        enemy1sprites[0] = new SpriteSheet(context, R.drawable.enemy1).getSprites();
        enemySpriteList.add(0, enemy1sprites);

        Sprite[][] enemy2sprites = new Sprite[1][SpriteSheet.MAX_FRAMES];
        enemy2sprites[0] = new SpriteSheet(context, R.drawable.enemy2).getSprites();
        enemySpriteList.add(1, enemy2sprites);

        Sprite[][] enemy3sprites = new Sprite[1][SpriteSheet.MAX_FRAMES];
        enemy3sprites[0] = new SpriteSheet(context, R.drawable.enemy3).getSprites();
        enemySpriteList.add(2, enemy3sprites);


        // Set the display area to centre the player on the screen
        DisplayMetrics displayMeasures = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMeasures);
        displayArea = new DisplayArea(player, displayMeasures.widthPixels, displayMeasures.heightPixels);

        tilemap = new Tilemap(context, level, customMapNo); // Initialize Tilemap

        // Initialize the vector with 0s
        spellDirection = new Vector<Double>(2);
        spellDirection.add(0, 0.0);
        spellDirection.add(1, 0.0);

        score = 0; // Initialise score

        gameOver = new GameOver(context, displayArea); // Initialise game over object

        // Initialise back button
        BitmapFactory.Options bitmapOpts = new BitmapFactory.Options();
        bitmapOpts.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_back, bitmapOpts);
        btnBack = new GameButton(bitmap, 80, 80, 50);

        // Calculate enemy spawn frequency
        double spawnsPerSeconds = 0.3;
        if (level <= 3) spawnsPerSeconds *= level;
        UPDATES_PER_SPAWN = GameLoop.MAX_UPS/spawnsPerSeconds;

        // Initialise collectable hearts
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart, bitmapOpts);
        heartsCt = 2 + (level * 2);
        heartsList = new ArrayList<Heart>(heartsCt);
        for (int i = 0; i < heartsCt; i++)
            heartsList.add(i, new Heart(context, displayArea, bitmap));
    }

    /**
     * Display the calculated average frames per second on the canvas.
     * @param canvas Canvas to display on
     */
    public void displayFPS(Canvas canvas) {
        String avgFPS = Double.toString(gameLoop.getAvgFPS());
        Paint paint = new Paint();
        paint.setTextSize(25);
        int color = ContextCompat.getColor(context, R.color.orange);
        paint.setColor(color);
        canvas.drawText("FPS: " + avgFPS, 150, 70, paint);
    }

    /**
     * Display the calculated average updates per second on the canvas.
     * @param canvas Canvas to display on
     */
    public void displayUPS(Canvas canvas) {
        String avgUPS = Double.toString(gameLoop.getAvgUPS());
        Paint paint = new Paint();
        paint.setTextSize(25);
        int color = ContextCompat.getColor(context, R.color.orange);
        paint.setColor(color);
        canvas.drawText("UPS: " + avgUPS, 150, 110, paint);
    }

    /**
     * Display the current score on the canvas.
     * @param canvas Canvas to display on
     */
    public void displayScore(Canvas canvas) {
        String scoreStr = Integer.toString(score);
        Paint paint = new Paint();
        paint.setTextSize(80);
        int color = ContextCompat.getColor(context, R.color.score);
        paint.setColor(color);
        canvas.drawText("Score: " + scoreStr, displayArea.DISPLAY_RECT.centerX() - 150, 100, paint);
    }

    /**
     * @param surfaceHolder
     */
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        if (gameLoop.getState().equals(Thread.State.TERMINATED))
            gameLoop = new GameLoop(this, surfaceHolder);
        gameLoop.startLoop();
    }

    /**
     * @param surfaceHolder
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    /**
     * @param surfaceHolder
     */
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    /**
     * Displays message as toast
     * @param message message to display
     */
    public void displayMessage(String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Handle touch events.
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Handle user input touch event actions
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick.getIsPressed()) {
                    // Joystick was pressed before this event -> cast spell
                    lastTouchX = event.getX(event.getActionIndex());
                    lastTouchY = event.getY(event.getActionIndex());
                    spellCt++;
                    player.attackForUpdates = Player.MAX_ATTACK_FOR_UPDATES;
                } else if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                    joystickID = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                } else {
                    // Joystick was not previously, and is not pressed in this event -> cast spell
                    lastTouchX = event.getX(event.getActionIndex());
                    lastTouchY = event.getY(event.getActionIndex());
                    spellCt++;
                    player.attackForUpdates = Player.MAX_ATTACK_FOR_UPDATES;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (joystick.getIsPressed()) {
                    // Joystick was pressed previously and is now moved
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (joystickID == event.getPointerId(event.getActionIndex())) {
                    // joystick pointer was let go off -> setIsPressed(false) and resetActuator()
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Draw entities and other information to canvas.
     * @param canvas Canvas to display on
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (recycling) return; // Skip drawing if recycling images

        // Draw Tilemap
        tilemap.draw(canvas, displayArea);
        displayFPS(canvas);
        displayUPS(canvas);

        for (Heart heart : heartsList) {
            heart.draw(canvas);
        }

        // Draw spells
        for (Spell spell : spells) {
            spell.draw(canvas, displayArea);
        }

        // Draw enemies
        for (Enemy enemy : enemyList) {
            enemy.draw(canvas, displayArea);
        }

        player.draw(canvas, displayArea);

        displayScore(canvas);

        if (isGameOver) {
            gameOver.draw(canvas, highScore, score);
            return;
        }

        joystick.draw(canvas, displayArea);

        btnBack.draw(canvas);
    }

    /**
     * Update entities and information.
     */
    public void update() {
        // Stop updates after game over
        if (isGameOver) {
            // Check if pressed game over menu buttons
            if (gameOver.btnBack.isPressed(lastTouchX, lastTouchY)) {
                optimiseMemory();
                activity.onBackPressed();
            }
            if (gameOver.btnRestart.isPressed(lastTouchX, lastTouchY)) {
                optimiseMemory();
                Intent intent = activity.getIntent();
                activity.finish();
                activity.startActivity(intent);
            }

            return;
        }

        // Check if back button pressed
        if (btnBack.isPressed(lastTouchX, lastTouchY)) activity.onBackPressed();

        if (player.getHP() <= 0){
            onGameOver();
            isGameOver = true;
        }

        player.update();
        joystick.update();
        displayArea.update();

        // Cast spells
        while (spellCt > 0) {
            spellDirection.set(0, (double) (lastTouchX - displayArea.xToDisplay(player.getX())));
            spellDirection.set(1, (double) (lastTouchY - displayArea.yToDisplay(player.getY())));

            // Normalize the vector
            double magnitude = Math.sqrt(spellDirection.get(0) * spellDirection.get(0) + spellDirection.get(1) * spellDirection.get(1));
            spellDirection.set(0, spellDirection.get(0) / magnitude);
            spellDirection.set(1, spellDirection.get(1) / magnitude);

            spells.add(new Spell(getContext(), player, spellDirection));
            spellCt --;
        }

        // Update spells
        for (Spell spell : spells) {
            spell.update();
        }

        // Destroy away spells
        Iterator<Spell> iteratorSpells = spells.iterator();
        while (iteratorSpells.hasNext()) {
            Spell s = iteratorSpells.next();
            if (Math.sqrt(Math.pow(s.getX() - player.getX(), 2) + Math.pow(s.getY() - player.getY(), 2)) > SPELL_DISTANCE) {
                iteratorSpells.remove();
            }
        }

        // If ready, spawn enemy
        if(readyToSpawn()) {
            List<Integer> enemyOptions = new ArrayList<Integer>();
            if (level <= 3) {
                for (int i = 0; i < level; i++)
                    enemyOptions.add(i);
            }
            else {
                if (customEnemyToggle[0]) enemyOptions.add(0);
                if (customEnemyToggle[1]) enemyOptions.add(1);
                if (customEnemyToggle[2]) enemyOptions.add(2);
            }
            int type = enemyOptions.get((int) (Math.random() * enemyOptions.size()));
            switch (type) {
                case 0:
                    enemyList.add(new Enemy(getContext(), player, new Animator(enemySpriteList.get(type)), (level != 4 ? 1 : customEnemyHP)));
                    break;
                case 1:
                    enemyList.add(new Enemy(getContext(), player, new Animator(enemySpriteList.get(type)), (level != 4 ? 3 : customEnemyHP)));
                    break;
                case 2:
                    enemyList.add(new Enemy(getContext(), player, new Animator(enemySpriteList.get(type)), (level != 4 ? 5 : customEnemyHP)));
                    break;
                default:
                    break;
            }
        }

        // Update all enemies
        for (Enemy enemy : enemyList) {
            enemy.update();
        }

        // Check for enemy collision
        Iterator<Enemy> iteratorEnemy = enemyList.iterator();
        while (iteratorEnemy.hasNext()) {
            Entity enemy = iteratorEnemy.next();
            if (Entity.isColliding(enemy, player)) {
                // Destroy enemy if it collides with the player
                iteratorEnemy.remove();
                // Remove player HP
                if (player.getHP() > 0) player.setHP(player.getHP() - 1);
                continue;
            }

            // Check for spell collision
            Iterator<Spell> iteratorSpell = spells.iterator();
            while (iteratorSpell.hasNext()) {
                Spell spell = iteratorSpell.next();
                // Remove enemy HP if it collides with a spell
                if (Spell.isColliding(spell, enemy)) {
                    score++;
                    iteratorSpell.remove();
                    // Remove enemy HP or destroy if it collides with the player
                    if (enemy.getHP() - 1 <=0) {
                        iteratorEnemy.remove();
                        playDeathSound();
                    }
                    else enemy.setHP(enemy.getHP() - 1);
                    break;
                }
            }
        }

        for (Heart heart : heartsList) heart.update(); // Update hearts

        // Check for heart collision
        Iterator<Heart> iteratorHeart = heartsList.iterator();
        while (iteratorHeart.hasNext()) {
            Heart heart = iteratorHeart.next();
            if (Entity.isColliding(heart, player) && player.getHP() < player.MAX_HP) {
                player.setHP(player.getHP() + 1);
                iteratorHeart.remove();
            }
        }
    }

    /**
     * Recycles the memory used by the images drawn for the buttons.
     */
    private void optimiseMemory() {
        recycling = true;
        btnBack.recycleMemory();
        gameOver.btnRestart.recycleMemory();
        gameOver.btnBack.recycleMemory();
    }


    /**
     * readyToSpawn checks if a new enemy should spawn, according to the decided number of spawns
     * per minute (see SPAWNS_PER_MINUTE at top)
     * @return
     */
    public boolean readyToSpawn() {
        if (updatesUntilNextSpawn <= 0) {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        } else {
            updatesUntilNextSpawn --;
            return false;
        }
    }

    /**
     * When game is over, set player state.
     * Calculate and save high score.
     */
    private void onGameOver() {
        player.setState(EntityState.DEAD);
        if (score > highScore){
            highScore = score;
            saveHighScore();
        }
    }

    /**
     * Saves the new high score in the shared preferences.
     */
    private void saveHighScore() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(HIGH_SCORE, score);
        editor.apply();

    }

    /**
     * Play the death sound effect on different thread.
     */
    private void playDeathSound() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer effect = MediaPlayer.create(context, R.raw.death);
                effect.start();
                try {
                    Thread.sleep((effect.getDuration()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    displayMessage("Could not play the sound effect!");
                } finally {
                    effect.release();
                }
            }
        }).start();
    }

    /**
     * Pauses the game loop.
     */
    public void pause() {
        gameLoop.stopLoop();
    }
}
