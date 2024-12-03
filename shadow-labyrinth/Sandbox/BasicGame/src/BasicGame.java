import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    final int maxMapCol = 50;
    final int maxMapRow = 50;

    ArrayList<Map> tiles = new ArrayList<>();
    int[][] tileNumbers = new int[maxMapRow][maxMapCol];

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
        SaxionApp.startGameLoop(new BasicGame(), 768, 576, 40);
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

        // Draw the Cookiemonster
        SaxionApp.drawImage(
                cookiemonster.imageFile,
                (cookiemonster.worldX - FINAL_TILE_SCALE / 2),
                (cookiemonster.worldY - FINAL_TILE_SCALE / 2),
                FINAL_TILE_SCALE,
                FINAL_TILE_SCALE
        );
    }

    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        if (keyboardEvent.isKeyPressed()) {
            if (keyboardEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                cookiemonster.xSpeed = -1;
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                cookiemonster.xSpeed = 1;
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_UP) {
                cookiemonster.ySpeed = -1;
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                cookiemonster.ySpeed = 1;
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

        Map wall = new Map();
        wall.image = "shadow-labyrinth/Sandbox/resources/images/map/wall.png";
        wall.collision = false;

        Map grass = new Map();
        grass.image = "shadow-labyrinth/Sandbox/resources/images/map/grass00.png";
        grass.collision = false;

        tileTypes[0] = grass;
        tileTypes[1] = wall;
    }

    private void loadMap() throws IOException {
        Path path = Paths.get("shadow-labyrinth/Sandbox/resources/files/world01.txt");
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + path.toAbsolutePath());
        }

        BufferedReader br = Files.newBufferedReader(path);

        int row = 0;

        while (row < maxMapRow) {
            String line = br.readLine();
            if (line == null) break;

            String[] numbers = line.split(" ");
            for (int col = 0; col < maxMapCol; col++) {
                tileNumbers[row][col] = Integer.parseInt(numbers[col]);
            }
            row++;
        }
        br.close();

    }

    private void drawMap() {
        for (int row = 0; row < maxMapRow; row++) {
            for (int col = 0; col < maxMapCol; col++) {
                int tileNumber = tileNumbers[row][col];
                Map tile = tileTypes[tileNumber];

                int worldX = col * FINAL_TILE_SCALE;
                int worldY = row * FINAL_TILE_SCALE;

                SaxionApp.drawImage(tile.image, worldX, worldY, FINAL_TILE_SCALE, FINAL_TILE_SCALE);
            }
        }
    }

    private boolean checkCollision(int x, int y) {
        int col = x / FINAL_TILE_SCALE;
        int row = y / FINAL_TILE_SCALE;

        if (col >= 0 && col < maxMapCol && row >= 0 && row < maxMapRow) {
            int tileNumber = tileNumbers[row][col];
            return tileTypes[tileNumber].collision;
        }
        return true; // Collision for out-of-bound areas
    }
}
