import nl.saxion.app.SaxionApp;

public final class UserInterface {
    // Function to draw a start screen
    public static void drawStartScreen() {
        SaxionApp.drawImage("shadow-labyrinth/Sandbox/resources/images/screen/startingscreen.png", 0, 0);
        SaxionApp.drawImage("shadow-labyrinth/Sandbox/resources/images/screen/gamelogo.png", 290, 0, 200, 200);
        SaxionApp.drawImage("shadow-labyrinth/Sandbox/resources/images/player/MCfront.png", 360, 200, 50, 50);

        int x, y;
        String text = "Play game";
        SaxionApp.drawRectangle(320, 300, 135, 50);
    }
}
