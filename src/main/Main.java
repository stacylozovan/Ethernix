import entity.CharacterManager;
import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

public class Main implements GameLoop {
    private Map map;
    private CharacterManager characterManager;
    private boolean[] keys = new boolean[256];

    private int cameraX = 0;
    private int cameraY = 0;
    private int screenWidth = 1000;
    private int screenHeight = 1000;
    private int tileSize = 50;

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

        updateCamera();

        map.draw(cameraX, cameraY);

        player.update(keys, map.getWidth(), map.getHeight(), tileSize);

        int playerScreenX = player.x - cameraX;
        int playerScreenY = player.y - cameraY;
        player.draw(playerScreenX, playerScreenY);

        characterManager.update(keys);
        characterManager.draw();

        characterManager.handleCharacterInteractions();

        characterManager.displayHealthStatus();
    }


    private void updateCamera() {
        cameraX = player.x - screenWidth / 2;
        cameraY = player.y - screenHeight / 2;

        int maxCameraX = map.getWidth() * tileSize - screenWidth;
        int maxCameraY = map.getHeight() * tileSize - screenHeight;

        cameraX = Math.max(0, Math.min(cameraX, maxCameraX));
        cameraY = Math.max(0, Math.min(cameraY, maxCameraY));
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
