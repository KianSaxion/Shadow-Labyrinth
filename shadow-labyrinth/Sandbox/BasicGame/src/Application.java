import nl.saxion.app.SaxionApp;

public class Application implements Runnable {

    public static void main(String[] args) {
        SaxionApp.start(new Application(), 768, 576);
    }

    public void run() {
        SaxionApp.drawImage("shadow-labyrinth/Sandbox/resources/images/screen/startingscreen.png", 0, 0);
    }
}
