import nl.saxion.app.SaxionApp;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

public class main implements GameLoop {

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new main(), 1000, 1000, 40);
    }



    public static final int TILE_SIZE = 64;
//    public static final int GRID_WIDTH = 32;
//    public static final int GRID_HEIGHT = 32;

    public static final String[] TILE_IMAGES = {
            "src/object/tiles/grass.png",
            "src/object/tiles/tree.png",
            "src/object/tiles/wall.png",
            "src/object/tiles/sand.png",
    };


    public static final int[][] TILE_MAP = {
            {0, 0, 1, 1, 1, 0, 0, 0, 3, 3, 1},
            {0, 0, 1, 2, 2, 0, 0, 3, 3, 3},
            {1, 1, 2, 2, 2, 1, 1, 3, 3, 0},
            {2, 2, 0, 0, 2, 1, 1, 0, 3, 0},
            {2, 1, 1, 0, 0, 3, 3, 0, 0, 0},
            {0, 0, 1, 1, 3, 3, 0, 0, 0, 0},
            {0, 3, 3, 1, 1, 0, 0, 0, 2, 2},
            {0, 0, 2, 0, 0, 1, 1, 0, 3, 2},
            {3, 2, 0, 1, 1, 3, 3, 0, 0, 0},
            {3, 0, 0, 0, 2, 0, 3, 3, 2, 2}
    };

    mainPlayer player;

    @Override
    public void init() {
        player = new mainPlayer();
        player.x = 100;
        player.y = 100;
    }

    @Override
    public void loop() { //Naufal the goat
        SaxionApp.clear();

        drawBackground();

        // Draw the player
        SaxionApp.drawText(player.character, player.x, player.y, 70); // Placeholder for player image
    }

    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        if (keyboardEvent.isKeyPressed()) {
            if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_UP || keyboardEvent.getKeyCode() == KeyboardEvent.VK_W) {
                player.y = player.y - 4; // UP
            } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_DOWN || keyboardEvent.getKeyCode() == KeyboardEvent.VK_S) {
                player.y = player.y + 4; // DOWN
            } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_LEFT || keyboardEvent.getKeyCode() == KeyboardEvent.VK_A) {
                player.x = player.x - 4; // LEFT
            } else if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_RIGHT || keyboardEvent.getKeyCode() == KeyboardEvent.VK_D) {
                player.x = player.x + 4; // RIGHT
            }
        }
    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }

    public void drawBackground() {
        for (int row = 0; row < TILE_MAP.length; row++) {
            for (int col = 0; col < TILE_MAP[row].length; col++) {
                int x = col * TILE_SIZE;
                int y = row * TILE_SIZE;

                int tileType = TILE_MAP[row][col];

                String tileImage = TILE_IMAGES[tileType];

                SaxionApp.drawImage(tileImage, x, y, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}
