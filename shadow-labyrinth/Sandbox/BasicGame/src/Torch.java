public class Torch {

    private final int worldX, worldY;
    private final int lightRadius;

    // Add getter methods
    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
    }

    public int getLightRadius() {
        return lightRadius;
    }

    // Constructor
    public Torch(int worldX, int worldY, int lightRadius) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.lightRadius = lightRadius;
    }
}
