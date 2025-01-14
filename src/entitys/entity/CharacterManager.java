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
//        if (scene.equals("intro_scene")){
//            npcs.add( new NPC("kakashi", 500, 1300, npcDialogues.get("kakashi"), "down", "dynamic"));
//        } else {
//            npcs.add(new NPC("mark", 850, 1100, npcDialogues.get("mark"), "down", "static"));
//            npcs.add(new NPC("lucy", 1250, 950, npcDialogues.get("lucy"), "up", "static"));
//        }
//        npcs.add(new NPC("villager", 300, 300, npcDialogues.get("villager"), "down", "static"));
//        npcs.add(new NPC("merchant", 500, 500, npcDialogues.get("merchant"), "down", "static"));
    }

    public void update(boolean[] keys,tile.Map gamemap) {
        activePlayer.update(keys,gamemap);
        madara.setPosition(madara.getX(), madara.getY());
    }

    private void loadNPCs(String scene, Map<String, String[]> npcDialogues) {
        npcs.clear();

        if (scene.equals("intro_scene")) {
            npcs.add(new NPC("kakashi", 500, 1300, npcDialogues.get("kakashi"), "down", "dynamic"));
        } else if (scene.equals("multiverse")) {
            npcs.add(new NPC("mark", 850, 1100, npcDialogues.get("mark"), "down", "static"));
            npcs.add(new NPC("lucy", 1250, 950, npcDialogues.get("lucy"), "up", "static"));
        }
    }

    public void changeScene(String newScene) {
        CsvReader csvReader = new CsvReader("src/res/npcs/npc_dialogues.csv");
        Map<String, String[]> npcDialogues = DialogueLoader.loadDialogues(csvReader);

        loadNPCs(newScene, npcDialogues);
        System.out.println("Scene changed to: " + newScene);
    }

    public void handleCharacterInteractions() {
        if (isNear(activePlayer)) {
            activePlayer.takeDamage(0);
        }

        if (naruto.getHealth() <= 0 && (gojo == null || gojo.getHealth() <= 0)) {
            System.out.println("Both Naruto and Gojo are dead!");
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

