import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.awt.*;
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
    private long lastHealthReductionTime = 0;
    private static final int HEALTH_COOLDOWN_MS = 320;

    //Audio flags
    private boolean youDiedMusicPlayed = false;
    private boolean endMusicPlayed = false;

    public static long startTime;
    public static long finishTime;
    public static boolean timerStarted = false;

    private long lastExecutionTime = 0;

    public static boolean isAddedToCSV = false;

    private static long lasTimeExecution = 0;

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
        NPC.setDialogue();

        Lighting.initializeFilters();

        // Initialize NPCs

        new NPC("shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Yellow_Right.png", Variable.ORIGINAL_TILE_SIZE * 10, Variable.ORIGINAL_TILE_SIZE * 49, 0);
        new NPC("shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Green_Right.png", Variable.ORIGINAL_TILE_SIZE * 61, Variable.ORIGINAL_TILE_SIZE * 43, 1);
        new NPC("shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Orange_Left.png", Variable.ORIGINAL_TILE_SIZE * 45, Variable.ORIGINAL_TILE_SIZE * 8, 2);
        new NPC("shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Blue_Left.png", Variable.ORIGINAL_TILE_SIZE * 115, Variable.ORIGINAL_TILE_SIZE * 53, 3);
        new NPC("shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Red_Right.png", Variable.ORIGINAL_TILE_SIZE * 64, Variable.ORIGINAL_TILE_SIZE * 10, 4);
        initializeGameState();
    }

    @Override
    public void loop() {
        if (screenState == 0) {
            SaxionApp.clear();
            UserInterface.drawStartScreen();

            if (!AudioHelper3.isPlaying()) {
                AudioHelper3.newSong("shadow-labyrinth/Sandbox/resources/sounds/HollowKnight_HollowKnightCut.wav", true);
            }

            if (!KeyHandler.isUpArrowPressed && !KeyHandler.isDownArrowPressed && AudioHelper.isPlaying()) {
                AudioHelper.stop();
            }

        } else if (screenState == 1) { // Main game
            SaxionApp.clear();
            keyHandler.update(player);
            long currentTime = System.currentTimeMillis();

            if (AudioHelper3.isPlaying()) {
                AudioHelper3.stop();
            }

            if (!AudioHelper.isPlaying()) {
                AudioHelper.newSong("shadow-labyrinth/Sandbox/resources/sounds/HollowKnight_Dirtmouth.wav", true);
            }

            // Update the camera position based on the player
            cameraX = player.worldX - player.screenX;
            cameraY = player.worldY - player.screenY;

            // Draw map and NPCs based on the camera
            currentMap.drawMap(player, tileNumbers, tileTypes);

            // Handle movement and collision logic
            int newX = player.worldX + player.xSpeed;
            int newY = player.worldY + player.ySpeed;

            // Check for collisions with the map or NPCs
            if (player.ySpeed > 0 && Map.checkCollision(newX, newY + 10, tileNumbers, tileTypes)) {
                player.ySpeed = 0;
                player.xSpeed = 0;
            } else if (!Map.checkCollision(newX, newY, tileNumbers, tileTypes)) {
                player.worldX = newX;
                player.worldY = newY;
            }
            // Draw and check collisions with all NPCs
            boolean npcInRange = false;

            TrapManager.drawTraps(cameraX, cameraY);

            // Trigger trap activation animations
            TrapManager.checkTrapActivation(player);

            // Handles health reduction separately from traps with timing
            if (TrapManager.checkHealthCollision(player) && currentTime - lastHealthReductionTime >= HEALTH_COOLDOWN_MS) {
                if (playerHealth.reduceHealth()) {
                    lastHealthReductionTime = currentTime;
                }
            }

            if (currentMap.checkHealthReset(player.worldX, player.worldY, tileNumbers, tileTypes)) {
                playerHealth.resetHealth();
            }

            if (playerHealth.isGameOver()) {
                screenState = 3;
            }

            for (NPC npc : NPC.NPCs) {
                npc.draw(cameraX, cameraY);

                // Check if the player is colliding with this specific NPC
                if (npc.isColliding(player.worldX, player.worldY)) {
                    if (!npcInRange) { // Ensure this block runs only once per frame
                        npcInRange = true; // Mark that the player is near an NPC
                        player.xSpeed = 0;
                        player.ySpeed = 0;
                    }
                }
            }
            // Draw the dialogue screen if activated
            if (NPC.activateDialogue) {
                UserInterface.drawNPCDialogue();
                Lighting.ENABLED = false;
            }


            long currentTime2 = System.currentTimeMillis();

            if (currentTime2 - lastExecutionTime >= 500) { // creates delay in NPC movements
                for (Monster monster : Monster.Monsters) {
                    if (Monster.isMonsterInVisivbleArea(cameraX, cameraY, monster) && monster.alive) {
                        Monster.update(monster);
                        AudioHelper3.play("shadow-labyrinth/Sandbox/resources/sounds/goopy-slime-4-219777.wav", false);
                    }
                }
                lastExecutionTime = currentTime;
            }

            for (Monster monster : Monster.Monsters) {
                if (monster.alive) {
                    Monster.draw(cameraX, cameraY, monster);
                }
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
                    timerStarted = false;
                    screenState = 5;
                }
            }

            Lighting.draw();
            drawHealthBar();


            // Draw and check collisions with all NPCs
            for (NPC npc : NPC.NPCs) {
                npc.draw(cameraX, cameraY);

                // Check if the player is colliding with this specific NPC
                if (npc.isColliding(player.worldX, player.worldY)) {
                    if (!npcInRange) { // Ensure this block runs only once per frame
                        npcInRange = true; // Mark that the player is near an NPC
                        player.xSpeed = 0;
                        player.ySpeed = 0;
                    }
                }
            }

            // Draw player and other entities
            SaxionApp.drawImage(player.imageFile, (player.screenX - (Variable.ORIGINAL_TILE_SIZE / 2)),
                    (player.screenY - (Variable.ORIGINAL_TILE_SIZE / 2)), Variable.ORIGINAL_TILE_SIZE, Variable.ORIGINAL_TILE_SIZE);

            
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
                playerHealth.resetHealth();
                initializeGameState();
            }

        } else if (screenState == 4) { // Map
            Map.drawMinimap();

        } else if (screenState == 5) { // End screen
            SaxionApp.clear();
            SaxionApp.drawImage("shadow-labyrinth/Sandbox/resources/images/screen/end_screen.png", 0, 0, 768, 576);
            SaxionApp.setTextDrawingColor(Color.WHITE);

            if (!endMusicPlayed) {
                AudioHelper.newSong("shadow-labyrinth/Sandbox/resources/sounds/HollowKnight_EnterHallownestCut.wav", false);
                endMusicPlayed = true;
            }

            long totalTime = finishTime - startTime;
            double seconds = totalTime / 1000.0;

            SaxionApp.drawText("You finished in " + seconds + " seconds", 10, 10, 20);
            SaxionApp.drawText("Press enter to go back", 10, 35, 20);

            if (!isAddedToCSV) {
                Leaderboard.saveTime(totalTime);
            }
            isAddedToCSV = true;

            if (screenState == 5 && KeyHandler.isEnterPressed) {
                endMusicPlayed = false;
                KeyHandler.isEnterPressed = false;
                if (AudioHelper.isPlaying()) {
                    AudioHelper.stop();
                }
                initializeGameState();
            }
        } else if (screenState == 6) {
            SaxionApp.clear();
            UserInterface.drawKeyMap();
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
        isAddedToCSV = false;

        startTime = 0;
        finishTime = 0;

        player.worldX = Variable.ORIGINAL_TILE_SIZE * 100; // 13
        player.worldY = Variable.ORIGINAL_TILE_SIZE * 52; // 500
        player.xSpeed = 0;
        player.ySpeed = 0;

        try {
            currentMap.loadMap(tileNumbers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        playerHealth = new Health();
        TrapManager = new TrapManager();
        TrapManager.initializeTraps(tileNumbers);
        youDiedMusicPlayed = false;

        new Monster("shadow-labyrinth/Sandbox/resources/images/monsters/blueslime_down_1.png", Variable.ORIGINAL_TILE_SIZE * 50, Variable.ORIGINAL_TILE_SIZE * 40);
        new Monster("shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png", Variable.ORIGINAL_TILE_SIZE * 32, Variable.ORIGINAL_TILE_SIZE * 18);
        new Monster("shadow-labyrinth/Sandbox/resources/images/monsters/blueslime_down_1.png", Variable.ORIGINAL_TILE_SIZE * 53, Variable.ORIGINAL_TILE_SIZE * 24);
        new Monster("shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png", Variable.ORIGINAL_TILE_SIZE * 83, Variable.ORIGINAL_TILE_SIZE * 26);
        new Monster("shadow-labyrinth/Sandbox/resources/images/monsters/blueslime_down_1.png", Variable.ORIGINAL_TILE_SIZE * 100, Variable.ORIGINAL_TILE_SIZE * 41);
        new Monster("shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png", Variable.ORIGINAL_TILE_SIZE * 100, Variable.ORIGINAL_TILE_SIZE * 52);
        new Monster("shadow-labyrinth/Sandbox/resources/images/monsters/blueslime_down_1.png", Variable.ORIGINAL_TILE_SIZE * 100, Variable.ORIGINAL_TILE_SIZE * 45);
        new Monster("shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png", Variable.ORIGINAL_TILE_SIZE * 98, Variable.ORIGINAL_TILE_SIZE * 18);
        new Monster("shadow-labyrinth/Sandbox/resources/images/monsters/blueslime_down_1.png", Variable.ORIGINAL_TILE_SIZE * 100, Variable.ORIGINAL_TILE_SIZE * 17);
        new Monster("shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png", Variable.ORIGINAL_TILE_SIZE * 101, Variable.ORIGINAL_TILE_SIZE * 19);
    }

    private void drawHealthBar() {
        playerHealth.draw();
    }
}