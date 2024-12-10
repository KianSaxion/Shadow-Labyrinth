import nl.saxion.app.SaxionApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Map {
    public String image;
    public boolean collision;

    public final int MAX_MAP_COLUMN = 122;
    public final int MAX_MAP_ROW = 56;
    // TILE SETTINGS
    final int FINAL_TILE_SCALE = 52;


    // This method loads two images for stone blocks stored in an array that is accessible within other methods
    public Map[] loadTileTypes() {
        Map[] tileTypes = new Map[3];
        Map darkWall = new Map();
        darkWall.image = "shadow-labyrinth/Sandbox/resources/images/map/redBrick.png";
        darkWall.collision = false;

        Map lightWall = new Map();
        lightWall.image = "shadow-labyrinth/Sandbox/resources/images/map/darkWall.png";
        lightWall.collision = true;

        Map voidWall = new Map();
        voidWall.image = "shadow-labyrinth/Sandbox/resources/images/map/void.png";
        voidWall.collision = false;

        tileTypes[0] = darkWall;
        tileTypes[1] = lightWall;
        tileTypes[2] = voidWall;

        return tileTypes;
    }


    // Draws a map using 2d Array
    public void drawMap(Player player, int[][] tileNumbers, Map[] tileTypes) {
        int mapCol = 0;
        int mapRow = 0;

        while (mapCol < MAX_MAP_COLUMN && mapRow < MAX_MAP_ROW) {
            int worldX = mapCol * FINAL_TILE_SCALE;
            int worldY = mapRow * FINAL_TILE_SCALE;
            int screenX = worldX - player.worldX + player.screenX;
            int screenY = worldY - player.worldY + player.screenY;
            int tileNumber = tileNumbers[mapRow][mapCol];
            Map tile = tileTypes[tileNumber];

            // the map tiles will be drawn around the player
            if (worldX + FINAL_TILE_SCALE > (player.worldX - player.screenX) &&
                    worldX - FINAL_TILE_SCALE < (player.worldX + player.screenX) &&
                    worldY + FINAL_TILE_SCALE > (player.worldY - player.screenY) &&
                    worldY - FINAL_TILE_SCALE < (player.worldY + player.screenY)) {
                SaxionApp.drawImage(tile.image, screenX, screenY, FINAL_TILE_SCALE, FINAL_TILE_SCALE);
            }
            mapCol++;

            if (mapCol == MAX_MAP_COLUMN) {
                mapCol = 0;
                mapRow++;
            }
        }
    }

    public void loadMap(int[][] tileNumbers) throws IOException {
        // MAP SETTINGS


        Path path = Paths.get("shadow-labyrinth/Sandbox/resources/files/map.txt");
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + path.toAbsolutePath());
        }

        BufferedReader br = Files.newBufferedReader(path);

        int row = 0;

        while (row < MAX_MAP_ROW) {
            String line = br.readLine();
            if (line == null) break;

            String[] numbers = line.split(" ");
            for (int col = 0; col < MAX_MAP_COLUMN; col++) {
                tileNumbers[row][col] = Integer.parseInt(numbers[col]);
            }
            row++;
        }

        br.close();
    }

    public boolean checkCollision(int x, int y, int[][] tileNumbers, Map[] tileTypes) {
        int col = x / FINAL_TILE_SCALE;
        int row = y / FINAL_TILE_SCALE;

        if (col >= 0 && col < MAX_MAP_COLUMN && row >= 0 && row < MAX_MAP_ROW) {
            int tileNumber = tileNumbers[row][col];
            return tileTypes[tileNumber] != null && tileTypes[tileNumber].collision;
        }
        return true; // Collision for out-of-bound areas
    }
}