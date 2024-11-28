import nl.saxion.app.SaxionApp;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

public class main implements GameLoop {

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new main(), 1000, 1000, 40);
    }

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
        SaxionApp.drawText(player.character, player.x, player.y, 70); // This will be changed in the future to the character image.
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
}






