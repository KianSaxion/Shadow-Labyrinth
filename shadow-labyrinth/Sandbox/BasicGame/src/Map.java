import nl.saxion.app.SaxionApp;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// This class contains mainly files for the drawing map and also needed methods
public class Map {
    public String image;
    public boolean collision;
    public boolean isFinish;
    public boolean isLightZone;
    public boolean isTrapZone; // not needed, just here for testing purposes
    public boolean resetsHealth;

    // Variable for the size of minimap
    private static final int TILE_SIZE_MINIMAP = 5;

    // This method loads two images for stone blocks stored in an array that is accessible within other methods
    // by passing it as an argument using OOP principles.
    public Map[] loadTileTypes() {
        Map[] tileTypes = new Map[14];
        Map darkWall = new Map();
        darkWall.image = "shadow-labyrinth/Sandbox/resources/images/map/redBrick.png";
        darkWall.collision = false;
        darkWall.isFinish = false;

        Map lightWall = new Map();
        lightWall.image = "shadow-labyrinth/Sandbox/resources/images/map/darkWall.png";
        lightWall.collision = true;
        lightWall.isFinish = false;

        Map voidWall = new Map();
        voidWall.image = "shadow-labyrinth/Sandbox/resources/images/map/void.png";
        voidWall.collision = false;
        voidWall.isFinish = false;

        Map lightZone = new Map();
        lightZone.image = "shadow-labyrinth/Sandbox/resources/images/map/redBrick.png";
        lightZone.collision = false;
        lightZone.isFinish = false;
        lightZone.isLightZone = true;

        Map trapZone = new Map();
        trapZone.image = "shadow-labyrinth/Sandbox/resources/images/map/redBrick.png";
        trapZone.collision = false;
        trapZone.isFinish = false;
        trapZone.isLightZone = false;
        trapZone.isTrapZone = true;

        Map healthResetZone = new Map();
        healthResetZone.image = "shadow-labyrinth/Sandbox/resources/images/map/healing_well.png";
        healthResetZone.collision = false;
        healthResetZone.isFinish = false;
        healthResetZone.isLightZone = false;
        healthResetZone.isTrapZone = false;
        healthResetZone.resetsHealth = true;

        Map endTile = new Map();
        endTile.image = "shadow-labyrinth/Sandbox/resources/images/map/end_gold_final.png";
        endTile.collision = false;
        endTile.isFinish = true;

        Map grassTile = new Map();
        grassTile.image = "shadow-labyrinth/Sandbox/resources/images/map/grassTile1.png";

        Map grassBrickTile = new Map();
        grassBrickTile.image = "shadow-labyrinth/Sandbox/resources/images/map/grassBrickTile.png";

        Map grassWallBottom = new Map();
        grassWallBottom.image = "shadow-labyrinth/Sandbox/resources/images/map/darkWallGrass.png";
        grassWallBottom.collision = true;

        Map grassWallCorner = new Map();
        grassWallCorner.image = "shadow-labyrinth/Sandbox/resources/images/map/darkWallGrassCorner.png";
        grassWallCorner.collision = true;

        Map grassWallLeft = new Map();
        grassWallLeft.image = "shadow-labyrinth/Sandbox/resources/images/map/darkWallGrassLeft.png";
        grassWallLeft.collision = true;

        Map grassWallRight = new Map();
        grassWallRight.image = "shadow-labyrinth/Sandbox/resources/images/map/darkWallGrassRight.png";
        grassWallRight.collision = true;

        Map grassWallTop = new Map();
        grassWallTop.image = "shadow-labyrinth/Sandbox/resources/images/map/darkWallGrassTop.png";
        grassWallTop.collision = true;

        Map grassWallCornerBottom = new Map();
        grassWallCornerBottom.image = "shadow-labyrinth/Sandbox/resources/images/map/darkWallGrassCorner2.png";
        grassWallCornerBottom.collision = true;

        tileTypes[0] = darkWall;
        tileTypes[1] = lightWall;
        tileTypes[2] = voidWall;
        tileTypes[3] = lightZone;
        tileTypes[4] = trapZone;
        tileTypes[5] = endTile;
        tileTypes[6] = healthResetZone;
        tileTypes[7] = grassBrickTile;
        tileTypes[8] = grassWallBottom;
        tileTypes[9] = grassWallCorner;
        tileTypes[10] = grassWallLeft;
        tileTypes[11] = grassWallRight;
        tileTypes[12] = grassWallTop;
        tileTypes[13] = grassWallCornerBottom;
        tileTypes[14] = grassTile;

        return tileTypes;
    }

    // 2d array to check whether the player was on that specific tile
    private static final boolean[][] visitedTiles = new boolean[Variable.MAX_MAP_ROW][Variable.MAX_MAP_COLUMN];

