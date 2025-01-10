import nl.saxion.app.SaxionApp;

import java.util.ArrayList;

public class NPC {
    public static ArrayList<NPC> NPCs = new ArrayList<>(); // Store all NPC instances
    public String imagePath; // Path to the NPC's image
    public int worldX; // NPC's world X position
    public int worldY; // NPC's world Y position
    public boolean isDrawn = false;

    public NPC(String imagePath, int worldX, int worldY) {
        this.imagePath = imagePath;
        this.worldX = worldX;
        this.worldY = worldY;
        NPCs.add(this); // Add this NPC to the list
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
        // Define the size of the NPC (based on tile size)
        int npcSize = (int) (Variable.ORIGINAL_TILE_SIZE * 1.5); // Scaling applied

        // Check for overlap between the player's position and the NPC's position
        return playerX < worldX + npcSize &&
                playerX + Variable.ORIGINAL_TILE_SIZE > worldX &&
                playerY < worldY + npcSize &&
                playerY + Variable.ORIGINAL_TILE_SIZE > worldY;
    }
}
