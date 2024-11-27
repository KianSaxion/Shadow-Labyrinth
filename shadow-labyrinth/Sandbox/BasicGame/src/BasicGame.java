import nl.saxion.app.SaxionApp;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.awt.*;
import java.util.ArrayList;

public class BasicGame implements GameLoop {
    ArrayList<Map> tiles = new ArrayList<>();
    public static void main(String[] args) {
        SaxionApp.startGameLoop(new BasicGame(), 768, 576, 40);
    }

    @Override
    public void init() {
    }


    @Override
    public void loop() {
        loadMap();
        drawMap();
    }

    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {

    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }

    public void loadMap() {
        Map grass = new Map();
        grass.image = "Shadow-Labyrinth\\shadow-labyrinth\\Sandbox\\resources\\images\\map\\grass00.png";

        tiles.add(grass);
    }
    public void drawMap () {
        SaxionApp.drawImage(tiles.get(0).image, 100, 100);
    }

}






