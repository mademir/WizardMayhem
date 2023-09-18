package com.example.mygame.map;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.mygame.graphics.Sprite;
import com.example.mygame.graphics.SpriteSheet;

class GrassTile extends Tile {
    private final Sprite sprite;

    /** Constructs a grass tile.
     * @param spriteSheet sprite sheet to get the tile from
     * @param mapLocationRect rect representing the position of the tile to be drawn in the map
     */
    public GrassTile(SpriteSheet spriteSheet, Rect mapLocationRect) {
        super(mapLocationRect);
        sprite = spriteSheet.getGrassSprite();
    }

    /**
     * Draws the tile on the canvas
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas, mapLocRect.left, mapLocRect.top);
    }
}
