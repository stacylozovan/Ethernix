import entity.CharacterManager;
import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;
import tile.Map;

public class Main implements GameLoop {
    private Map map;
    private CharacterManager characterManager;
    private boolean[] keys = new boolean[256];

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new Main(), 1000, 1000, 40);
    }

    @Override
    public void init() {
        characterManager = new CharacterManager();
        map = new Map();
    }

    @Override
    public void loop() {
        SaxionApp.clear();

        map.draw();

        characterManager.update(keys);
        characterManager.draw();

        characterManager.handleCharacterInteractions();
//        characterManager.displayHealthStatus();
    }

    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        int keyCode = keyboardEvent.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = keyboardEvent.isKeyPressed();
        }
    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {
    }
}
