import nl.saxion.app.SaxionApp;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;
import java.awt.event.KeyEvent;

import java.util.ArrayList;

public class BasicGame implements GameLoop {
    ArrayList<Map> tiles = new ArrayList<>();
    final int FINAL_SCALE = 48;
    final int MAX_SCREEN_COL = 48;
    final int MAX_SCREEN_ROW = 48;
    Cookiemonster cookiemonster;

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new BasicGame(), 768, 576, 40);
    }

    @Override
    public void init() {
        cookiemonster = new Cookiemonster();
        cookiemonster.x = 100;
        cookiemonster.y = 100;

        loadMap();
    }


    @Override
    public void loop() {

        SaxionApp.clear();

        drawMap();
        SaxionApp.drawImage(cookiemonster.imageFile, cookiemonster.x, cookiemonster.y, FINAL_SCALE, FINAL_SCALE);
        cookiemonster.x += cookiemonster.xSpeed;
        cookiemonster.y += cookiemonster.ySpeed;
    }

    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        if (keyboardEvent.isKeyPressed()) {
            if (keyboardEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                if (cookiemonster.xSpeed > 0) {
                    cookiemonster.xSpeed = 0;
                } else {
                    cookiemonster.xSpeed -= 1;
                }
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                if(cookiemonster.xSpeed < 0) {
                    cookiemonster.xSpeed = 0;
                } else {
                    cookiemonster.xSpeed += 1;
                }
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_UP) {
                cookiemonster.ySpeed -= 1;
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                cookiemonster.ySpeed += 1;
            }
        }
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
            SaxionApp.drawImage(tiles.get(0).image, x, y, FINAL_SCALE, FINAL_SCALE);
            col++;
            x += MAX_SCREEN_ROW;

            if (col == 48) {
                col = 0;
                x = 0;
                row++;
                y += MAX_SCREEN_COL;
            }
        }
    }
}






