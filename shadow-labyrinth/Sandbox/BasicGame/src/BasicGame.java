import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.io.IOException;

public class BasicGame implements GameLoop {
    // TILE SETTINGS
    final int FINAL_TILE_SCALE = 52;
    final int ORIGINAL_TILE_SIZE = 16;

    public final int MAX_MAP_COLUMN = 122;
    public  int MAX_MAP_ROW = 56;

    int[][] tileNumbers = new int[MAX_MAP_ROW][MAX_MAP_COLUMN];
    Map[] tileTypes = new Map[3];


    // Game Entities
    Player player = new Player();
    KeyHandler keyHandler = new KeyHandler();
    Map currentMap = new Map();

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new BasicGame(), 768, 576, 20);
    }

    @Override
    public void init() {
        try {
            tileTypes = currentMap.loadTileTypes();
            currentMap.loadMap(tileNumbers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        keyHandler.update(player);
        player.worldX = ORIGINAL_TILE_SIZE * 23;
        player.worldY = ORIGINAL_TILE_SIZE * 21;
    }

    @Override
    public void loop() {
        SaxionApp.clear();
        keyHandler.update(player);
        currentMap.drawMap(player, tileNumbers, tileTypes);
        int newX = player.worldX + player.xSpeed;
        int newY = player.worldY + player.ySpeed;

        // If the player is moving downward (positive ySpeed), check for a collision
        if (player.ySpeed > 0 && currentMap.checkCollision(newX, newY + 10, tileNumbers, tileTypes)) {
            player.ySpeed = 0;
            player.xSpeed = 0;
        }
        // If there is no collision at the player's intended new position (newX, newY)
        // Update the player's location if the path is clear
        else if (!currentMap.checkCollision(newX, newY, tileNumbers, tileTypes)) {
            player.worldX = newX;
            player.worldY = newY;
        }

        SaxionApp.drawImage(player.imageFile, (player.screenX - (FINAL_TILE_SCALE / 2)),
                (player.screenY - (FINAL_TILE_SCALE / 2)), FINAL_TILE_SCALE, FINAL_TILE_SCALE);

    }

    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        if (keyboardEvent.isKeyPressed()) {
            keyHandler.keyPressed(keyboardEvent);
        } else {
            keyHandler.keyReleased(keyboardEvent);
        }
    }

    /**
     * @param mouseEvent
     */
    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }

}
