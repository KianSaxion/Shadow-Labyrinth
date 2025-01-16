
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

    public boolean playerIsColliding(int playerX, int playerY) {
        final int PLAYER_X_OFFSET = 50;
        final int PLAYER_Y_OFFSET = 60;

        playerX -= PLAYER_X_OFFSET;
        playerY -= PLAYER_Y_OFFSET;

        return playerX < this.worldX &&
                playerX + Variable.ORIGINAL_TILE_SIZE > this.worldX &&
                playerY < this.worldY &&
                playerY + Variable.ORIGINAL_TILE_SIZE > this.worldY;
    }


    public static void update(Monster monster) {
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
            resolvePlayerCollision(monster);
        }
    }

    private static void resolvePlayerCollision(Monster monster) {
        if (Map.checkCollision(BasicGame.player.worldX, BasicGame.player.worldY, BasicGame.tileNumbers, BasicGame.tileTypes)) {

            int futureX = monster.worldX;
            int futureY = monster.worldY;

            // Knock the monster back slightly
            int deltaX = BasicGame.player.worldX - monster.worldX;
            int deltaY = BasicGame.player.worldY - monster.worldY;

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (deltaX > 0) {
                    futureX -= monster.speed;
                    if (Map.checkMonsterCollission(futureX, futureY, monsterSize, BasicGame.tileNumbers, BasicGame.tileTypes)) {
                        monster.worldX -= monster.speed;
                    } else {
                        monster.worldX += monster.speed;
                    }
                }
            } else {
                if (deltaY > 0) {
                    futureY -= monster.speed;
                    if (Map.checkMonsterCollission(futureX, futureY, monsterSize, BasicGame.tileNumbers, BasicGame.tileTypes)) {
                        monster.worldY -= monster.speed;
                    } else {
                        monster.worldY += monster.speed;
                    }
                }
            }

            setAction(monster);
        }
    }

    private static void moveMonster(Monster monster, int x, int y) {
        monster.worldX = x;
        monster.worldY = y;

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


