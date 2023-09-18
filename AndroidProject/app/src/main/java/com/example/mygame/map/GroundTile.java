package com.example.mygame.map;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.mygame.graphics.Sprite;
import com.example.mygame.graphics.SpriteSheet;

class GroundTile extends Tile {
    private final Sprite sprite;

    /** Constructs a ground tile.
     * @param spriteSheet sprite sheet to get the tile from
     * @param mapLocationRect rect representing the position of the tile to be drawn in the map
     */
    public GroundTile(SpriteSheet spriteSheet, Rect mapLocationRect) {
        super(mapLocationRect);
        sprite = spriteSheet.getGroundSprite();
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
