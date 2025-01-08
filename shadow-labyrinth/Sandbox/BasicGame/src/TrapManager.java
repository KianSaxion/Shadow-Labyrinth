import java.util.ArrayList;
import java.util.List;

public class TrapManager {
    private final List<Trap> traps;
    private boolean healthReduced; // Track health reduction for the collision cycle

    public TrapManager() {
        this.traps = new ArrayList<>();
        this.healthReduced = false;
    }

    // Main logic to check trap collisions and draw traps
    public void checkAndDrawTraps(int[][] tileNumbers, Player player, Health playerHealth, int cameraX, int cameraY) {
        for (int row = 0; row < Variable.MAX_MAP_ROW; row++) {
            for (int col = 0; col < Variable.MAX_MAP_COLUMN; col++) {
                if (tileNumbers[row][col] == 4) { // Trap zone tile
                    int trapX = col * Variable.ORIGINAL_TILE_SIZE;
                    int trapY = row * Variable.ORIGINAL_TILE_SIZE;

                    // Check if trap already exists in the list; if not, create it
                    Trap trap = Trap.findTrap(traps, trapX, trapY);
                    if (trap == null) {
                        trap = new Trap(trapX, trapY);
                        traps.add(trap);
                    }

                    // Check for trap collision with player
                    if (trap.checkCollision(player.worldX, player.worldY)) {
                        if (!playerHealth.isGameOver() && !healthReduced) {
                            // Reduce health only once per collision cycle
                            if (playerHealth.reduceHealth()) {
                                trap.activateTrap(); // Trigger trap animation
                                healthReduced = true; // Mark health reduction for this cycle
                            }
                        }
                    }

                    // Draw the trap
                    trap.draw(cameraX, cameraY);
                }
            }
        }

        // Reset the health reduction flag when the trap resets
        for (Trap trap : traps) {
            if (!trap.isActivated()) {
                healthReduced = false;
                break;
            }
        }
    }
}
