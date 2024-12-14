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
    private MainMenu mainMenu = new MainMenu();
    private boolean inMenu = true;
    private boolean gameStarted = false;

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

        if (mainMenu.isInSettings()) {
            SaxionApp.drawText("Settings", 150,150,50); // if I click the settings button, this will be changed into a settings method later

        } else if (inMenu) {
            mainMenu.drawMainMenu();

        } else if (gameStarted) {
            map.draw();

            characterManager.update(keys);
            characterManager.draw();

            characterManager.handleCharacterInteractions();

            characterManager.displayHealthStatus();
        }
    }

    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        int keyCode = keyboardEvent.getKeyCode();
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = keyboardEvent.isKeyPressed();
        }

        if (mainMenu.handlingKeyboardEscapeButton(keyboardEvent)) {
            inMenu = true; // if we click ESC, the main menu appears
        }
    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {
        if (inMenu) {
            // If the mouse event returns true, start the game
            if (mainMenu.mouseEvent(mouseEvent)) {
                inMenu = false;
                gameStarted = true;
            }

            else if (mainMenu.isInSettings()) {
                // Show settings screen
                inMenu = false;
            }
        }
    }

}
