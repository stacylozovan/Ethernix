import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;
import tile.Map;

public class main implements GameLoop {

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new main(), 1000, 1000, 40);
    }

    private Map map;
    private mainPlayer player;
    private boolean[] keys = new boolean[256]; // For tracking key presses

    @Override
    public void init() {
        player = new mainPlayer();
        player.x = 500;
        player.y = 500;

        map = new Map();
    }

    @Override
    public void loop() {
        SaxionApp.clear();

        map.draw();

        // Draw the player
        SaxionApp.drawText(player.character, player.x, player.y, 70); // Replace with player image if needed


        handleMovement();
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

    public void handleMovement() {
        if (keys[KeyboardEvent.VK_UP] || keys[KeyboardEvent.VK_W]) {
            player.y -= 10; // Move player up
        }
        if (keys[KeyboardEvent.VK_DOWN] || keys[KeyboardEvent.VK_S]) {
            player.y += 10; // Move player down
        }
        if (keys[KeyboardEvent.VK_LEFT] || keys[KeyboardEvent.VK_A]) {
            player.x -= 10; // Move player left
        }
        if (keys[KeyboardEvent.VK_RIGHT] || keys[KeyboardEvent.VK_D]) {
            player.x += 10; // Move player right
        }
    }
}
