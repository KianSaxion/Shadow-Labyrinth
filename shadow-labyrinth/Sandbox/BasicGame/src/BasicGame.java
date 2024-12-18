import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.io.IOException;

public class BasicGame implements GameLoop {
    // The constant responsible for which screen to display
    public static int screenState = 0;
    int[][] tileNumbers = new int[Variable.MAX_MAP_ROW][Variable.MAX_MAP_COLUMN];
    Map[] tileTypes = new Map[3];


    // Game Entities
    Player player = new Player();
    KeyHandler keyHandler = new KeyHandler();
    Map currentMap = new Map();
    Lighting lighting;

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
        player.worldX = Variable.ORIGINAL_TILE_SIZE * 23;
        player.worldY = Variable.ORIGINAL_TILE_SIZE * 21;

        // Initialize lighting without initial filter creation
        lighting = new Lighting(player, 768, 576, 200); // Example starting circle size
    }

    @Override
    public void loop() {
        if (screenState == 0) {
            SaxionApp.clear();
            UserInterface.drawStartScreen();
        } else if (screenState == 1) {
            SaxionApp.clear();
            lighting.update(player, 320); // Light radius in pixels
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

            SaxionApp.drawImage(player.imageFile, (player.screenX - (Variable.ORIGINAL_TILE_SIZE / 2)),
                    (player.screenY - (Variable.ORIGINAL_TILE_SIZE / 2)), Variable.ORIGINAL_TILE_SIZE, Variable.ORIGINAL_TILE_SIZE);

            lighting.draw();
        }

        // Check if the player is moving downward (positive ySpeed) and check for a collision
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

        // Check if the player reached the finish tile
        if (currentMap.checkFinish(newX, newY, tileNumbers, tileTypes)) {
            System.out.println("Finished the game");

            // When we have the startmenu ready we can go to the start menu if the player
            // has reached the finish tile. For now we just print in the terminal that
            // the game is finished.
        }

        SaxionApp.drawImage(player.imageFile, (player.screenX - (Variable.ORIGINAL_TILE_SIZE / 2)),
                (player.screenY - (Variable.ORIGINAL_TILE_SIZE / 2)), Variable.ORIGINAL_TILE_SIZE, Variable.ORIGINAL_TILE_SIZE);

        lighting.draw();
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
