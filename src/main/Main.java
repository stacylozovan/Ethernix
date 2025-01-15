import entity.*;
import main.CollisionChecker;
import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

public class Main implements GameLoop {
    private tile.Map introMap;
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

    private boolean isIntroScene = true;
    private boolean transitioningToNextScene = false;
    private int transitionStep = 0;
    private long transitionStartTime = 0;

    private boolean inBattle = false;
    private boolean attackKeyPressed = false;
    private String battleMapImage = "src/res/object/battlemap1.png";

    private boolean interactingWithNPC = false;
    private NPC currentInteractingNPC;

    private int originalPlayerX = 0;
    private int originalPlayerY = 0;
    private int blinkCount = 0;

    private String currentSong = null;

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new Main(), 1000, 1000, 40);
    }

    @Override
    public void init() {
        introMap = new tile.Map("/object/intro_map.txt");
        CollisionChecker collisionChecker = new CollisionChecker(introMap);
        characterManager = new CharacterManager(collisionChecker, "intro_scene");
    }

    @Override
    public void loop() {
        SaxionApp.clear();

        if (mainMenu.isInSettings()) {
            SaxionApp.drawText("Settings", 150, 150, 50);
        } else if (inMenu) {
            mainMenu.drawMainMenu();
        } else if (isIntroScene) {
            updateIntroScene();
        } else if (gameStarted) {
            if (!inBattle) {
                updateOverworld();
            } else {
                updateBattle();
            }
        }
    }

    private void updateIntroScene() {
        updateCamera(introMap);
        introMap.draw(cameraX, cameraY);
        characterManager.update(keys, introMap);
        characterManager.draw(cameraX, cameraY);

        int playerScreenX = characterManager.getNaruto().getX() - cameraX;
        int playerScreenY = characterManager.getNaruto().getY() - cameraY;
        handleNPCInteractions(playerScreenX, playerScreenY);

        playBackgroundMusic();

        if (!interactingWithNPC && isIntroScene && !transitioningToNextScene) {
            NPC kakashi = characterManager.getNPCByName("kakashi");
            if (kakashi != null && kakashi.dialogue.length == kakashi.currentDialogueIndex) {
                transitioningToNextScene = true;
                transitionStep = 0;
                transitionStartTime = System.currentTimeMillis();
                kakashi.isVisible = false;
            }
        }
        if (transitioningToNextScene) {
            handleTransition();
            System.out.println("NextScene!!");
        }
    }

    private void handleTransition() {
        long currentTime = System.currentTimeMillis();

        switch (transitionStep) {
            case 0:
                if (currentTime - transitionStartTime >= 1000) {
                    Player naruto = characterManager.getNaruto();
                    originalPlayerX = naruto.getX();
                    originalPlayerY = naruto.getY();

                    transitionStartTime = currentTime;
                    transitionStep = 1;
                }
                break;

            case 1:
                if (currentTime - transitionStartTime < 2000) {
                    int shakeX = (int) (Math.random() * 10 - 5);
                    int shakeY = (int) (Math.random() * 10 - 5);

                    Player naruto = characterManager.getNaruto();
                    naruto.setPosition(originalPlayerX + shakeX, originalPlayerY + shakeY);
                } else {
                    Player naruto = characterManager.getNaruto();
                    naruto.setPosition(originalPlayerX, originalPlayerY);

                    transitionStartTime = currentTime;
                    transitionStep = 2;
                }
                break;

            case 2:
                int blinkDuration = 500 - (blinkCount * 50);
                blinkDuration = Math.max(blinkDuration, 100);
                if ((currentTime - transitionStartTime) % blinkDuration < blinkDuration / 2) {
                    SaxionApp.setFill(java.awt.Color.BLACK);
                    SaxionApp.drawRectangle(0, 0, 1000, 1000);
                }

                if ((currentTime - transitionStartTime) >= blinkDuration * (blinkCount + 1)) {
                    blinkCount++;
                }

                if (blinkCount >= 6) {
                    blinkCount = 0;
                    transitionStartTime = currentTime;
                    transitionStep = 3;
                }
                break;

            case 3:
                gameStarted = true;
                isIntroScene = false;
                transitioningToNextScene = false;
                Player naruto = characterManager.getNaruto();
                naruto.x = 1050;
                naruto.y = 290;
                naruto.direction = "down";
                characterManager.changeScene("multiverse");
                gameMap = new tile.Map("/object/map_output_new.txt");
                break;
        }
    }


    private void updateOverworld() {
        updateCamera(gameMap);
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
        SaxionApp.drawImage(battleMapImage, 0, 0, 1000, 1000);

        combatSystem.drawHealthBars();
        combatSystem.drawBattleField();

        // Display action menu when it's the player's turn
        if (combatSystem.isPlayerTurn()) {
            combatSystem.displayActionMenu();
        }

        // Check if the battle is over
        if (combatSystem.isBattleOver()) {
            endBattle();
        }
    }



