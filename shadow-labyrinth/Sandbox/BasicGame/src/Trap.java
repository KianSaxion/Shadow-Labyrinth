import nl.saxion.app.SaxionApp;

import java.util.List;

public class Trap {
    private int x, y;
    private String[] trapImages = {
            "shadow-labyrinth/Sandbox/resources/images/traps/spike_1.png",
            "shadow-labyrinth/Sandbox/resources/images/traps/spike_2.png",
            "shadow-labyrinth/Sandbox/resources/images/traps/spike_3.png",
            "shadow-labyrinth/Sandbox/resources/images/traps/spike_4.png"
    };
    private int animationFrame = 0;
    private boolean activated = false; // Whether the trap has triggered
    private long triggerTime = 0; // To track when the trap was triggered

    public Trap(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(int cameraX, int cameraY) {
        if (activated) {
            long elapsedTime = System.currentTimeMillis() - triggerTime;

            // Transition through the spike images
            if (elapsedTime < 160) {
                animationFrame = 1; // spike_2
            } else if (elapsedTime < 320) {
                animationFrame = 2; // spike_3
            } else if (elapsedTime < 480) {
                animationFrame = 3; // spike_4
            } else {
                // After some time, return to static state (spike_1)
                resetTrap();
            }
        } else {
            // Static state (spike_1) before activation
            animationFrame = 0;
        }

        SaxionApp.drawImage(trapImages[animationFrame], x - cameraX + 10, y - cameraY + 10, 30, 30);
    }

    public boolean checkCollision(int playerX, int playerY) {
        return Math.abs(playerX - x) < 30 && Math.abs(playerY - y) < 40;
    }

    public void activateTrap() {
        if (!activated) {
            activated = true;
            triggerTime = System.currentTimeMillis();
        }
    }

    public void resetTrap() {
        activated = false;
        triggerTime = 0;
    }

    public static Trap findTrap(List<Trap> traps, int x, int y) {
        for (Trap trap : traps) {
            if (trap.getX() == x && trap.getY() == y) {
                return trap;
            }
        }
        return null;
    }
}
