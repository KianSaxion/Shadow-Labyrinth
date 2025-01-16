import java.util.ArrayList;
import java.util.List;

public class TrapManager {
    private final List<Trap> traps;

    public TrapManager() {
        traps = new ArrayList<>();
    }

    // Initialize traps based on map configuration (tileType 4)
    public void initializeTraps(int[][] tileNumbers) {
        for (int row = 0; row < Variable.MAX_MAP_ROW; row++) {
            for (int col = 0; col < Variable.MAX_MAP_COLUMN; col++) {
                if (tileNumbers[row][col] == 4) {  // Trap tile
                    int trapX = col * Variable.ORIGINAL_TILE_SIZE;
                    int trapY = row * Variable.ORIGINAL_TILE_SIZE;

                    if (Trap.findTrap(traps, trapX, trapY) == null) {
                        traps.add(new Trap(trapX, trapY));
                    }
                }
            }
        }
    }

    // Draw traps and animate them
    public void drawTraps(int cameraX, int cameraY) {
        for (Trap trap : traps) {
            trap.draw(cameraX, cameraY);
        }
    }

    // Check for trap activation separately (for animations)
    public boolean checkTrapActivation(Player player) {
        boolean activated = false;
        for (Trap trap : traps) {
            if (trap.checkCollision(player.worldX, player.worldY)) {
                trap.activateTrap();
                activated = true;
            }
        }
        return activated;
    }

    // Check only for health collision logic
    public boolean checkHealthCollision(Player player) {
        for (Trap trap : traps) {
            if (trap.checkCollision(player.worldX, player.worldY)) {
                return true;
            }
        }
        return false;
    }
}
