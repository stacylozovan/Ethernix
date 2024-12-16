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

        characterManager.update(keys);
        int playerScreenX = characterManager.getPlayer().getX() - cameraX;
        int playerScreenY = characterManager.getPlayer().getY() - cameraY;
        characterManager.draw(playerScreenX, playerScreenY, cameraX, cameraY);
        characterManager.handleCharacterInteractions();
        characterManager.displayHealthStatus();
    }

    private void updateCamera() {
        int screenWidth = 1000;
        int screenHeight = 1000;
        int tileSize = 50;

        cameraX = characterManager.getPlayer().getX() - screenWidth / 2;
        cameraY = characterManager.getPlayer().getY() - screenHeight / 2;

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
