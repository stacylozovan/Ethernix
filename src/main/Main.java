import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

public class Main implements GameLoop {

    private Map map;
    private Player player;
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
        player = new Player();
        player.setDefaultValues();

        map = new Map();
    }

    @Override
    public void loop() {
        SaxionApp.clear();

        updateCamera();

        map.draw(cameraX, cameraY);

        player.update(keys);
        player.draw(screenWidth / 2, screenHeight / 2);
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
            keys[keyCode] = keyboardEvent.isKeyPressed(); // Track key press states
        }
    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {
    }
}
