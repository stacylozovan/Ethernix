import nl.saxion.app.SaxionApp;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

public class main implements GameLoop {

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new main(), 1000, 1000, 40);
    }

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
}






