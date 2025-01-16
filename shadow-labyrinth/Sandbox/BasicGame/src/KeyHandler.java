import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.KeyboardEvent;

import java.awt.event.KeyEvent;

public class KeyHandler {
    public static boolean upPressed, downPressed, leftPressed, rightPressed, isUpArrowPressed, isDownArrowPressed, isEnterPressed, isEscapePressed, isMiniMapPressed;
    public static boolean isSpacePressed = false;
    private int speed = 10; // Fixed SPEED
    private boolean toggleFrame = false;
    private static int miniMapState = 0;

    public static void keyPressed(KeyboardEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            upPressed = true;
            NPC.activateDialogue = false;
            Lighting.ENABLED = true;
        }
        if (key == KeyEvent.VK_S) {
            downPressed = true;
            NPC.activateDialogue = false;
            Lighting.ENABLED = true;
        }
        if (key == KeyEvent.VK_D) {
            rightPressed = true;
            NPC.activateDialogue = false;
            Lighting.ENABLED = true;
        }
        if (key == KeyEvent.VK_A) {
            leftPressed = true;
            NPC.activateDialogue = false;
            Lighting.ENABLED = true;
        }

        if (key == KeyEvent.VK_UP) {
            isUpArrowPressed = true;
            if (BasicGame.screenState == 0) {
                AudioHelper.play("shadow-labyrinth/Sandbox/resources/sounds/clickSound.wav", false);
            }

            UserInterface.commandNumber--;
            if (UserInterface.commandNumber < 0) {
                UserInterface.commandNumber = 3;
            }
        }

        if (key == KeyEvent.VK_DOWN) {
            isUpArrowPressed = true;
            if (BasicGame.screenState == 0) {
                AudioHelper.play("shadow-labyrinth/Sandbox/resources/sounds/clickSound.wav", false);
            }

            UserInterface.commandNumber++;
            if (UserInterface.commandNumber > 3) {
                UserInterface.commandNumber = 0;
            }
        }

        if (key == KeyEvent.VK_ENTER) {
            if (UserInterface.commandNumber == 0) {
                BasicGame.screenState = 1;

                isEnterPressed = true;
                // Start the timer once the game has started
                BasicGame.startTime = System.currentTimeMillis();
                BasicGame.timerStarted = true;
            }

            if (UserInterface.commandNumber == 1) {
                BasicGame.screenState = 2;
            }

            if (UserInterface.commandNumber == 2) {
                BasicGame.screenState = 6;
            }

            if (UserInterface.commandNumber == 3) {
                System.exit(0);
            }
        }
        // If the user presses the escape button on the leaderboard screen, go back to the main screen
        if (key == KeyEvent.VK_ESCAPE) {
            if (BasicGame.screenState == 2 || BasicGame.screenState == 6) {
                BasicGame.screenState = 0;
            }
        }

        // Assume this is within your keyboard event handler method
        if (key == KeyEvent.VK_M) {
            if (BasicGame.screenState == 1) {
                // Set screen state based on the toggled value
                BasicGame.screenState = 4; // Show the screen
            }
        }
        if (key == KeyEvent.VK_SPACE) {
            if (NPC.activateDialogue) {
                NPC.activateDialogue = false; // Close the dialogue when space is pressed
                UserInterface.dialogueOpen = true; // Ensure dialogueOpen is set to true
                Lighting.ENABLED = true;
            }
        }
    }

    public static void keyReleased(KeyboardEvent e) {
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
        if (key == KeyEvent.VK_UP) {
            isUpArrowPressed = false;
        }
        if (key == KeyEvent.VK_DOWN) {
            isDownArrowPressed = false;
        }
        if (key == KeyEvent.VK_ENTER) {
            isEnterPressed = false;
        }
        if (key == KeyEvent.VK_ESCAPE) {
            isEscapePressed = false;
        }

        if (key == KeyEvent.VK_M) {
            isMiniMapPressed = false;

            if (miniMapState == 1) {
                if (BasicGame.screenState == 0) {
                    BasicGame.screenState = 4;
                }
                miniMapState = 0;
            }
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
            player.imageFile = toggleFrame ? "shadow-labyrinth/Sandbox/resources/images/player/MCback.png" // ternary operator: condition ? valueIfTrue : valueIfFalse
                    : "shadow-labyrinth/Sandbox/resources/images/player/MCback2.png";
        } else if (downPressed) {
            player.ySpeed = speed;
            player.imageFile = toggleFrame ? "shadow-labyrinth/Sandbox/resources/images/player/MCfront.png" : "shadow-labyrinth/Sandbox/resources/images/player/MCfront2.png";
        } else {
            player.ySpeed = 0;
        }

        if (leftPressed) {
            player.xSpeed = -speed;
            player.imageFile = toggleFrame ? "shadow-labyrinth/Sandbox/resources/images/player/MCleft.png" : "shadow-labyrinth/Sandbox/resources/images/player/MCleft2.png";
        } else if (rightPressed) {
            player.xSpeed = speed;
            player.imageFile = toggleFrame ? "shadow-labyrinth/Sandbox/resources/images/player/MCright.png" : "shadow-labyrinth/Sandbox/resources/images/player/MCright2.png";
        } else {
            player.xSpeed = 0;
        }
    }

}