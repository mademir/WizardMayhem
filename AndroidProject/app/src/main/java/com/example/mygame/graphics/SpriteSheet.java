package com.example.mygame.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import com.example.mygame.R;

/**
 * Sprite sheet that gets a bitmap of sequential sprites from a drawable.
 */
public class SpriteSheet {
    public static final int SPRITE_W = 750;
    public static final int SPRITE_H = 750;
    public static final int MAX_FRAMES = 8;
    private Bitmap bitmap;

    /**
     * Construct a sprite sheet where sprites can be extracted from for drawing its frames.
     * @param context caller context
     * @param drawableID id of the drawable for the sprite sheet
     */
    public SpriteSheet(Context context, int drawableID) {
        BitmapFactory.Options bitmapOpts = new BitmapFactory.Options();
        bitmapOpts.inScaled = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), drawableID, bitmapOpts);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * Returns an array of Sprites from the current sprite sheet.
     * @return sprite array from sprite sheet
     */
    public Sprite[] getSprites() {
        Sprite[] sprites = new Sprite[MAX_FRAMES];
        for (int i = 0; i < MAX_FRAMES; i++){
            sprites[i] = new Sprite(this, new Rect(i*SPRITE_W, 0, (i+1)*SPRITE_W, SPRITE_H));
        }
        return sprites;
    }

    /**
     * Returns a specific sprite from the sheet at given row and column.
     * @param row row index of sprite in sprite sheet
     * @param col column index of sprite in sprite sheet
     * @return Sprite at row, col
     */
    private Sprite getSpriteByIndex(int row, int col) {
        return new Sprite(this, new Rect(
                col* 64,
                row* 64,
                (col + 1)* 64,
                (row + 1)* 64
        ));
    }

    public Sprite getWaterSprite() {
        return getSpriteByIndex(0, 0);
    }

    public Sprite getLavaSprite() {
        return getSpriteByIndex(0, 1);
    }

    public Sprite getGroundSprite() {
        return getSpriteByIndex(0, 2);
    }

    public Sprite getGrassSprite() {
        return getSpriteByIndex(0, 3);
    }

    public Sprite getTreeSprite() {
        return getSpriteByIndex(0, 4);
    }
}
