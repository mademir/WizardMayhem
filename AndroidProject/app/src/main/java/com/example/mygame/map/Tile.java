package com.example.mygame.map;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.mygame.graphics.SpriteSheet;

abstract class Tile {

    protected final Rect mapLocRect;

    /**
     * Constructs a tile for a tile in the map layout with the provided map position.
     * @param mapLocationRect the map location
     */
    public Tile(Rect mapLocationRect) {
        this.mapLocRect = mapLocationRect;
    }

    /**
     * Enum for the tile types.
     */
    public enum TileType {
        WATER_TILE,
        LAVA_TILE,
        GROUND_TILE,
        GRASS_TILE,
        TREE_TILE
    }

    /**
     * Generates the specified tile.
     * @param tileType type of the tile
     * @param spriteSheet the sprite sheet the tile frame is from
     * @param mapLocRect the map location of the tile
     * @return tile of specified type
     */
    public static Tile getTile(int tileType, SpriteSheet spriteSheet, Rect mapLocRect) {

        switch(TileType.values()[tileType]) {

            case WATER_TILE:
                return new WaterTile(spriteSheet, mapLocRect);
            case LAVA_TILE:
                return new LavaTile(spriteSheet, mapLocRect);
            case GROUND_TILE:
                return new GroundTile(spriteSheet, mapLocRect);
            case GRASS_TILE:
                return new GrassTile(spriteSheet, mapLocRect);
            case TREE_TILE:
                return new TreeTile(spriteSheet, mapLocRect);
            default:
                return null;
        }

    }

    /**
     * Abstract draw class all tiles must have.
     * @param canvas canvas to draw on
     */
    public abstract void draw(Canvas canvas);
}
