import entity.AudioHelper;
import entity.CharacterManager;
import entity.DialogueLoader;
import entity.NPC;
import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main implements GameLoop {
    private tile.Map gameMap;
    private CharacterManager characterManager;
    private boolean[] keys = new boolean[256];
    private MainMenu mainMenu = new MainMenu();
    private boolean inMenu = true;
    private boolean gameStarted = false;
    private AudioHelper audioHelper;
    private int frameCount = 0;

    private int cameraX = 0;
    private int cameraY = 0;

    // NPC-related variables
    private List<NPC> npcs;
    private boolean interactingWithNPC = false;
    private NPC currentInteractingNPC;

    public static void main(String[] args) {
        SaxionApp.startGameLoop(new Main(), 1000, 1000, 40);
    }

    @Override
    public void init() {
        characterManager = new CharacterManager();

        gameMap = new tile.Map();

        // Initialize NPCs
        initNPCs();
    }

    private void initNPCs() {
        npcs = new ArrayList<>();
        Map<String, String[]> dialogues = DialogueLoader.loadDialogues(new nl.saxion.app.CsvReader("src/res/npcs/npc_dialogues.csv"));

        // Example NPCs
        NPC npc1 = new NPC("villager", 300, 300, dialogues.get("villager"), "down", "static");
        NPC npc2 = new NPC("merchant", 500, 500, dialogues.get("merchant"), "down", "static");

        npcs.add(npc1);
        npcs.add(npc2);
    }

    @Override
    public void loop() {
        frameCount++; // Increment frame counter

        SaxionApp.clear();

        if (mainMenu.isInSettings()) {
            SaxionApp.drawText("Settings", 150,150,50); // if I click the settings button, this will be changed into a settings method later
        } else if (inMenu) {
            mainMenu.drawMainMenu();
        } else if (gameStarted) {

            String[] songs = {
                    "src/res/audio/first_map_audio_1.wav",
                    "src/res/audio/first_map_audio_2.wav",
                    "src/res/audio/first_map_audio_3.wav"
            }; // 3 songs randomized



            if (!AudioHelper.isPlaying() || !AudioHelper.isSongInArray(AudioHelper.getFilename(), songs)) {
                // Select a random song from the list and play it
                int randomIndex = SaxionApp.getRandomValueBetween(0, 3);
                String selectedSong = songs[randomIndex];
                AudioHelper.newSong(selectedSong, false); // Play the selected song
            }

            updateCamera();

            gameMap.draw(cameraX, cameraY);

            characterManager.update(keys);
            int playerScreenX = characterManager.getPlayer().getX() - cameraX;
            int playerScreenY = characterManager.getPlayer().getY() - cameraY;
            characterManager.draw(playerScreenX, playerScreenY, cameraX, cameraY);

            // Draw and handle NPC interactions
            handleNPCInteractions(playerScreenX, playerScreenY, frameCount); // Pass frameCount to handle animation
        }
    }

    private void handleNPCInteractions(int playerScreenX, int playerScreenY, int frameCount) {
        if (interactingWithNPC && currentInteractingNPC != null) {
            // Display interaction text
            currentInteractingNPC.interact();

            // End dialog on spacebar press
            if (keys[' ']) {
                interactingWithNPC = false;
                currentInteractingNPC = null;
            }
        } else {
            // Check for nearby NPCs
            for (NPC npc : npcs) {
                if (npc.isVisible) {
                    // Draw NPC
                    int npcScreenX = npc.x - cameraX;
                    int npcScreenY = npc.y - cameraY;
                    npc.draw(npcScreenX, npcScreenY, frameCount); // Pass frameCount to NPC

                    // Check for interaction
                    if (npc.isPlayerNear(characterManager.getPlayer().getX(), characterManager.getPlayer().getY()) && keys['E']) {
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

        cameraX = characterManager.getPlayer().getX() - screenWidth / 2;
        cameraY = characterManager.getPlayer().getY() - screenHeight / 2;

        int maxCameraX = gameMap.getWidth() * tileSize - screenWidth;
        int maxCameraY = gameMap.getHeight() * tileSize - screenHeight;

        cameraX = Math.max(0, Math.min(cameraX, maxCameraX));
        cameraY = Math.max(0, Math.min(cameraY, maxCameraY));
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
            if (mainMenu.mouseEvent(mouseEvent)) {
                inMenu = false;
                gameStarted = true;
            } else if (mainMenu.isInSettings()) {
                inMenu = false; // Show settings screen
            }

            else if (mainMenu.isInSettings()) {
                // Show settings screen
                inMenu = false;
            }
        }
    }
}
