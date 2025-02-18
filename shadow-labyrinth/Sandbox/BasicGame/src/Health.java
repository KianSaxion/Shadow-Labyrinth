import nl.saxion.app.SaxionApp;

import java.util.ArrayList;

public class Health {
    private static final int MAX_HEALTH = 10; // Maximum health (5 hearts, each heart = 2 health)
    private static int currentHealth = MAX_HEALTH;
    private static ArrayList<String> hearts = new ArrayList<>();

    public Health() {
        resetHearts();
    }

    public void draw() {
        for (int i = 0; i < hearts.size(); i++) {
            SaxionApp.drawImage(hearts.get(i), 10 + i * 35, 10, 30, 30);
        }
    }

    // Reset health to maximum and update the hearts display
    public void resetHealth() {
        currentHealth = MAX_HEALTH;
        resetHearts();
    }


    // Update health and return whether the health was reduced
    public static boolean reduceHealth() {
        if (currentHealth > 0) {
            currentHealth--;
            updateHearts();
            return true;
        }
        return false;
    }

    private void resetHearts() {
        hearts.clear();
        for (int i = 0; i < MAX_HEALTH / 2; i++) {
            hearts.add("shadow-labyrinth/Sandbox/resources/images/Hearts/heart_full.png");
        }
    }

    // Update the hearts display based on current health
    public static void updateHearts() {
        int fullHearts = currentHealth / 2;
        int halfHeart = currentHealth % 2;

        hearts.clear();
        for (int i = 0; i < fullHearts; i++) {
            hearts.add("shadow-labyrinth/Sandbox/resources/images/Hearts/heart_full.png");
        }
        if (halfHeart > 0) {
            hearts.add("shadow-labyrinth/Sandbox/resources/images/Hearts/heart_half.png");
        }
    }

    // Return true if the player's health is 0
    public boolean isGameOver() {
        return currentHealth == 0;
    }
}
