import entity.AudioHelper;
import entity.CharacterManager;
import entity.CombatSystemLogic;
import entity.NPC;
import main.CollisionChecker;
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
    private AudioHelper audioHelper;

    private int cameraX = 0;
    private int cameraY = 0;

    private boolean inBattle = false;
    private boolean attackKeyPressed = false;
    private String battleMapImage = "src/res/object/battlemap1.png";

    private boolean interactingWithNPC = false;
    private NPC currentInteractingNPC;

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new Main(), 1000, 1000, 40);
    }

    @Override
    public void init() {
        gameMap = new tile.Map();
        CollisionChecker collisionChecker = new CollisionChecker(gameMap);
        characterManager = new CharacterManager(collisionChecker);
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
                updateOverworld();
            } else {
                updateBattle();
            }
        }
    }

    private void updateOverworld() {
        updateCamera();
        checkForBattleTransition();

        gameMap.draw(cameraX, cameraY);
        characterManager.update(keys, gameMap);
        characterManager.draw(cameraX, cameraY);

        int playerScreenX = characterManager.getActivePlayer().getX() - cameraX;
        int playerScreenY = characterManager.getActivePlayer().getY() - cameraY;
        handleNPCInteractions(playerScreenX, playerScreenY);

        characterManager.handleCharacterInteractions();
        characterManager.displayHealthStatus();

        playBackgroundMusic();
    }

    private void updateBattle() {
        drawBattleScene();

        if (combatSystem.isBattleOver()) {
            endBattle();
        }
    }

    private void playBackgroundMusic() {
        String[] songs = {
                "src/res/audio/first_map_audio_1.wav",
                "src/res/audio/first_map_audio_2.wav",
                "src/res/audio/first_map_audio_3.wav"
        };

        if (!AudioHelper.isPlaying() || !AudioHelper.isSongInArray(AudioHelper.getFilename(), songs)) {
            int randomIndex = SaxionApp.getRandomValueBetween(0, 3);
            String selectedSong = songs[randomIndex];
            AudioHelper.newSong(selectedSong, false);
        }
    }

    private void handleNPCInteractions(int playerScreenX, int playerScreenY) {
        if (interactingWithNPC && currentInteractingNPC != null) {
            if (!currentInteractingNPC.isPlayerNear(characterManager.getActivePlayer().getX(), characterManager.getActivePlayer().getY())) {
                interactingWithNPC = false;
                currentInteractingNPC = null;
                return;
            }

            boolean isKeyPressed = keys[KeyboardEvent.VK_E] || keys[KeyboardEvent.VK_SPACE];
            currentInteractingNPC.interact(isKeyPressed);

            if (keys[KeyboardEvent.VK_SPACE] && currentInteractingNPC.dialogue.length == currentInteractingNPC.currentDialogueIndex) {
                interactingWithNPC = false;
                currentInteractingNPC = null;
            }
        } else {
            for (NPC npc : CharacterManager.npcs) {
                if (npc.isVisible) {
                    if (npc.isPlayerNear(characterManager.getActivePlayer().getX(), characterManager.getActivePlayer().getY()) && keys[KeyboardEvent.VK_E]) {
                        interactingWithNPC = true;
                        currentInteractingNPC = npc;
                        break;
                    }
                }
            }
        }
    }

    private void updateCamera() {
        int screenWidth = 1000;
        int screenHeight = 1000;
        int tileSize = 50;

        cameraX = characterManager.getNaruto().getX() - screenWidth / 2;
        cameraY = characterManager.getNaruto().getY() - screenHeight / 2;

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
        if (characterManager.getNaruto() == null || characterManager.getGojo() == null || characterManager.getMadara() == null) {
            return;
        }

        inBattle = true;

        combatSystem = new CombatSystemLogic(
                characterManager.getNaruto(),
                characterManager.getGojo(),
                characterManager.getMadara()
        );

        combatSystem.startBattle();
    }

    private void drawBattleScene() {
        SaxionApp.drawImage(battleMapImage, 0, 0, 1000, 1000);

        combatSystem.drawHealthBars();
        combatSystem.drawBattleField();

        if (attackKeyPressed) {
            combatSystem.handleCombat();
            attackKeyPressed = false;
        }
    }

    private void endBattle() {
        inBattle = false;

        combatSystem.endBattle();

        characterManager.getNaruto().setPosition(1180, 600);
        characterManager.getMadara().setPosition(1180, 300);
    }

    @Override
    public void keyboardEvent(KeyboardEvent keyboardEvent) {
        int keyCode = keyboardEvent.getKeyCode();

        if (inBattle) {
            if (keyboardEvent.isKeyPressed()) {
                if (keyCode == KeyboardEvent.VK_A) {
                    attackKeyPressed = true;
                }

                if (keyCode == KeyboardEvent.VK_1) {
                    combatSystem.switchPlayer(1);
                }

                if (keyCode == KeyboardEvent.VK_2) {
                    combatSystem.switchPlayer(2);
                }
            }
        }

        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = keyboardEvent.isKeyPressed();
        }

        if (!keyboardEvent.isKeyPressed() && interactingWithNPC && currentInteractingNPC != null) {
            currentInteractingNPC.releaseKey();
        }

        if (mainMenu.handlingKeyboardEscapeButton(keyboardEvent)) {
            inMenu = true;
        }
    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {
        if (inBattle && mouseEvent.isMouseDown() && mouseEvent.isLeftMouseButton()) {
            attackKeyPressed = true;
        }

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
