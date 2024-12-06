import nl.saxion.app.interaction.KeyboardEvent;
import java.awt.event.KeyEvent;

public class KeyHandler {
    boolean upPressed, downPressed, leftPressed, rightPressed;
    private final int speed = 5; // Fixed speed

    public void keyPressed(KeyboardEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (key == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (key == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (key == KeyEvent.VK_A) {
            leftPressed = true;
        }
    }

    public void keyReleased(KeyboardEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (key == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (key == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (key == KeyEvent.VK_A) {
            leftPressed = false;
        }
    }

    public void update(Player player) {
        if (upPressed) {
            player.ySpeed = -speed;
        } else if (downPressed) {
            player.ySpeed = speed;
        } else {
            player.ySpeed = 0;
        }

        if (leftPressed) {
            player.xSpeed = -speed;
        } else if (rightPressed) {
            player.xSpeed = speed;
        } else {
            player.xSpeed = 0;
        }
    }
}