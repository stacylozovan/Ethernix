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

// This must be change later for the custom map
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
    boolean[] keys = new boolean[256]; // for tracking the keys

    @Override
    public void init() {
        player = new mainPlayer();
        player.x = 100;
        player.y = 100;
    }

    @Override
    public void loop() {
        SaxionApp.clear();

        drawBackground();

        // Draw the player
        SaxionApp.drawText(player.character, player.x, player.y, 70); // This will be changed in the future to the character image.
        handleMovement();
    }

    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        int keyCode = keyboardEvent.getKeyCode();

        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = keyboardEvent.isKeyPressed(); // checks what key is pressed and assigns it to the boolean variable
        }
    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }

    public void handleMovement() {

        if (keys[KeyboardEvent.VK_UP] || keys[KeyboardEvent.VK_W]) {
            player.y -= 10; // UP
        }

        if (keys[KeyboardEvent.VK_DOWN] || keys[KeyboardEvent.VK_S]) {
            player.y += 10; // DOWN
        }

        if (keys[KeyboardEvent.VK_LEFT] || keys[KeyboardEvent.VK_A]) {
            player.x -= 10; // LEFT
        }

        if (keys[KeyboardEvent.VK_RIGHT] || keys[KeyboardEvent.VK_D]) {
            player.x += 10; // RIGHT
        }
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