    // This method draws minimap based on the tiles and whether the player was on that tile or not
    public static void drawMinimap() {
        SaxionApp.setBackgroundColor(Color.black);
        SaxionApp.setTextDrawingColor(Color.white);
        SaxionApp.drawText("To exit the map, press Enter.", 300, 70, 30);

        SaxionApp.setFill(Color.darkGray);
        SaxionApp.setBorderColor(Color.darkGray);

        int x = 40;
        int y = 125;

        for (int row = 0; row < Variable.MAX_MAP_ROW; row++) {
            for (int col = 0; col < Variable.MAX_MAP_COLUMN; col++) {
                int tileNumber = BasicGame.tileNumbers[row][col];
                Map tile = BasicGame.tileTypes[tileNumber];

                if (visitedTiles[row][col]) {
                    SaxionApp.drawImage(tile.image, x, y, TILE_SIZE_MINIMAP, TILE_SIZE_MINIMAP);
                } else {
                    SaxionApp.drawRectangle(x, y, TILE_SIZE_MINIMAP, TILE_SIZE_MINIMAP);
                }
                x += TILE_SIZE_MINIMAP;
            }
            x = 40;
            y += TILE_SIZE_MINIMAP;
        }

        // Draw player on the map based on the location that it is in the game
        int playerX = (BasicGame.player.worldX / Variable.ORIGINAL_TILE_SIZE) * TILE_SIZE_MINIMAP + 35;
        int playerY = (BasicGame.player.worldY / Variable.ORIGINAL_TILE_SIZE) * TILE_SIZE_MINIMAP + 115;

        final int PLAYER_SIZE = 20;
        SaxionApp.drawImage("shadow-labyrinth/Sandbox/resources/images/player/MCfront.png", playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);
    }


    // This method draws entire map, and also changes the 2d Array of tiles where the player has stepped on
    public void drawMap(Player player, int[][] tileNumbers, Map[] tileTypes) {
        int startCol = Math.max(0, (player.worldX - player.screenX) / Variable.ORIGINAL_TILE_SIZE);
        int endCol = Math.min(Variable.MAX_MAP_COLUMN - 1, (player.worldX + player.screenX) / Variable.ORIGINAL_TILE_SIZE);
        int startRow = Math.max(0, (player.worldY - player.screenY) / Variable.ORIGINAL_TILE_SIZE);
        int endRow = Math.min(Variable.MAX_MAP_ROW - 1, (player.worldY + player.screenY) / Variable.ORIGINAL_TILE_SIZE);

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                int worldX = col * Variable.ORIGINAL_TILE_SIZE;
                int worldY = row * Variable.ORIGINAL_TILE_SIZE;
                int screenX = worldX - player.worldX + player.screenX;
                int screenY = worldY - player.worldY + player.screenY;
                int tileNumber = tileNumbers[row][col];
                Map tile = tileTypes[tileNumber];

                // Mark the tile as visited
                visitedTiles[row][col] = true;

                SaxionApp.drawImage(tile.image, screenX, screenY, Variable.ORIGINAL_TILE_SIZE, Variable.ORIGINAL_TILE_SIZE);
            }
        }
    }

    // This method loads tile numbers into a 2d array from the map txt file, basically each number corresponds to a
    // block that will be drawn on the screen.
    public void loadMap(int[][] tileNumbers) throws IOException {
        // MAP SETTINGS, in case if the file was not found. We will know what is the matter.
        Path path = Paths.get("shadow-labyrinth/Sandbox/resources/files/map.txt");
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + path.toAbsolutePath());
        }

        // It is a special class for read
        BufferedReader br = Files.newBufferedReader(path);

        int row = 0;

        while (row < Variable.MAX_MAP_ROW) {
            String line = br.readLine();
            if (line == null) break;

            String[] numbers = line.split(" ");
            for (int col = 0; col < Variable.MAX_MAP_COLUMN; col++) {
                tileNumbers[row][col] = Integer.parseInt(numbers[col]);
            }
            row++;
        }

        br.close();
    }
    public boolean checkCollision(int playerX, int playerY, int[][] tileNumbers, Map[] tileTypes, ArrayList<NPC> npcs) {
        int col = playerX / Variable.ORIGINAL_TILE_SIZE;
        int row = playerY / Variable.ORIGINAL_TILE_SIZE;

        // Check for tile collisions
        if (col >= 0 && col < Variable.MAX_MAP_COLUMN && row >= 0 && row < Variable.MAX_MAP_ROW) {
            int tileNumber = tileNumbers[row][col];
            if (tileTypes[tileNumber] != null && tileTypes[tileNumber].collision) {
                return true; // Collision with a solid tile
            }
        }

        // Check for NPC collisions
        for (NPC npc : npcs) {
            if (npc.isColliding(playerX, playerY)) {
                return true; // Collision with an NPC
            }
        }

        return false; // No collision
    }


    public boolean checkLightZone(int x, int y, int[][] tileNumbers, Map[] tileTypes) {
        return checkTileCondition(x, y, tileNumbers, tileTypes, "LightZone");
    }

    public boolean checkHealthReset(int x, int y, int[][] tileNumbers, Map[] tileTypes) {
        return checkTileCondition(x, y, tileNumbers, tileTypes, "HealthReset");
    }

    public boolean checkFinish(int x, int y, int[][] tileNumbers, Map[] tileTypes) {
        return checkTileCondition(x, y, tileNumbers, tileTypes, "Finish");
    }

    public boolean checkTileCondition(int x, int y, int[][] tileNumbers, Map[] tileTypes, String conditionType) {
        int col = x / Variable.ORIGINAL_TILE_SIZE;
        int row = y / Variable.ORIGINAL_TILE_SIZE;

        if (col >= 0 && col < Variable.MAX_MAP_COLUMN && row >= 0 && row < Variable.MAX_MAP_ROW) {
            int tileNumber = tileNumbers[row][col];
            Map tile = tileTypes[tileNumber];

            if (tile != null) {
                switch (conditionType) {
                    case "LightZone":
                        return tile.isLightZone;
                    case "HealthReset":
                        return tile.resetsHealth;
                    case "Finish":
                        return tile.isFinish;
                }
            }
        }
        return false;
    }
}