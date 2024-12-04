import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BasicGame implements GameLoop {
    // TILE SETTINGS
    final int FINAL_TILE_SCALE = 64;
    final int ORIGINAL_TILE_SIZE = 16;

    // MAP SETTINGS
    final int MAX_MAP_COLUMN = 100;
    final int MAX_MAP_ROW = 100;

    // lists for a maps
    int[][] tileNumbers = new int[MAX_MAP_ROW][MAX_MAP_COLUMN];

    // Game Entities
    Player player;
    Map[] tileTypes;

    // Method for a safe loading of resources for the map. It is connected directly to SaxionApp
    public BasicGame() {
        try {
            loadTileTypes();
            loadMap();
        } catch (IOException e) {
            System.err.println("Error initializing game: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new BasicGame(), 768, 576, 20);
    }

    @Override
    public void init() {
        player = new Player();
        player.worldX = ORIGINAL_TILE_SIZE * 23;
        player.worldY = ORIGINAL_TILE_SIZE * 21;
    }

    @Override
    public void loop() {
        SaxionApp.clear();
        drawMap();

        int newX = player.worldX + player.xSpeed;
        int newY = player.worldY + player.ySpeed;

        // Check for collisions before updating position
        if (!checkCollision(newX, newY)) {
            player.worldX = newX;
            player.worldY = newY;
        } else {
            player.xSpeed = 0;
            player.ySpeed = 0;
        }


        SaxionApp.drawImage(player.imageFile, (player.screenX - (FINAL_TILE_SCALE / 2)),
                (player.screenY - (FINAL_TILE_SCALE / 2)), FINAL_TILE_SCALE, FINAL_TILE_SCALE);

    }

    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        if (keyboardEvent.isKeyPressed()) {
            if (keyboardEvent.getKeyCode() == KeyEvent.VK_A) {
                if (player.xSpeed > 0) {
                    player.xSpeed = 0;
                } else {
                    player.xSpeed -= 1;
                }
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_D) {
                if (player.xSpeed < 0) {
                    player.xSpeed = 0;
                } else {
                    player.xSpeed += 1;
                }
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_W) {
                player.ySpeed -= 1;
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_S) {
                player.ySpeed += 1;
            }
        }
    }

    /**
     * @param mouseEvent
     */
    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }

    // This method loads two images for stone blocks stored in an array that is accessible within other methods
    private void loadTileTypes() {
        tileTypes = new Map[3];

        Map darkWall = new Map();
        darkWall.image = "shadow-labyrinth/Sandbox/resources/images/map/lightWall.png";
        darkWall.collision = false;

        Map lightWall = new Map();
        lightWall.image = "shadow-labyrinth/Sandbox/resources/images/map/darkWall.png";
        lightWall.collision = false;

        Map voidWall = new Map();
        voidWall.image = "shadow-labyrinth/Sandbox/resources/images/map/void.png";
        voidWall.collision = false;

        tileTypes[0] = darkWall;
        tileTypes[1] = lightWall;
        tileTypes[2] = voidWall;
    }

    // This method loads numbers from a txt file into a 2d array that is used to draw a map
    private void loadMap() throws IOException {
        Path path = Paths.get("shadow-labyrinth/Sandbox/resources/files/world01.txt");
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

    // Draws a map using 2d Array
    public void drawMap() {
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

    // Checks for a collision based on the image whether that one has a boolean isCollision true or not
    private boolean checkCollision(int x, int y) {
        int col = x / FINAL_TILE_SCALE;
        int row = y / FINAL_TILE_SCALE;

        if (col >= 0 && col < MAX_MAP_COLUMN && row >= 0 && row < MAX_MAP_ROW) {
            int tileNumber = tileNumbers[row][col];
            return tileTypes[tileNumber].collision;
        }
        return true; // Collision for out-of-bound areas
    }
}
