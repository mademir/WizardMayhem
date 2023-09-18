package com.example.mygame.map;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.mygame.graphics.Sprite;
import com.example.mygame.graphics.SpriteSheet;

class TreeTile extends Tile {
    private final Sprite grassSprite;
    private final Sprite treeSprite;

    /** Constructs a tree tile.
     * @param spriteSheet sprite sheet to get the tile from
     * @param mapLocationRect rect representing the position of the tile to be drawn in the map
     */
    public TreeTile(SpriteSheet spriteSheet, Rect mapLocationRect) {
        super(mapLocationRect);
        grassSprite = spriteSheet.getGrassSprite();
        treeSprite = spriteSheet.getTreeSprite();
    }

    /**
     * Draws the tile on the canvas
     * @param canvas canvas to draw on
     */
    @Override
    public void draw(Canvas canvas) {
        grassSprite.draw(canvas, mapLocRect.left, mapLocRect.top);
        treeSprite.draw(canvas, mapLocRect.left, mapLocRect.top);
    }
}
