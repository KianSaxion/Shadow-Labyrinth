import nl.saxion.app.SaxionApp;

import java.awt.*;

public class Minimap {
    public boolean isMiniMapOn = false;
    // I need tiles and tile numbers
    // Done

    // First draw the map on the full screen
    // Done

    // Then implement the M key, so when it is pressed and the game is running you get to see a map
    // then if you press ENTER you go back to the game
    // Done

    public static void drawMiniMapFullScreen() {
        SaxionApp.setBackgroundColor(Color.black);
        SaxionApp.setTextDrawingColor(Color.white);
        SaxionApp.drawText("To exit the map, press Enter.", 300, 70, 30);
        int tileSizeMiniMap = 5;

        int x = 40;
        int y = 125;

        for (int row = 0; row < Variable.MAX_MAP_ROW; row++) {
            for (int col = 0; col < Variable.MAX_MAP_COLUMN; col++) {
                int tileNumber = BasicGame.tileNumbers[row][col];
                Map tile = BasicGame.tileTypes[tileNumber];

                SaxionApp.drawImage(tile.image, x, y, tileSizeMiniMap, tileSizeMiniMap);
                x += tileSizeMiniMap;
            }
            x = 40;
            y += tileSizeMiniMap;
        }

        // Draw player on the map based on the location that it is in the game

        int playerX = (BasicGame.player.worldX / Variable.ORIGINAL_TILE_SIZE) * tileSizeMiniMap + 35;
        int playerY = (BasicGame.player.worldY / Variable.ORIGINAL_TILE_SIZE) * tileSizeMiniMap + 115;
        SaxionApp.drawImage("shadow-labyrinth/Sandbox/resources/images/player/MCfront.png", playerX, playerY, 15, 15);

    }


    // Draw a part of the minimap on the game screen while game is running in a small window

    // Draw a player on that map
}
