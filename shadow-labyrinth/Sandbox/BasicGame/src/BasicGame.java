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
import java.util.ArrayList;

public class BasicGame implements GameLoop {
    // TILE SETTINGS
    final int FINAL_TILE_SCALE = 48;
    final int ORIGINAL_TILE_SIZE = 16;
    final int MAX_SCREEN_COL = 50;
    final int MAX_SCREEN_ROW = 50;

    // MAP SETTINGS
    final int MAX_MAP_COLUMN = 48;
    final int MAX_MAP_ROW = 48;

    ArrayList<Map> tiles = new ArrayList<>();
    int[][] tileNumbers = new int[MAX_MAP_ROW][MAX_MAP_COLUMN];

    // Game Entities
    Cookiemonster cookiemonster;
    Map[] tileTypes;

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
        cookiemonster = new Cookiemonster();
        cookiemonster.worldX = ORIGINAL_TILE_SIZE * 23;
        cookiemonster.worldY = ORIGINAL_TILE_SIZE * 21;
    }

    @Override
    public void loop() {
        SaxionApp.clear();
        drawMap();

        int newX = cookiemonster.worldX + cookiemonster.xSpeed;
        int newY = cookiemonster.worldY + cookiemonster.ySpeed;

        // Check for collisions before updating position
        if (!checkCollision(newX, newY)) {
            cookiemonster.worldX = newX;
            cookiemonster.worldY = newY;
        } else {
            cookiemonster.xSpeed = 0;
            cookiemonster.ySpeed = 0;
        }


        SaxionApp.drawImage(cookiemonster.imageFile, (cookiemonster.screenX - (FINAL_TILE_SCALE / 2)),
                (cookiemonster.screenY - (FINAL_TILE_SCALE / 2)), FINAL_TILE_SCALE, FINAL_TILE_SCALE);

    }

    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        if (keyboardEvent.isKeyPressed()) {
            if (keyboardEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                if (cookiemonster.xSpeed > 0) {
                    cookiemonster.xSpeed = 0;
                } else {
                    cookiemonster.xSpeed -= 1;
                }
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (cookiemonster.xSpeed < 0) {
                    cookiemonster.xSpeed = 0;
                } else {
                    cookiemonster.xSpeed += 1;
                }
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_UP) {
                cookiemonster.ySpeed -= 1;
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                cookiemonster.ySpeed += 1;
            }
        }
    }

    /**
     * @param mouseEvent
     */
    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }

    private void loadTileTypes() {
        tileTypes = new Map[2];

        Map darkWall = new Map();
        darkWall.image = "shadow-labyrinth/Sandbox/resources/images/map/darkWall.png";
        darkWall.collision = true;

        Map lightWall = new Map();
        lightWall.image = "shadow-labyrinth/Sandbox/resources/images/map/lightWall.png";
        lightWall.collision = false;

        tileTypes[0] = darkWall;
        tileTypes[1] = lightWall;
    }

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

    public void drawMap() {
        int mapCol = 0;
        int mapRow = 0;

        while (mapCol < MAX_MAP_COLUMN && mapRow < MAX_MAP_ROW) {
            int worldX = mapCol * FINAL_TILE_SCALE;
            int worldY = mapRow * FINAL_TILE_SCALE;
            int screenX = worldX - cookiemonster.worldX + cookiemonster.screenX;
            int screenY = worldY - cookiemonster.worldY + cookiemonster.screenY;
            int tileNumber = tileNumbers[mapRow][mapCol];
            Map tile = tileTypes[tileNumber];

            // the map tiles will be drawn around the player
            if (worldX + FINAL_TILE_SCALE > (cookiemonster.worldX - cookiemonster.screenX) &&
                    worldX - FINAL_TILE_SCALE < (cookiemonster.worldX + cookiemonster.screenX) &&
                    worldY + FINAL_TILE_SCALE > (cookiemonster.worldY - cookiemonster.screenY) &&
                    worldY - FINAL_TILE_SCALE < (cookiemonster.worldY + cookiemonster.screenY)) {
                SaxionApp.drawImage(tile.image, screenX, screenY, FINAL_TILE_SCALE, FINAL_TILE_SCALE);
            }
            mapCol++;

            if (mapCol == MAX_MAP_COLUMN) {
                mapCol = 0;
                mapRow++;
            }
        }
    }

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
