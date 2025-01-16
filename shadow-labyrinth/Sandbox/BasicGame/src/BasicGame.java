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
    
    // HP related Game Entities
    Health playerHealth;
    private TrapManager TrapManager;
    private boolean youDiedMusicPlayed = false;

    public static long startTime;
    public static long finishTime;
    public static boolean timerStarted = false;

    private long lastExecutionTime = 0;

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

        Lighting.initializeFilters();

        // Call the method to initialize the variables.
        // So if you want to initialize new variables use the -
        // initializeGameState method so that the initialization -
        // variables can reset once the game is finished
        // Initialize NPCs
        new NPC("shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Yellow_Right.png", Variable.ORIGINAL_TILE_SIZE * 10, Variable.ORIGINAL_TILE_SIZE * 49);
        new NPC("shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Green_Right.png", Variable.ORIGINAL_TILE_SIZE * 61, Variable.ORIGINAL_TILE_SIZE * 43);
        new NPC("shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Orange_Left.png", Variable.ORIGINAL_TILE_SIZE * 45, Variable.ORIGINAL_TILE_SIZE * 8);
        new NPC("shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Blue_Left.png", Variable.ORIGINAL_TILE_SIZE * 115, Variable.ORIGINAL_TILE_SIZE * 53);
        new NPC("shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Red_Right.png", Variable.ORIGINAL_TILE_SIZE * 64, Variable.ORIGINAL_TILE_SIZE * 10);

        new Monster("shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png", Variable.ORIGINAL_TILE_SIZE * 50, Variable.ORIGINAL_TILE_SIZE * 40);
//        new Monster("shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png", Variable.ORIGINAL_TILE_SIZE * 53, Variable.ORIGINAL_TILE_SIZE * 40);

        // Initialize other game state variables
        initializeGameState();
    }

    @Override
    public void loop() {
        if (screenState == 0) {
            SaxionApp.clear();
            UserInterface.drawStartScreen();

            if (!KeyHandler.isUpArrowPressed && !KeyHandler.isDownArrowPressed && AudioHelper.isPlaying()) {
                AudioHelper.stop();
            }

        } else if (screenState == 1) { // Main game
            SaxionApp.clear();
            keyHandler.update(player);

            if (!AudioHelper.isPlaying()) {
//                AudioHelper.newSong("shadow-labyrinth/Sandbox/resources/sounds/HollowKnight_Dirtmouth.wav", true);
            }


            // Update the camera position based on the player
            cameraX = player.worldX - player.screenX;
            cameraY = player.worldY - player.screenY;

            // Draw map and NPCs based on the camera
            currentMap.drawMap(player, tileNumbers, tileTypes);

            // Draw all NPCs
            for (NPC npc : NPC.NPCs) {
                npc.draw(cameraX, cameraY);
            }

            long currentTime = System.currentTimeMillis();

            if (currentTime - lastExecutionTime >= 500) { // creates delay in NPC movements
                for (Monster monster : Monster.Monsters) {
                    if (Monster.isMonsterInVisivbleArea(cameraX, cameraY, monster)) {
                        Monster.update(monster);
//                        AudioHelper.play("shadow-labyrinth/Sandbox/resources/sounds/goopy-slime-4-219777.wav", false);
                    }
                }
                lastExecutionTime = currentTime;
            }

            for (Monster monster : Monster.Monsters) {
                if (monster.alive) {
                    Monster.draw(cameraX, cameraY, monster);
                }
            }

            int newX = player.worldX + player.xSpeed;
            int newY = player.worldY + player.ySpeed;

            // check on collision
            if (player.ySpeed > 0 && Map.checkCollision(newX, newY + 10, tileNumbers, tileTypes)) {
                player.ySpeed = 0;
                player.xSpeed = 0;
            } else if (!Map.checkCollision(newX, newY, tileNumbers, tileTypes)) {
                player.worldX = newX;
                player.worldY = newY;
            }

            // Delegate traps handling to TrapManager to reduce clutter in BasicGame
            TrapManager.checkAndDrawTraps(tileNumbers, player, playerHealth, cameraX, cameraY);

            // Check if the player is dead
            if (playerHealth.isGameOver()) {
                screenState = 3;
            }

            // Update the lighting filter based on player's position
            if (currentMap.checkLightZone(player.worldX, player.worldY, tileNumbers, tileTypes)) {
                Lighting.updateFilter(600);
            } else {
                Lighting.updateFilter(400);
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
            drawHealthBar();


            // if the screenState is equal to 2, show the leaderboard
        } else if (screenState == 2) {
            SaxionApp.clear();
            UserInterface.drawLeaderboard();

        } else if (screenState == 3) { // Game over screen
            SaxionApp.clear();
            SaxionApp.drawImage("shadow-labyrinth/Sandbox/resources/images/Traps/you_died_full.png", 0, 0, 768, 576);

            if (!youDiedMusicPlayed) {
                AudioHelper.newSong("shadow-labyrinth/Sandbox/resources/sounds/darkSouls_youDied.wav", false);
                youDiedMusicPlayed = true;
            }

            if (KeyHandler.isEnterPressed) {
                youDiedMusicPlayed = false;
                if (AudioHelper.isPlaying()) {
                    AudioHelper.stop();
                }
                initializeGameState();
            }

        } else if (screenState == 4) {
            Map.drawMinimap();
        }
    }

    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        if (keyboardEvent.isKeyPressed()) {
            KeyHandler.keyPressed(keyboardEvent);
        } else {
            KeyHandler.keyReleased(keyboardEvent);
        }
    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {
    }

    // Method to initialize the main variables of the game, also used to -
    // reset the game once finished.
    public void initializeGameState() {
        screenState = 0;
        timerStarted = false;

        player.worldX = Variable.ORIGINAL_TILE_SIZE * 52;
        player.worldY = Variable.ORIGINAL_TILE_SIZE * 39;
        player.xSpeed = 0;
        player.ySpeed = 0;

        try {
            currentMap.loadMap(tileNumbers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        playerHealth = new Health();
        TrapManager = new TrapManager();
        youDiedMusicPlayed = false;
    }

    private void drawHealthBar() {
        playerHealth.draw();
    }
}