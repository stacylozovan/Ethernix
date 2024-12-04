import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;
import tile.Map;

public class main implements GameLoop {

    private Map map;
    private mainPlayer player;
    private itachi_boss itachi; // Add the boss
    private boolean[] keys = new boolean[256]; // For tracking key presses

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new main(), 1000, 1000, 40);
    }

    @Override
    public void init() {
        // Initialize player with a starting position and image path
        player = new mainPlayer("Player", 500, 500, "src/main/character/gojo.jpg"); // Set the player's image path

        // Initialize the boss (itachi_boss) with image path
        itachi = new itachi_boss("Itachi", 300, 300, "src/main/character/itachi.png"); // Set the boss's image path


        map = new Map();
    }

    @Override
    public void loop() {
        SaxionApp.clear(); // Clear the screen before drawing each frame

        // Draw the map
        map.draw();

        player.draw(); // Draw the player at the current position

        itachi.draw(); // Draw the boss at the current position

        // Call the boss AI behavior to make the boss move towards the player (optional)
        itachi.aiBehavior(player);

        // Handle player movement
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
            player.move(0, -10); // Move player up
        }
        if (keys[KeyboardEvent.VK_DOWN] || keys[KeyboardEvent.VK_S]) {
            player.move(0, 10); // Move player down
        }
        if (keys[KeyboardEvent.VK_LEFT] || keys[KeyboardEvent.VK_A]) {
            player.move(-10, 0); // Move player left
        }
        if (keys[KeyboardEvent.VK_RIGHT] || keys[KeyboardEvent.VK_D]) {
            player.move(10, 0); // Move player right
        }
    }
}
