import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.KeyboardEvent;
import java.awt.event.KeyEvent;

public class KeyHandler {
    boolean upPressed, downPressed, leftPressed, rightPressed, isUpArrowPressed, isDownArrowPressed, isEnterPressed;
    private int speed = 10; // Fixed SPEED
    private boolean toggleFrame = false;

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

        if (key == KeyEvent.VK_UP) {
            if (BasicGame.screenState == 0) {
                AudioHelper.play("shadow-labyrinth/Sandbox/resources/sounds/clickSound.wav", false);
            }
            isUpArrowPressed = true;
            UserInterface.commandNumber--;
            if (UserInterface.commandNumber < 0) {
                UserInterface.commandNumber = 3;
            }
        }

        if (key == KeyEvent.VK_DOWN) {
            if (BasicGame.screenState == 0) {
                AudioHelper.play("shadow-labyrinth/Sandbox/resources/sounds/clickSound.wav", false);
            }

            isDownArrowPressed = true;
            UserInterface.commandNumber++;
            if (UserInterface.commandNumber > 3) {
                UserInterface.commandNumber = 0;
            }
        }

        if (key == KeyEvent.VK_ENTER) {
            isEnterPressed = true;
            if (UserInterface.commandNumber == 0) {
                BasicGame.screenState = 1;

                // Start the timer once the game has started
                BasicGame.startTime = System.currentTimeMillis();
                BasicGame.timerStarted = true;
            }

            if (UserInterface.commandNumber == 1) {
                BasicGame.screenState = 2;
            }

            if (UserInterface.commandNumber == 3) {
                System.exit(0);
            }
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

    private int frameCounter = 0; // Used to control frame switching

    public void update(Player player) {
        frameCounter++;
        if (frameCounter >= 10) { // Adjust the threshold for slower animation
            toggleFrame = !toggleFrame; // Flip the toggle
            frameCounter = 0; // Reset counter
        }

        if (upPressed) {
            player.ySpeed = -speed;
            player.imageFile = toggleFrame
                    ? "shadow-labyrinth/Sandbox/resources/images/player/MCback.png" // ternary operator: condition ? valueIfTrue : valueIfFalse
                    : "shadow-labyrinth/Sandbox/resources/images/player/MCback2.png";
        } else if (downPressed) {
            player.ySpeed = speed;
            player.imageFile = toggleFrame
                    ? "shadow-labyrinth/Sandbox/resources/images/player/MCfront.png"
                    : "shadow-labyrinth/Sandbox/resources/images/player/MCfront2.png";
        } else {
            player.ySpeed = 0;
        }

        if (leftPressed) {
            player.xSpeed = -speed;
            player.imageFile = toggleFrame
                    ? "shadow-labyrinth/Sandbox/resources/images/player/MCleft.png"
                    : "shadow-labyrinth/Sandbox/resources/images/player/MCleft2.png";
        } else if (rightPressed) {
            player.xSpeed = speed;
            player.imageFile = toggleFrame
                    ? "shadow-labyrinth/Sandbox/resources/images/player/MCright.png"
                    : "shadow-labyrinth/Sandbox/resources/images/player/MCright2.png";
        } else {
            player.xSpeed = 0;
        }
    }
}