//    private void playBackgroundMusic() {
//        String[] songs = {
//                "src/res/audio/first_map_audio_1.wav",
//                "src/res/audio/first_map_audio_2.wav",
//                "src/res/audio/first_map_audio_3.wav"
//        };
//
//        if (!AudioHelper.isPlaying() || !AudioHelper.isSongInArray(AudioHelper.getFilename(), songs)) {
//            int randomIndex = SaxionApp.getRandomValueBetween(0, 3);
//            String selectedSong = songs[randomIndex];
//            AudioHelper.newSong(selectedSong, false);
//        }
//    }

    private void playBackgroundMusic() {
        String[] introSongs = {
                "src/res/audio/main_menu.wav"
        };

        String[] overworldSongs = {
                "src/res/audio/first_map_audio_1.wav",
                "src/res/audio/first_map_audio_2.wav",
                "src/res/audio/first_map_audio_3.wav"
        };

        String[] battleSongs = {
//                "src/res/audio/battle_music_1.wav",
                "src/res/audio/first_map_audio_1.wav",
                "src/res/audio/first_map_audio_2.wav",
                "src/res/audio/first_map_audio_3.wav"
        };

        String[] selectedSongs;

        if (isIntroScene) {
            selectedSongs = introSongs;
        } else if (inBattle) {
            selectedSongs = battleSongs;
        } else {
            selectedSongs = overworldSongs;
        }

        if (!AudioHelper.isPlaying() || !AudioHelper.isSongInArray(currentSong, selectedSongs)) {
            int randomIndex = SaxionApp.getRandomValueBetween(0, selectedSongs.length);
            currentSong = selectedSongs[randomIndex];
            AudioHelper.newSong(currentSong, false);
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

private void updateCamera(tile.Map currentMap) {
    int screenWidth = 1000;
    int screenHeight = 1000;
    int tileSize = tile.Map.TILE_SIZE;

    cameraX = characterManager.getNaruto().getX() - screenWidth / 2;
    cameraY = characterManager.getNaruto().getY() - screenHeight / 2;

    int maxCameraX = currentMap.getWidth() * tileSize - screenWidth;
    int maxCameraY = currentMap.getHeight() * tileSize - screenHeight;

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
                // Handle 'A' key for normal attack
                if (keyCode == KeyboardEvent.VK_A) {
                    combatSystem.handlePlayerAction(null, keyboardEvent); // Trigger normal attack
                }

                // Handle 'E' key for special attack
                if (keyCode == KeyboardEvent.VK_E) {
                    combatSystem.handlePlayerAction(null, keyboardEvent); // Trigger special attack
                }

                // Handle 'Q' key for ultimate attack
                if (keyCode == KeyboardEvent.VK_Q) {
                    combatSystem.handlePlayerAction(null, keyboardEvent); // Trigger ultimate attack
                }

                // Handle player switching
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
    public void mouseEvent(nl.saxion.app.interaction.MouseEvent mouseEvent) {
        if (inBattle && mouseEvent.isMouseDown() && mouseEvent.isLeftMouseButton()) {
            combatSystem.handlePlayerAction(mouseEvent, null); // Trigger normal attack
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
