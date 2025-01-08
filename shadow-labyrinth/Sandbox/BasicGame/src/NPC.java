import nl.saxion.app.SaxionApp;

public class NPC {
    private String[] NPCs = new String[5];

    public NPC() {
        // Load NPC images
        NPCs[0] = "shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Yellow_Right.png";
        NPCs[1] = "shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Orange_Right.png";
        NPCs[2] = "shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Red_Right.png";
        NPCs[3] = "shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Green_Right.png";
        NPCs[4] = "shadow-labyrinth/Sandbox/resources/images/NPC/NPC_Blue_Right.png";
    }

    public void draw(int cameraX, int cameraY, int worldX, int worldY, int NPCNumber) {
        if (NPCNumber < 0 || NPCNumber >= NPCs.length) {
            throw new IllegalArgumentException("Invalid NPC index: " + NPCNumber);
        }

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
            SaxionApp.drawImage(NPCs[NPCNumber], screenX, screenY, scaledWidth, scaledHeight);
        }
    }
}
