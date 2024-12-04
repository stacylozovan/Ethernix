import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;
import tile.Map;

public class main implements GameLoop {

    private Map map;
    private Player player;
    private boolean[] keys = new boolean[256];

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new main(), 1000, 1000, 40);
    }

    @Override
    public void init() {
        player = new Player();
        player.setDefaultValues();

        map = new Map();
    }

    @Override
    public void loop() {
        SaxionApp.clear();

        map.draw();

        player.update(keys);
        player.draw();
    }

    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        int keyCode = keyboardEvent.getKeyCode();

        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = keyboardEvent.isKeyPressed(); // Track key press states
        }
    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {
    }
}
