package com.example.mygame.map;

import static com.example.mygame.map.MapLayout.NUMBER_OF_COLUMN_TILES;
import static com.example.mygame.map.MapLayout.NUMBER_OF_ROW_TILES;
import static com.example.mygame.map.MapLayout.TILE_HEIGHT_PIXELS;
import static com.example.mygame.map.MapLayout.TILE_WIDTH_PIXELS;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.mygame.DisplayArea;
import com.example.mygame.R;
import com.example.mygame.graphics.SpriteSheet;

/**
 * Tile map to simulate the tiles on the ground in the game.
 */
public class Tilemap {

    private final MapLayout mapLayout;
    private Tile[][] tilemap;
    private SpriteSheet spriteSheet;
    private Bitmap mapBitmap;

    /**
     * Constructs a tilemap for the given level or with the specified map number.
     * @param context caller context
     * @param level level the tilemap is for
     * @param customMapNo the map number specified
     */
    public Tilemap(Context context, int level, int customMapNo) {
        level = (level != 4 ? level : customMapNo);
        mapLayout = new MapLayout(level);
        switch (level) {
            case 2:
                this.spriteSheet = new SpriteSheet(context, R.drawable.tiles2);
                break;
            case 3:
                this.spriteSheet = new SpriteSheet(context, R.drawable.tiles3);
                break;
            default:
                this.spriteSheet = new SpriteSheet(context, R.drawable.tiles1);
                break;
        }
        initializeTilemap();
    }

    /**
     * Initialises the tilemap with the given row and column numbers.
     * Creates a big single bitmap from the tiles.
     */
    private void initializeTilemap() {
        int[][] layout = mapLayout.getLayout();
        tilemap = new Tile[NUMBER_OF_ROW_TILES][NUMBER_OF_COLUMN_TILES];
        for (int iRow = 0; iRow < NUMBER_OF_ROW_TILES; iRow++) {
            for (int iCol = 0; iCol < NUMBER_OF_COLUMN_TILES; iCol++) {
                tilemap[iRow][iCol] = Tile.getTile(
                    layout[iRow][iCol],
                    spriteSheet,
                    getRectByIndex(iRow, iCol)
                );
            }
        }

        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        mapBitmap = Bitmap.createBitmap(
                NUMBER_OF_COLUMN_TILES*TILE_WIDTH_PIXELS,
                NUMBER_OF_ROW_TILES*TILE_HEIGHT_PIXELS,
                config
        );

        Canvas mapCanvas = new Canvas(mapBitmap);

        for (int iRow = 0; iRow < NUMBER_OF_ROW_TILES; iRow++) {
            for (int iCol = 0; iCol < NUMBER_OF_COLUMN_TILES; iCol++) {
                tilemap[iRow][iCol].draw(mapCanvas);
            }
        }
    }

    /**
     * Returns a rectangle that is at the given index of the tilemap.
     * @param idxRow row index
     * @param idxCol column index
     * @return rectangle of the index
     */
    private Rect getRectByIndex(int idxRow, int idxCol) {
        return new Rect(
                idxCol*TILE_WIDTH_PIXELS,
                idxRow*TILE_HEIGHT_PIXELS,
                (idxCol + 1)*TILE_WIDTH_PIXELS,
                (idxRow + 1)*TILE_HEIGHT_PIXELS
        );
    }

    /**
     * Draw the bitmap generated from the tiles.
     * @param canvas canvas to draw on
     * @param displayArea display area of the game
     */
    public void draw(Canvas canvas, DisplayArea displayArea) {
        canvas.drawBitmap(
            mapBitmap,
                displayArea.getGameRect(),
                displayArea.DISPLAY_RECT,
                null
        );
    }
}
