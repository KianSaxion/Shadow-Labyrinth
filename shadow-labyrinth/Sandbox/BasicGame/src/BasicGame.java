import nl.saxion.app.SaxionApp;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.awt.event.KeyEvent;

import java.util.ArrayList;

public class BasicGame implements GameLoop {
    ArrayList<Map> tiles = new ArrayList<>();
    // TILE SETTINGS
    final int FINAL_TILE_SCALE = 48;
    final int ORIGINAL_TILE_SIZE = 16;
    final int MAX_SCREEN_COL = 48;
    final int MAX_SCREEN_ROW = 48;
    Cookiemonster cookiemonster;

    // MAP SETTINGS
    final int maxMapCol = 50;
    final int maxMapRow = 50;
    final int mapWidth = FINAL_TILE_SCALE * maxMapCol;
    final int mapHeigth = FINAL_TILE_SCALE * maxMapRow;

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new BasicGame(), 768, 576, 40);
    }

    @Override
    public void init() {
        cookiemonster = new Cookiemonster();
        cookiemonster.worldX = ORIGINAL_TILE_SIZE * 23;
        cookiemonster.worldY = ORIGINAL_TILE_SIZE * 21;

        loadMap();
    }


    @Override
    public void loop() {

        SaxionApp.clear();

        drawMap();
        SaxionApp.drawImage(cookiemonster.imageFile, (cookiemonster.screenX - (FINAL_TILE_SCALE / 2)), (cookiemonster.screenY - (FINAL_TILE_SCALE / 2)), FINAL_TILE_SCALE, FINAL_TILE_SCALE);
        cookiemonster.worldX += cookiemonster.xSpeed;
        cookiemonster.worldY += cookiemonster.ySpeed;
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
                cookiemonster.ySpeed -= 1;
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                cookiemonster.ySpeed += 1;
            }
        }
    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }

    public void loadMap() {
        Map grass = new Map();
        grass.image = "shadow-labyrinth/Sandbox/resources/images/map/wall.png";

        tiles.add(grass);
    }

    public void drawMap() {
        int mapCol = 0;
        int mapRow = 0;

        while (mapCol < maxMapCol && mapRow < maxMapRow) {
            int worldX = mapCol * FINAL_TILE_SCALE;
            int worldY = mapRow * FINAL_TILE_SCALE;
            int screenX = worldX - cookiemonster.worldX + cookiemonster.screenX;
            int screenY = worldY - cookiemonster.worldY + cookiemonster.screenY;

            // the map tiles will be drawn around the player
            if (worldX + FINAL_TILE_SCALE > (cookiemonster.worldX - cookiemonster.screenX) &&
                    worldX - FINAL_TILE_SCALE < (cookiemonster.worldX + cookiemonster.screenX) &&
                    worldY + FINAL_TILE_SCALE > (cookiemonster.worldY - cookiemonster.screenY) &&
                    worldY - FINAL_TILE_SCALE < (cookiemonster.worldY + cookiemonster.screenY)) {
                SaxionApp.drawImage(tiles.get(0).image, screenX, screenY, FINAL_TILE_SCALE, FINAL_TILE_SCALE);
            }
            mapCol++;

            if (mapCol == maxMapCol) {
                mapCol = 0;
                mapRow++;
            }
        }
    }
}






