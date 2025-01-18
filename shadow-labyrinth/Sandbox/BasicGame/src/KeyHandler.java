import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.KeyboardEvent;

import java.awt.event.KeyEvent;

public class KeyHandler {
    public static boolean upPressed, downPressed, leftPressed, rightPressed, isUpArrowPressed, isDownArrowPressed, isEnterPressed, isEscapePressed, isMiniMapPressed, isEPressed;
    public static int speed = 10; // Fixed SPEED
    public static boolean isSpacePressed = false;
    private boolean toggleFrame = false;
    private static int miniMapState = 0;
    public static boolean firsTime = true;
    //    private static String playerPath = "shadow-labyrinth/Sandbox/resources/images/player/";
    private static String playerWithSwordPath = "shadow-labyrinth/Sandbox/resources/images/playerSwordAnimation/";

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

                if (firsTime) {
                    // Start the timer once the game has started
                    BasicGame.startTime = System.currentTimeMillis();
                    BasicGame.timerStarted = true;

                    firsTime = false;
                }


                for (Monster monster : Monster.Monsters) {
                    monster.alive = true;
                }
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

        if (key == KeyEvent.VK_E) {
            isEPressed = true;
        }
    }


    public static void hide() {
        if (NPC.activateDialogue) {
            NPC.activateDialogue = false; // Close the dialogue when space is pressed
            UserInterface.dialogueOpen = true; // Ensure dialogueOpen is set to true
            Lighting.ENABLED = true;
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

        if (key == KeyEvent.VK_E) {
            isEPressed = false;
        }
    }

    private int frameCounter = 0; // Used to control frame switching


    // Class-level variable to persist the last movement direction
    private String lastDirection = "down"; // Default starting direction

    public void update(Player player) {
        if (upPressed) lastDirection = "up";
        if (downPressed) lastDirection = "down";
        if (leftPressed) lastDirection = "left";
        if (rightPressed) lastDirection = "right";

        frameCounter++;
        if (frameCounter >= 10) { // Adjust the threshold for slower animation
            toggleFrame = !toggleFrame; // Flip the toggle
            frameCounter = 0; // Reset counter
        }

        if (upPressed) {
            player.ySpeed = -speed;
            player.imageFile = toggleFrame ? "shadow-labyrinth/Sandbox/resources/images/player/MCback.png" // ternary operator: condition ? valueIfTrue : valueIfFalse
                    : "shadow-labyrinth/Sandbox/resources/images/player/MCback2.png";

            if (isEPressed) {
                player.imageFile = playerWithSwordPath + "MCback_swordDown.png";
            }
        } else if (downPressed) {
            player.ySpeed = speed;
            player.imageFile = toggleFrame ? "shadow-labyrinth/Sandbox/resources/images/player/MCfront.png" : "shadow-labyrinth/Sandbox/resources/images/player/MCfront2.png";
            if (isEPressed) {
                player.imageFile = playerWithSwordPath + "MCfront_swordDown.png";
            }
        } else {
            player.ySpeed = 0;

            // Handle standing still with or without sword
            if (isEPressed) {
                // Display sword down based on last direction
                player.imageFile = switch (lastDirection) {
                    case "up" -> playerWithSwordPath + "MCback_swordDown.png";
                    case "down" -> playerWithSwordPath + "MCfront_swordDown.png";
                    case "left" -> playerWithSwordPath + "MCleft_swordDown.png";
                    case "right" -> playerWithSwordPath + "MCright_swordDown.png";
                    default -> player.imageFile; // Default case if no direction
                };
            } else {
                // Display regular standing image based on last direction
                player.imageFile = switch (lastDirection) {
                    case "up" -> "shadow-labyrinth/Sandbox/resources/images/player/MCback.png";
                    case "down" -> "shadow-labyrinth/Sandbox/resources/images/player/MCfront.png";
                    case "left" -> "shadow-labyrinth/Sandbox/resources/images/player/MCleft.png";
                    case "right" -> "shadow-labyrinth/Sandbox/resources/images/player/MCright.png";
                    default -> player.imageFile; // Default case if no direction
                };
            }
        }


        if (leftPressed) {
            player.xSpeed = -speed;
            player.imageFile = toggleFrame ? "shadow-labyrinth/Sandbox/resources/images/player/MCleft.png" : "shadow-labyrinth/Sandbox/resources/images/player/MCleft2.png";
            if (isEPressed) {
                player.imageFile = playerWithSwordPath + "MCleft_swordDown.png";
            }
        } else if (rightPressed) {
            player.xSpeed = speed;
            player.imageFile = toggleFrame ? "shadow-labyrinth/Sandbox/resources/images/player/MCright.png" : "shadow-labyrinth/Sandbox/resources/images/player/MCright2.png";
            if (isEPressed) {
                player.imageFile = playerWithSwordPath + "MCright_swordDown.png";
            }
        } else {
            player.xSpeed = 0;
        }
    }

}