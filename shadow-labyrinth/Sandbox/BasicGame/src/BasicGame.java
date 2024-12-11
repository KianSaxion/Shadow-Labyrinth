import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.io.IOException;

public class BasicGame implements GameLoop {
    Variable var = new Variable();


    int[][] tileNumbers = new int[var.MAX_MAP_ROW][var.MAX_MAP_COLUMN];
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
        player.worldX = var.ORIGINAL_TILE_SIZE * 23;
        player.worldY = var.ORIGINAL_TILE_SIZE * 21;
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

        SaxionApp.drawImage(player.imageFile, (player.screenX - (var.ORIGINAL_TILE_SIZE / 2)),
                (player.screenY - (var.ORIGINAL_TILE_SIZE / 2)), var.ORIGINAL_TILE_SIZE, var.ORIGINAL_TILE_SIZE);

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
