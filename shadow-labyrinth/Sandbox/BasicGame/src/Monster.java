
import nl.saxion.app.SaxionApp;

import java.util.ArrayList;
import java.util.Random;

public class Monster {
    public static ArrayList<Monster> Monsters = new ArrayList<>(); // Store all Monsters instances
    public static int monsterSize = 50;

    public String imagePath; // Path to the Monster's image
    public int worldX; // Monster's world X position
    public int worldY; // Monster's world Y position
    public String direction;
    public int speed = 20;
    public int state = 0;
    public boolean alive = true;

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

        // Only draw if the NPC is within the screen's visible area
        if (screenX + Variable.ORIGINAL_TILE_SIZE > 0 && screenX < SaxionApp.getWidth() &&
                screenY + Variable.ORIGINAL_TILE_SIZE > 0 && screenY < SaxionApp.getHeight()) {
            SaxionApp.drawImage(monster.imagePath, screenX, screenY, monsterSize, monsterSize);
        }
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

    public static boolean playerIsColliding(int playerX, int playerY, Monster monster) {
        final int PLAYER_X_OFFSET = 50;
        final int PLAYER_Y_OFFSET = 60;

        // Define the attack range (can be adjusted based on the game's needs)
        int attackRange = Variable.ORIGINAL_TILE_SIZE;

        // Check if the player is close enough to the monster
        boolean isInRange = Math.abs(playerX - monster.worldX)
                <= attackRange && Math.abs(playerY - monster.worldY) <= attackRange;

        if (isInRange && KeyHandler.isEPressed && monster.alive) { // Assuming "E" is the attack key
            monster.alive = false; // Kill the monster
            AudioHelper2.play("shadow-labyrinth/Sandbox/resources/sounds/deadSlime.wav", false);
        }

        playerX -= PLAYER_X_OFFSET;
        playerY -= PLAYER_Y_OFFSET;

        return playerX < monster.worldX &&
                playerX + Variable.ORIGINAL_TILE_SIZE > monster.worldX &&
                playerY < monster.worldY &&
                playerY + Variable.ORIGINAL_TILE_SIZE > monster.worldY;
    }


    public static void update(Monster monster) {
        if (monster.alive) {
            int futureX = monster.worldX; // Start with the current position
            int futureY = monster.worldY;

            setAction(monster); // Determine the monster's action
            switch (monster.direction) {
                case "right" -> futureX += monster.speed;
                case "left" -> futureX -= monster.speed;
                case "up" -> futureY -= monster.speed;
                case "down" -> futureY += monster.speed;
            }

//         Check if movement is valid
            if (Map.checkMonsterCollission(futureX, futureY, monsterSize, BasicGame.tileNumbers, BasicGame.tileTypes)) {
                // Update the monster's position
                moveMonster(monster, futureX, futureY);
            }
        }
    }

    private static void moveMonster(Monster monster, int x, int y) {
        monster.worldX = x;
        monster.worldY = y;

        if (monster.imagePath.equals("shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png") ||
                monster.imagePath.equals("shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_2.png")) {
            if (monster.direction.equals("up")) {
                monster.imagePath = monster.state == 0
                        ? "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_2.png"
                        : "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png";
                monster.state = 1 - monster.state;
            } else if (monster.direction.equals("down")) {
                monster.imagePath = monster.state == 0
                        ? "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_2.png"
                        : "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png";
                monster.state = 1 - monster.state;
                monster.worldY += monster.speed;
            } else if (monster.direction.equals("left")) {
                monster.imagePath = monster.state == 0
                        ? "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_2.png"
                        : "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png";
                monster.state = 1 - monster.state;
            } else if (monster.direction.equals("right")) {
                monster.imagePath = monster.state == 0
                        ? "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_2.png"
                        : "shadow-labyrinth/Sandbox/resources/images/monsters/redslime_down_1.png";
                monster.state = 1 - monster.state;
            }
        }
        // Duplicated code for better movement of the slime, otherwise it stays in a very small box.
        else {
            if (monster.direction.equals("up")) {
                monster.imagePath = monster.state == 0
                        ? "shadow-labyrinth/Sandbox/resources/images/monsters/blueslime_down_2.png"
                        : "shadow-labyrinth/Sandbox/resources/images/monsters/blueslime_down_1.png";
                monster.state = 1 - monster.state;
            } else if (monster.direction.equals("down")) {
                monster.imagePath = monster.state == 0
                        ? "shadow-labyrinth/Sandbox/resources/images/monsters/blueslime_down_2.png"
                        : "shadow-labyrinth/Sandbox/resources/images/monsters/blueslime_down_1.png";
                monster.state = 1 - monster.state;
                monster.worldY += monster.speed;
            } else if (monster.direction.equals("left")) {
                monster.imagePath = monster.state == 0
                        ? "shadow-labyrinth/Sandbox/resources/images/monsters/blueslime_down_2.png"
                        : "shadow-labyrinth/Sandbox/resources/images/monsters/blueslime_down_1.png";
                monster.state = 1 - monster.state;
            } else if (monster.direction.equals("right")) {
                monster.imagePath = monster.state == 0
                        ? "shadow-labyrinth/Sandbox/resources/images/monsters/blueslime_down_2.png"
                        : "shadow-labyrinth/Sandbox/resources/images/monsters/blueslime_down_1.png";
                monster.state = 1 - monster.state;
            }
        }

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


