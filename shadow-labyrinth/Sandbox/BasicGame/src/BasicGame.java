import nl.saxion.app.SaxionApp;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.awt.event.KeyEvent;

import java.util.ArrayList;

public class BasicGame implements GameLoop {
    ArrayList<Map> tiles = new ArrayList<>();
    final int FINAL_SCALE = 48;
    final int MAX_SCREEN_COL = 48;
    final int MAX_SCREEN_ROW = 48;
    Cookiemonster cookiemonster;

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new BasicGame(), 768, 576, 40);
    }

    @Override
    public void init() {
        cookiemonster = new Cookiemonster();
        cookiemonster.x = 100;
        cookiemonster.y = 100;

        loadMap();
    }


    @Override
    public void loop() {

        SaxionApp.clear();

        int newX = cookiemonster.x + cookiemonster.xSpeed;
        int newY = cookiemonster.y + cookiemonster.ySpeed;

        // Check if the new position has collisions
        if (!checkCollision(newX, newY)) {
            // Only move if there is no collision
            cookiemonster.x = newX;
            cookiemonster.y = newY;
        } else {
            // Reset the speed if there is a is a collision
            cookiemonster.xSpeed = 0;
            cookiemonster.ySpeed = 0;
        }

        drawMap();
        SaxionApp.drawImage(cookiemonster.imageFile, cookiemonster.x, cookiemonster.y, FINAL_SCALE, FINAL_SCALE);

    }

    @Override
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
                if (cookiemonster.ySpeed > 0) {
                    cookiemonster.ySpeed = 0;
                } else {
                    cookiemonster.ySpeed -= 1;
                }
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                if (cookiemonster.ySpeed < 0) {
                    cookiemonster.ySpeed = 0;
                } else {
                    cookiemonster.ySpeed += 1;
                }
            }
        }
    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }

    public void loadMap() {
        // Create Wall and grass objects
        Map wall = new Map();
        wall.image = "shadow-labyrinth/Sandbox/resources/images/map/wall.png";
        wall.collision = true;

        Map grass = new Map();
        grass.image = "shadow-labyrinth/Sandbox/resources/images/map/grass00.png";
        grass.collision = false;

        // Fill the list with the map configuration
        for (int row = 0; row < MAX_SCREEN_ROW; row++) {
            for (int col = 0; col < MAX_SCREEN_COL; col++) {
                if (row == 0 || row == MAX_SCREEN_ROW - 1 || col == 0 || col == MAX_SCREEN_COL - 1) {
                    tiles.add(wall); // Outside layer filled with walls
                } else {
                    tiles.add(grass); // Inside area filled with grass
                }
            }
        }
    }

    public void drawMap() {
        int x = 0;
        int y = 0;

        for (int row = 0; row < MAX_SCREEN_ROW; row++) {
            for (int col = 0; col < MAX_SCREEN_COL; col++) {
                // Calculate the index in the tiles list
                int index = row * MAX_SCREEN_COL + col;
                Map tile = tiles.get(index);

                // draw the picture of the tile
                SaxionApp.drawImage(tile.image, x, y, FINAL_SCALE, FINAL_SCALE);

                x += FINAL_SCALE;
            }
            x = 0;
            y += FINAL_SCALE;
        }
    }

    public boolean checkCollision(int x, int y) {
        // Calculate in which column and row the player is
        int playerCol = x / FINAL_SCALE;
        int playerRow = y / FINAL_SCALE;

        // Check if the coordinates are within the raster
        if (playerCol >= 0 && playerCol < MAX_SCREEN_COL && playerRow >= 0 && playerRow < MAX_SCREEN_ROW) {
            int index = playerRow * MAX_SCREEN_COL + playerCol;
            Map tile = tiles.get(index);

            // Check if a tile is a wall
            return tile.collision;
        }

        // Outside the raster is always a collision
        return true;
    }
}







