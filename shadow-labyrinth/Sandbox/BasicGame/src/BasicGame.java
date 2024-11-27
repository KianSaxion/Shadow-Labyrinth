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
        grass.image = "shadow-labyrinth/Sandbox/resources/images/map/wall.png";

        tiles.add(grass);
    }

    public void drawMap() {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (col < 48 && row < 36) {
            SaxionApp.drawImage(tiles.get(0).image, x, y, 16, 16);
            col++;
            x += 16;

            if (col == 48) {
                col = 0;
                x = 0;
                row++;
                y += 16;
            }


        }
    }
}






