package com.example.mygame.panels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.mygame.DisplayArea;
import com.example.mygame.R;


/**
 * This class is to draw the text Game Over to the screen.
 */
public class GameOver {

    private final DisplayArea displayArea;
    private Context context;
    public final GameButton btnBack;
    public final GameButton btnRestart;

    /**
     * Constructs the game over panel with the back and restart buttons and high score.
     * @param context caller context
     * @param displayArea display area of the game
     */
    public GameOver(Context context, DisplayArea displayArea) {
        this.context = context;
        this.displayArea = displayArea;

        BitmapFactory.Options bitmapOpts = new BitmapFactory.Options();
        bitmapOpts.inScaled = false;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_back, bitmapOpts);
        btnBack = new GameButton(bitmap, 200, 900, 100);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_restart, bitmapOpts);
        btnRestart = new GameButton(bitmap, displayArea.DISPLAY_RECT.right - 200, 900, 100);
    }

    /**
     * Draw method to draw the panel.
     * @param canvas canvas to draw on
     * @param highScore the high score value to display
     * @param score score value to display
     */
    public void draw(Canvas canvas, int highScore, int score) {
        Paint paint = new Paint();

        paint.setColor(Color.argb(0.5f, 0.1f, 0.1f, 0.1f));
        canvas.drawRect(
                0,
                0,
                displayArea.DISPLAY_RECT.right,
                displayArea.DISPLAY_RECT.bottom,
                paint
        );

        paint.setColor(ContextCompat.getColor(context, R.color.gameOver));
        paint.setTextSize(200);
        canvas.drawText("Game Over", 650, 350, paint);

        paint.setColor(ContextCompat.getColor(context, R.color.score));
        paint.setTextSize(60);
        canvas.drawText("High Score: " + Integer.toString(highScore), 950, 900, paint);

        btnBack.draw(canvas);
        btnRestart.draw(canvas);
    }
}
