import nl.saxion.app.SaxionApp;

public final class UserInterface {

    private static final String IMAGE_PATH = "shadow-labyrinth/Sandbox/resources/images/";

    // Image paths
    private static final String START_SCREEN = IMAGE_PATH + "screen/startingscreen.png";
    private static final String GAME_LOGO = IMAGE_PATH + "screen/gamelogo.png";
    private static final String PLAYER_FRONT = IMAGE_PATH + "player/MCfront.png";
    private static final String START_BUTTON = IMAGE_PATH + "screen/StartButton.png";
    private static final String LEADERBOARD = IMAGE_PATH + "screen/Leaderboard.png";
    private static final String KEYBOARD_MAP = IMAGE_PATH + "screen/KeyboardMap.png";
    private static final String EXIT_GAME = IMAGE_PATH + "screen/ExitGame.png";
    private static final String ARROW = IMAGE_PATH + "screen/arrow.png";

    // Arrow positions for each button
    private static final int[][] ARROW_COORDINATES = {
            {175, 275}, // Start button
            {175, 330}, // Leaderboard
            {175, 385}, // Keyboard Map
            {175, 445}  // Exit Game
    };

    // Button positions and sizes
    private static final int[][] BUTTON_COORDINATES = {
            {240, 235, 280, 120}, // Start button
            {233, 290, 300, 120}, // Leaderboard
            {237, 330, 295, 140}, // Keyboard Map
            {235, 400, 300, 120}  // Exit Game
    };

    public static int commandNumber = 0;

    // Function to draw the start screen
    public static void drawStartScreen() {
        SaxionApp.drawImage(START_SCREEN, 0, 0);
        SaxionApp.drawImage(GAME_LOGO, 290, 0, 200, 200);
        SaxionApp.drawImage(PLAYER_FRONT, 360, 200, 50, 50);

        // Draw buttons
        for (int i = 0; i < BUTTON_COORDINATES.length; i++) {
            SaxionApp.drawImage(getButtonImagePath(i),
                    BUTTON_COORDINATES[i][0],
                    BUTTON_COORDINATES[i][1],
                    BUTTON_COORDINATES[i][2],
                    BUTTON_COORDINATES[i][3]);

            // Draw arrow if the current button is selected
            if (commandNumber == i) {
                SaxionApp.drawImage(ARROW, ARROW_COORDINATES[i][0], ARROW_COORDINATES[i][1], 45, 30);
            }
        }
    }

    // Helper method to get the appropriate image path for each button
    private static String getButtonImagePath(int index) {
        return switch (index) {
            case 0 -> START_BUTTON;
            case 1 -> LEADERBOARD;
            case 2 -> KEYBOARD_MAP;
            case 3 -> EXIT_GAME;
            default -> throw new IllegalArgumentException("Invalid button index: " + index);
        };
    }
}
