import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.io.IOException;

public class BasicGame implements GameLoop {
    // Camera properties
    private int cameraX;
    private int cameraY;
    // The constant responsible for which screen to display
    public static int screenState = 0;
    public static int[][] tileNumbers = new int[Variable.MAX_MAP_ROW][Variable.MAX_MAP_COLUMN];
    public static Map[] tileTypes = new Map[3];


    // Game Entities
    public static Player player = new Player();
    KeyHandler keyHandler = new KeyHandler();
    Map currentMap = new Map();
    Lighting lighting;
    NPC npc = new NPC();

    public static long startTime;
    public static long finishTime;
    public static boolean timerStarted = false;

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

        // Call the method to initialize the variables.
        // So if you want to initialize new variables use the -
        // initializeGameState method so that the initialization -
        // variables can reset once the game is finished
        initializeGameState();
    }


    @Override
    public void loop() {
        if (screenState == 0) {
            SaxionApp.clear();
            UserInterface.drawStartScreen();
        } else if (screenState == 1) {
            SaxionApp.clear();
            keyHandler.update(player);

            // Update the camera position based on the player
            cameraX = player.worldX - player.screenX;
            cameraY = player.worldY - player.screenY;

            // Draw map and NPCs based on the camera
            currentMap.drawMap(player, tileNumbers, tileTypes);
            npc.draw(cameraX, cameraY, Variable.ORIGINAL_TILE_SIZE * 10, Variable.ORIGINAL_TILE_SIZE * 49, 0);

            int newX = player.worldX + player.xSpeed;
            int newY = player.worldY + player.ySpeed;

            // check on collision
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

            if (currentMap.checkFinish(newX, newY, tileNumbers, tileTypes)) {
                if (timerStarted) {
                    finishTime = System.currentTimeMillis();
                    long totalTime = finishTime - startTime;
                    System.out.println("Finished the game in " + (totalTime / 1000.0) + " seconds.");
                    Leaderboard.saveTime(totalTime);
                    timerStarted = false;
                    initializeGameState();
                }
            }

            SaxionApp.drawImage(player.imageFile, (player.screenX - (Variable.ORIGINAL_TILE_SIZE / 2)),
                    (player.screenY - (Variable.ORIGINAL_TILE_SIZE / 2)), Variable.ORIGINAL_TILE_SIZE, Variable.ORIGINAL_TILE_SIZE);

            Lighting.draw();

            // if the screenState is equal to 2, show the leaderboard
        } else if (screenState == 2) {
            SaxionApp.clear();
            UserInterface.drawLeaderboard();
        } else if (screenState == 4) {
            Map.drawMinimap();
        }
    }

    public  void keyboardEvent(KeyboardEvent keyboardEvent) {
        if (keyboardEvent.isKeyPressed()) {
            KeyHandler.keyPressed(keyboardEvent);
        } else {
            KeyHandler.keyReleased(keyboardEvent);
        }
    }

    /**
     * @param mouseEvent
     */
    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }

    // Method to initialize the main variables of the game, also used to -
    // reset the game once finished.
    public void initializeGameState() {
        screenState = 0;
        timerStarted = false;

        player.worldX = Variable.ORIGINAL_TILE_SIZE * 13;
        player.worldY = Variable.ORIGINAL_TILE_SIZE * 50;
        player.xSpeed = 0;
        player.ySpeed = 0;

        try {
            currentMap.loadMap(tileNumbers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
