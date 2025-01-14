
import nl.saxion.app.SaxionApp;

import java.util.ArrayList;
import java.util.Random;

public class Monster {
    public static ArrayList<Monster> Monsters = new ArrayList<>(); // Store all Monsters instances

    public String imagePath; // Path to the Monster's image
    public int worldX; // Monster's world X position
    public int worldY; // Monster's world Y position
    public String direction;
    public int speed = 20;
    public int idleCounter = 0; // Counts the number of frames the monster remains idle
    public static boolean isDrawn = false;
    public int state = 0;

    public Monster(String imagePath, int worldX, int worldY) {
        this.imagePath = imagePath;
        this.worldX = worldX;
        this.worldY = worldY;
        Monsters.add(this); // Add this NPC to the list
    }

    public static void draw(int cameraX, int cameraY, Monster monster) {
        // Calculate screen position based on the camera position
        int screenX = monster.worldX - cameraX;
        int screenY = monster.worldY - cameraY;

        // Scaling factor (e.g., 1.5 means 150% of the original size)
        double scale = 1.5;

        int scaledWidth = (int) (Variable.ORIGINAL_TILE_SIZE * scale);
        int scaledHeight = (int) (Variable.ORIGINAL_TILE_SIZE * scale);

        // Only draw if the NPC is within the screen's visible area
        if (screenX + scaledWidth > 0 && screenX < SaxionApp.getWidth() &&
                screenY + scaledHeight > 0 && screenY < SaxionApp.getHeight()) {
            SaxionApp.drawImage(monster.imagePath, screenX, screenY, scaledWidth, scaledHeight);
        }
        isDrawn = true;
    }

    public static boolean isMonsterInVisivbleArea(int cameraX, int cameraY, Monster monster) {
        boolean inVisibleArea = false;

        // Calculate screen position based on the camera position
        int screenX = monster.worldX - cameraX;
        int screenY = monster.worldY - cameraY;

        // Scaling factor (e.g., 1.5 means 150% of the original size)
        double scale = 1.5;

        int scaledWidth = (int) (Variable.ORIGINAL_TILE_SIZE * scale);
        int scaledHeight = (int) (Variable.ORIGINAL_TILE_SIZE * scale);

        // Only draw if the NPC is within the screen's visible area
        if (screenX + scaledWidth > 0 && screenX < SaxionApp.getWidth() &&
                screenY + scaledHeight > 0 && screenY < SaxionApp.getHeight()) {
            inVisibleArea = true;
        }

        return inVisibleArea;
    }

    public boolean playerIsColliding(int playerX, int playerY) {
        final int PLAYER_X_OFFSET = 45;
        final int PLAYER_Y_OFFSET = 60;
        final int MONSTER_SIZE = 20;

        playerX -= PLAYER_X_OFFSET;
        playerY -= PLAYER_Y_OFFSET;


        return playerX < this.worldX + MONSTER_SIZE &&
                playerX + Variable.ORIGINAL_TILE_SIZE > this.worldX &&
                playerY < this.worldY + MONSTER_SIZE &&
                playerY + Variable.ORIGINAL_TILE_SIZE > this.worldY;
    }

    public static void update(Monster monster) {
        if (monster.idleCounter > 0) {
            // If the monster is idle, reduce the idle counter and skip movement
            monster.idleCounter--;
            return;
        }

        if (monster.playerIsColliding(BasicGame.player.worldX, BasicGame.player.worldY)) {
            resolvePlayerCollision(monster);
            monster.idleCounter = 2;
            return; // Skip further actions this frame
        }
//
//        if (Map.checkCollision(monster.worldX, monster.worldY, BasicGame.tileNumbers, BasicGame.tileTypes)) {
//            setAction(monster);
//        } else {
            // Normal action setting if no collision
            setAction(monster);

            // Movement logic
            if (!moveMonster(monster)) {
                // If the monster cannot move in its current direction, set idle and retry
                monster.idleCounter = 2;
                setAction(monster); // Retry with a new direction
            }
//        }

    }

    private static void resolvePlayerCollision(Monster monster) {
        int deltaX = BasicGame.player.worldX - monster.worldX;
        int deltaY = BasicGame.player.worldY - monster.worldY;

        // Determine the primary axis of collision
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // Horizontal collision
            if (deltaX > 0) {
                monster.worldX -= monster.speed; // Move left
            } else {
                monster.worldX += monster.speed; // Move right
            }
        } else {
            // Vertical collision
            if (deltaY > 0) {
                monster.worldY -= monster.speed; // Move up
            } else {
                monster.worldY += monster.speed; // Move down
            }
        }

        // Set idle state to give the monster time to recover
        monster.idleCounter = 2; // Pause for 2 frames
        setAction(monster); // Set a new random direction
    }

    private static boolean moveMonster(Monster monster) {
        boolean moved = false; // Tracks whether the monster moved

        if (monster.direction.equals("up")) {
            monster.imagePath = monster.state == 0
                    ? "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_2.png"
                    : "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png";
            monster.state = 1 - monster.state;
            monster.worldY -= monster.speed;
            moved = true;
        } else if (monster.direction.equals("down")) {
            monster.imagePath = monster.state == 0
                    ? "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_2.png"
                    : "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png";
            monster.state = 1 - monster.state;
            monster.worldY += monster.speed;
            moved = true;
        } else if (monster.direction.equals("left")) {
            monster.imagePath = monster.state == 0
                    ? "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_2.png"
                    : "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png";
            monster.state = 1 - monster.state;
            monster.worldX -= monster.speed;
            moved = true;
        } else if (monster.direction.equals("right")) {
            monster.imagePath = monster.state == 0
                    ? "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_2.png"
                    : "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png";
            monster.state = 1 - monster.state;
            monster.worldX += monster.speed;
            moved = true;
        }

        return moved;
    }

    public static void setAction(Monster monster) {
        Random random = new Random();

        int i = random.nextInt(100); // Pick up a number from  1 to 100

        if (i <= 25) {
            monster.direction = "up";
        } else if (i <= 50) {
            monster.direction = "down";
        } else if (i <= 75) {
            monster.direction = "left";
        } else {
            monster.direction = "right";
        }
    }
}


