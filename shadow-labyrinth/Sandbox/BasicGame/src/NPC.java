import java.util.ArrayList;
import java.util.LinkedHashMap;

import nl.saxion.app.SaxionApp;

public class NPC {
    public static ArrayList<NPC> NPCs = new ArrayList<>(); // Store all NPC instances
    private String imagePath; // Path to the NPC's image
    private int worldX; // NPC's world X position
    private int worldY; // NPC's world Y position
    private int number; // Make number an instance variable
    public String loadDialogue; // Make loadDialogue an instance variable
    public static boolean activateDialogue = false;
    public boolean isDrawn = false;
    static String[] dialogues = new String[5];

    public NPC(String imagePath, int worldX, int worldY, int number) {
        this.imagePath = imagePath;
        this.worldX = worldX;
        this.worldY = worldY;
        this.number = number; // Assign the unique number
        if (number >= 0 && number < dialogues.length) {
            this.loadDialogue = dialogues[number]; // Assign dialogue specific to this NPC
        } else {
            this.loadDialogue = "No dialogue available."; // Fallback for invalid numbers
        }
        System.out.println("Initialized NPC with number: " + this.number);
        NPCs.add(this); // Add this NPC to the global list
    }

    public int getNumber() {
        return number;
    }

    public void draw(int cameraX, int cameraY) {
        // Calculate screen position based on the camera position
        int screenX = worldX - cameraX;
        int screenY = worldY - cameraY;

        // Scaling factor (e.g., 1.5 means 150% of the original size)
        double scale = 1.5;

        int scaledWidth = (int) (Variable.ORIGINAL_TILE_SIZE * scale);
        int scaledHeight = (int) (Variable.ORIGINAL_TILE_SIZE * scale);

        // Only draw if the NPC is within the screen's visible area
        if (screenX + scaledWidth > 0 && screenX < SaxionApp.getWidth() &&
                screenY + scaledHeight > 0 && screenY < SaxionApp.getHeight()) {
            SaxionApp.drawImage(imagePath, screenX, screenY, scaledWidth, scaledHeight);
        }
        isDrawn = true;
    }

    public boolean isColliding(int playerX, int playerY) {
        int npcSize = (int) (Variable.ORIGINAL_TILE_SIZE * 1.5); // Scaling applied
        boolean collision = playerX + Variable.ORIGINAL_TILE_SIZE > worldX &&
                playerX < worldX + npcSize &&
                playerY + Variable.ORIGINAL_TILE_SIZE > worldY &&
                playerY < worldY + npcSize;

        if (collision) {
            UserInterface.currentDialogue = this.loadDialogue;
            UserInterface.drawNPCDialogue();
        }

        return collision; // Return true if there's a collision
    }

    public static void setDialogue() {
        dialogues[0] = "Oi, mate!\n Welcome, I see you have been banished to the shadow realm too.\n Well, it happens to the best of us,\n I’m here because I was a bad kid and got banished here by Sinterklaas.\n To escape, you must find the exit in this labyrinth.\n Good luck mate, you’re gonna need it oh and btw,\n look out for Bob, he is dangerous...";
        dialogues[1] = "Do you see that?\n There are many slimy creatures lurking in the darkness.\n A long time ago, an ancient evil wizard called Bob was banished here,\n because he stole one singular candy from the Kruidvat.\n He tried to escape using magic,\n but he exploded and turned into a lot of blobs.";
        dialogues[2] = "Woa! Another traveler who wants to escape, cool!\n Watch out for the traps,\n they are designed by to make sure you cannot escape!\n You must avoid them,\n otherwise they might trap you,\n because they are traps!";
        dialogues[3] = "What, a traveler??\n Here??\n How is that even possible?\n Nobody has ever come this far, well nobody except of me of course!\n Somewhere in this section is the end of the labyrinth.\n You must find it, before the Blobs find you!\n";
        dialogues[4] = "You are very close mate!\n quickly, let's find the exit of this labyrinth and get out of here.\n FREEDOM AWAITS!! ";
    }
}