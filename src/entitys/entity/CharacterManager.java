package entity;

import nl.saxion.app.CsvReader;
import java.util.ArrayList;
import nl.saxion.app.SaxionApp;
import java.util.List;
import java.util.Map;

public class CharacterManager {
    private final Player naruto;
    private final Player gojo;
    private Player activePlayer;
    private final Madara madara;
    public static List<NPC> npcs;
    private final main.CollisionChecker cChecker;
    private long gameOverStartTime = 0;
    private static final long GAME_OVER_DISPLAY_DURATION = 6000;
    private String gameOverImagePath = "src/res/scene/gameover.png";


    public CharacterManager(main.CollisionChecker cChecker, String scene) {
        CsvReader csvReader = new CsvReader("src/res/npcs/npc_dialogues.csv");
        Map<String, String[]> npcDialogues = DialogueLoader.loadDialogues(csvReader);

        this.cChecker = cChecker;
        this.naruto = new Player("naruto", cChecker);
        this.gojo = new Player("gojo", cChecker);

        this.madara = new Madara();

        this.naruto.setDefaultValues();
        this.gojo.setDefaultValues();
        this.madara.setDefaultValues();

        this.activePlayer = naruto;

        this.npcs = new ArrayList<>();
        loadNPCs(scene, npcDialogues);
    }

    public void update(boolean[] keys,tile.Map gamemap) {
        activePlayer.update(keys,gamemap);
        madara.setPosition(madara.getX(), madara.getY());
    }

    private void loadNPCs(String scene, Map<String, String[]> npcDialogues) {
        npcs.clear();

        if (scene.equals("intro_scene")) {
            npcs.add(new NPC("kakashi", (18 * tile.Map.TILE_SIZE), (18 * tile.Map.TILE_SIZE), npcDialogues.get("kakashi"), "down", "dynamic"));
        } else if (scene.equals("multiverse")) {
            npcs.add(new NPC("eren", (60 * tile.Map.TILE_SIZE), (35 * tile.Map.TILE_SIZE), npcDialogues.get("eren"), "down", "static"));
            npcs.add(new NPC("rengoku", (66 * tile.Map.TILE_SIZE), (44 * tile.Map.TILE_SIZE), npcDialogues.get("rengoku"), "down", "static"));
            npcs.add(new NPC("guts", (77 * tile.Map.TILE_SIZE), (41 * tile.Map.TILE_SIZE), npcDialogues.get("guts"), "down", "static"));
            npcs.add(new NPC("robin", (41 * tile.Map.TILE_SIZE), (34 * tile.Map.TILE_SIZE), npcDialogues.get("robin"), "down", "static"));
            npcs.add(new NPC("roshi", (38 * tile.Map.TILE_SIZE), (25 * tile.Map.TILE_SIZE), npcDialogues.get("roshi"), "down", "static"));
            npcs.add(new NPC("luffy", (42 * tile.Map.TILE_SIZE), (20 * tile.Map.TILE_SIZE), npcDialogues.get("luffy"), "down", "static"));
            npcs.add(new NPC("patrick", (26 * tile.Map.TILE_SIZE), (26 * tile.Map.TILE_SIZE), npcDialogues.get("patrick"), "down", "static"));
            npcs.add(new NPC("gojo", (33 * tile.Map.TILE_SIZE), (44* tile.Map.TILE_SIZE), npcDialogues.get("gojo"), "down", "static"));
        }
    }

    public void changeScene(String newScene) {
        CsvReader csvReader = new CsvReader("src/res/npcs/npc_dialogues.csv");
        Map<String, String[]> npcDialogues = DialogueLoader.loadDialogues(csvReader);

        loadNPCs(newScene, npcDialogues);
        System.out.println("Scene changed to: " + newScene);
    }

    public void resetGame() {
        naruto.setDefaultValues();
        naruto.setPosition(1500, 500);
        if (gojo != null) {
            gojo.setDefaultValues();
        }

        madara.setDefaultValues();

        activePlayer = naruto;

        System.out.println("Game has been reset!");
    }

    private void showGameOverScene() {
        SaxionApp.clear();

        SaxionApp.drawImage(gameOverImagePath, 0, 0, 1000, 1000);

        if (gameOverStartTime == 0) {
            gameOverStartTime = System.currentTimeMillis();
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - gameOverStartTime >= GAME_OVER_DISPLAY_DURATION) {
            resetGame();
            gameOverStartTime = 0;
        }
    }


    public void handleCharacterInteractions() {
        if (isNear(activePlayer)) {
            activePlayer.takeDamage(0);
        }

        if (naruto.getHealth() <= 0 && (gojo == null || gojo.getHealth() <= 0)) {
            System.out.println("Both Naruto and Gojo are dead!");
            showGameOverScene();
        }

        if (madara.getHealth() <= 0) {
            System.out.println("Madara is defeated!");
        }
    }


    public void draw(int cameraX, int cameraY) {
        for (NPC npc : npcs) {
            if (npc.isVisible){
                int npcScreenX = npc.getX() - cameraX;
                int npcScreenY = npc.getY() - cameraY;
                npc.draw(npcScreenX, npcScreenY);
            }
        }

        if (activePlayer == naruto) {
            drawCharacter(naruto, cameraX, cameraY);
        } else if (activePlayer == gojo) {
            drawCharacter(gojo, cameraX, cameraY);
        }
        drawCharacter(madara, cameraX, cameraY);
    }

    public void displayHealthStatus() {
//        System.out.println("Naruto Health: " + naruto.getHealth());
        if (gojo != null) {
//            System.out.println("Gojo Health: " + gojo.getHealth());
        }
//        System.out.println("Madara Health: " + madara.getHealth());
    }

    public Player getNaruto() {
        return naruto;
    }

    public Player getGojo() {
        return gojo;
    }

    public Madara getMadara() {
        return madara;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void switchActivePlayer(int playerNumber) {
        if (playerNumber == 1 && activePlayer != naruto) {
            naruto.setPosition(activePlayer.getX(), activePlayer.getY());
            activePlayer = naruto;
//            System.out.println("Switched to Naruto. Position: " + naruto.getX() + ", " + naruto.getY());
        } else if (playerNumber == 2 && (gojo == null || activePlayer != gojo)) {
//            if (gojo == null) {
//                gojo = new Player("gojo");
//                gojo.setDefaultValues();
//            }
            gojo.setPosition(activePlayer.getX(), activePlayer.getY());
            activePlayer = gojo;
//            System.out.println("Switched to Gojo. Position: " + gojo.getX() + ", " + gojo.getY());
        }
    }

    public boolean isPlayerNearMadara() {
        return isNear(activePlayer);
    }

    private boolean isNear(Player player) {
        return Math.abs(player.getX() - madara.getX()) < 200 &&
                Math.abs(player.getY() - madara.getY()) < 200;
    }

    private void drawCharacter(Entity character, int cameraX, int cameraY) {
        int screenX = character.getX() - cameraX;
        int screenY = character.getY() - cameraY;
        character.draw(screenX, screenY);
    }

    public void printNPCDialogues() {
        for (NPC npc : npcs) {
            System.out.println("NPC: " + npc.name);
            if (npc.dialogue != null) {
                for (int i = 0; i < npc.dialogue.length; i++) {
                    System.out.println("  Dialogue " + (i + 1) + ": " + npc.dialogue[i]);
                }
            } else {
                System.out.println("  No dialogues loaded!");
            }
        }
    }

    public NPC getNPCByName(String name) {
        for (NPC npc : npcs) {
            if (npc.getName().equals(name)) {
                return npc;
            }
        }
        return null;
    }
}

