import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;
import tile.Map;

public class Main implements GameLoop {
    private Map map;
    private Player player;
    private Madara madara;
    private CharacterManager characterManager;
    private boolean[] keys = new boolean[256];

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new Main(), 1000, 1000, 40); // 1000, 1000 for window size, 40 for FPS
    }

    @Override
    public void init() {
        player = new Player();
        player.setDefaultValues();

        madara = new Madara();
        madara.setDefaultValues();

        characterManager = new CharacterManager(player, madara);

        map = new Map();
    }

    @Override
    public void loop() {
        SaxionApp.clear();

        map.draw();

        player.update(keys);
        player.draw();

        madara.update(player);
        madara.draw();

        characterManager.handleCharacterInteractions();

        characterManager.displayHealthStatus();
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
