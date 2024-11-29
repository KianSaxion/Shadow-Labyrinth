import nl.saxion.app.SaxionApp;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;
import java.awt.event.KeyEvent;

import java.util.ArrayList;

public class BasicGame implements GameLoop {
    ArrayList<Map> tiles = new ArrayList<>();
    final int MAX_SCREEN_COL = 16;
    final int MAX_SCREEN_ROW = 16;
    Player player;

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new BasicGame(), 768, 576, 40);
    }

    @Override
    public void init() {
        player = new Player();
        player.x = 100;
        player.y = 100;
    }


    @Override
    public void loop() {
        SaxionApp.clear();
        loadMap();
        drawMap();
        SaxionApp.drawImage(player.imageFile, player.x, player.y, 32, 32);
        player.x += player.xSpeed;
        player.y += player.ySpeed;
    }

    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        if (keyboardEvent.isKeyPressed()) {
            if (keyboardEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                if (player.xSpeed > 0) {
                    player.xSpeed = 0;
                } else {
                    player.xSpeed -= 1;
                }
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                if(player.xSpeed < 0) {
                    player.xSpeed = 0;
                } else {
                    player.xSpeed += 1;
                }
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_UP) {
                player.ySpeed -= 1;
            } else if (keyboardEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                player.ySpeed += 1;
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
            SaxionApp.drawImage(tiles.get(0).image, x, y, MAX_SCREEN_ROW, MAX_SCREEN_COL);
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






