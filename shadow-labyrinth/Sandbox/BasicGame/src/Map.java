import nl.saxion.app.SaxionApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// This class contains mainly files for the drawing map and also needed methods
public class Map {
    public String image;
    public boolean collision;
    public boolean isFinish;

    // This method loads two images for stone blocks stored in an array that is accessible within other methods
    // by passing it as an argument using OOP principles.
    public Map[] loadTileTypes() {
        Map[] tileTypes = new Map[4];
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

        Map endTile = new Map();
        endTile.image = "shadow-labyrinth/Sandbox/resources/images/map/redBrick.png";
        endTile.collision = false;
        endTile.isFinish = true;

        tileTypes[0] = darkWall;
        tileTypes[1] = lightWall;
        tileTypes[2] = voidWall;
        tileTypes[3] = endTile;

        return tileTypes;
    }

    // This method draws a map based on the player coordinates, so it does not draw an entire map, as a result it does
    // not overload computer resources. It uses an array of tiles with images and also 2d array with numbers of the map blocks
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

    public boolean checkCollision(int x, int y, int[][] tileNumbers, Map[] tileTypes) {
        int col = x / Variable.ORIGINAL_TILE_SIZE;
        int row = y / Variable.ORIGINAL_TILE_SIZE;

        if (col >= 0 && col < Variable.MAX_MAP_COLUMN && row >= 0 && row < Variable.MAX_MAP_ROW) {
            int tileNumber = tileNumbers[row][col];
            return tileTypes[tileNumber] != null && tileTypes[tileNumber].collision;
        }
        return true;
    }

    // This method is very similar to the checkCollisions method, we might have to do something about
    // The redundancy of the code
    public boolean checkFinish(int x, int y, int[][] tileNumbers, Map[] tileTypes) {
        int col = x / Variable.ORIGINAL_TILE_SIZE;
        int row = y / Variable.ORIGINAL_TILE_SIZE;

        if (col >= 0 && col < Variable.MAX_MAP_COLUMN && row >= 0 && row < Variable.MAX_MAP_ROW) {
            int tileNumber = tileNumbers[row][col];
            return tileTypes[tileNumber] != null && tileTypes[tileNumber].isFinish;
        }
        return false;
    }
}