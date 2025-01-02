import nl.saxion.app.SaxionApp;

import java.awt.*;
import java.io.IOException;

public class Minimap {
    static Map[] tileTypes = new Map[3];
    static int[][] tileNumbers = new int[Variable.MAX_MAP_ROW][Variable.MAX_MAP_COLUMN];
    boolean isMiniMapOn = false;
    public static int tileSize = 10;

    public static void initializeMiniMap() throws IOException {
        tileNumbers = Map.loadMap();
        tileTypes = Map.loadTileTypes();
    }

    public static void drawMiniMap() throws IOException {
        initializeMiniMap();
        SaxionApp.setBackgroundColor(Color.black);

        int x = 100;
        int y = 100;

        for (int row = 0; row <= Variable.MAX_MAP_ROW; row++) {
            for (int col = 0; col <= Variable.MAX_MAP_COLUMN; col++) {

                int tileNumber = tileNumbers[row][col];
                Map tile = tileTypes[tileNumber];

                SaxionApp.drawImage(tile.image, x, y,tileSize, tileSize);
                x += tileSize;
            }
            y += tileSize;
        }
    }

//    // This method draws a map based on the player coordinates, so it does not draw an entire map, as a result it does
//    // not overload computer resources. It uses an array of tiles with images and also 2d array with numbers of the map blocks
//    public void drawMap(Player player, int[][] tileNumbers, Map[] tileTypes) {
//        int startCol = Math.max(0, (player.worldX - player.screenX) / Variable.ORIGINAL_TILE_SIZE);
//        int endCol = Math.min(Variable.MAX_MAP_COLUMN - 1, (player.worldX + player.screenX) / Variable.ORIGINAL_TILE_SIZE);
//        int startRow = Math.max(0, (player.worldY - player.screenY) / Variable.ORIGINAL_TILE_SIZE);
//        int endRow = Math.min(Variable.MAX_MAP_ROW - 1, (player.worldY + player.screenY) / Variable.ORIGINAL_TILE_SIZE);
//
//        for (int row = startRow; row <= endRow; row++) {
//            for (int col = startCol; col <= endCol; col++) {
//                int worldX = col * Variable.ORIGINAL_TILE_SIZE;
//                int worldY = row * Variable.ORIGINAL_TILE_SIZE;
//                int screenX = worldX - player.worldX + player.screenX;
//                int screenY = worldY - player.worldY + player.screenY;
//                int tileNumber = tileNumbers[row][col];
//                Map tile = tileTypes[tileNumber];
//
//                SaxionApp.drawImage(tile.image, screenX, screenY, Variable.ORIGINAL_TILE_SIZE, Variable.ORIGINAL_TILE_SIZE);
//            }
//        }
//    }

}
