import nl.saxion.app.SaxionApp;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

public class BasicGame implements GameLoop {

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new BasicGame(), 1000, 1000, 40);
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

    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }
}






