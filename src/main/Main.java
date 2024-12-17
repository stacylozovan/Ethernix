import entity.CharacterManager;
import entity.CombatSystemLogic;
import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

public class Main implements GameLoop {
    private tile.Map gameMap;
    private CharacterManager characterManager;
    private CombatSystemLogic combatSystem;

    private boolean[] keys = new boolean[256];
    private MainMenu mainMenu = new MainMenu();
    private boolean inMenu = true;
    private boolean gameStarted = false;

    private int cameraX = 0;
    private int cameraY = 0;

    private boolean inBattle = false;
    private boolean attackKeyPressed = false; // New flag for detecting attacks

    private String battleMapImage = "src/res/object/battlemap.png";

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new Main(), 1000, 1000, 40);
    }

    @Override
    public void init() {
        characterManager = new CharacterManager();
        gameMap = new tile.Map();
    }

    @Override
    public void loop() {
        SaxionApp.clear();

        if (mainMenu.isInSettings()) {
            SaxionApp.drawText("Settings", 150, 150, 50);
        } else if (inMenu) {
            mainMenu.drawMainMenu();
        } else if (gameStarted) {
            if (!inBattle) {
                updateCamera();
                checkForBattleTransition();
                gameMap.draw(cameraX, cameraY);
            } else {
                drawBattleScene();
            }

            if (!inBattle) {
                // Normal game loop
                characterManager.update(keys);
                int playerScreenX = characterManager.getPlayer().getX() - cameraX;
                int playerScreenY = characterManager.getPlayer().getY() - cameraY;
                characterManager.draw(playerScreenX, playerScreenY, cameraX, cameraY);
                characterManager.handleCharacterInteractions();
                characterManager.displayHealthStatus();
            } else {
                // Combat loop
                if (combatSystem.isBattleOver()) {
                    endBattle();
                }
            }
        }
    }

    private void updateCamera() {
        int screenWidth = 1000;
        int screenHeight = 1000;
        int tileSize = 50;

        cameraX = characterManager.getPlayer().getX() - screenWidth / 2;
        cameraY = characterManager.getPlayer().getY() - screenHeight / 2;

        int maxCameraX = gameMap.getWidth() * tileSize - screenWidth;
        int maxCameraY = gameMap.getHeight() * tileSize - screenHeight;

        cameraX = Math.max(0, Math.min(cameraX, maxCameraX));
        cameraY = Math.max(0, Math.min(cameraY, maxCameraY));
    }

    private void checkForBattleTransition() {
        if (characterManager.isPlayerNearMadara()) {
            switchToBattleMap();
        }
    }

    private void switchToBattleMap() {
        inBattle = true;

        combatSystem = new CombatSystemLogic(characterManager.getPlayer(), characterManager.getMadara());
        System.out.println("Transitioned to battle map. Combat starts!");
    }

    private void drawBattleScene() {

        SaxionApp.drawImage(battleMapImage, 0, 0, 1000, 1000);


        combatSystem.drawHealthBars();


        characterManager.getPlayer().draw(300, 800); // Player position
        characterManager.getMadara().draw(700, 400); // Madara position

        if (attackKeyPressed) {
            combatSystem.handleCombat();
            attackKeyPressed = false;
        }
    }

    private void endBattle() {
        inBattle = false;

        System.out.println("Battle ended. Returning to the regular map...");

        characterManager.getPlayer().setPosition(1180, 600);
        characterManager.getMadara().setPosition(1180, 300);
    }

    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        int keyCode = keyboardEvent.getKeyCode();

        if (inBattle && keyboardEvent.isKeyPressed() && keyCode == KeyboardEvent.VK_A) {
            attackKeyPressed = true;
        }

        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = keyboardEvent.isKeyPressed();
        }

        if (mainMenu.handlingKeyboardEscapeButton(keyboardEvent)) {
            inMenu = true;
        }
    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {
        if (inMenu) {
            if (mainMenu.mouseEvent(mouseEvent)) {
                inMenu = false;
                gameStarted = true;
            } else if (mainMenu.isInSettings()) {
                inMenu = false;
            }
        }
    }
}
