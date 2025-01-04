package entity;

import nl.saxion.app.CsvReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CharacterManager {
    private final Player naruto;
    private Player gojo;
    private Player activePlayer;
    private final Madara madara;
    private final List<NPC> npcs;

    public CharacterManager() {
        CsvReader csvReader = new CsvReader("src/res/npcs/npc_dialogues.csv");
        Map<String, String[]> npcDialogues = DialogueLoader.loadDialogues(csvReader);

        this.naruto = new Player("naruto");
        this.madara = new Madara();

        this.naruto.setDefaultValues();
        this.madara.setDefaultValues();

        this.activePlayer = naruto;

        this.npcs = new ArrayList<>();
        npcs.add(new NPC("mark", 700, 700, npcDialogues.get("mark"), "down", "static"));
        npcs.add(new NPC("lucy", 1150, 600, npcDialogues.get("lucy"), "up", "static"));
    }

    public void update(boolean[] keys) {
        activePlayer.update(keys);
        madara.setPosition(madara.getX(), madara.getY());
    }

    public void handleCharacterInteractions() {
        if (isNear(activePlayer)) {
            activePlayer.takeDamage(10);
        }

        if (naruto.getHealth() <= 0) {
            System.out.println("Naruto is dead!");
        }

        if (madara.getHealth() <= 0) {
            System.out.println("Madara is defeated!");
        }
    }

    public void draw(int cameraX, int cameraY) {
        drawCharacter(naruto, cameraX, cameraY);
        drawCharacter(madara, cameraX, cameraY);

        for (NPC npc : npcs) {
            drawCharacter(npc, cameraX, cameraY);
        }
    }

    public void displayHealthStatus() {
        System.out.println("Naruto Health: " + naruto.getHealth());
        if (gojo != null) {
            System.out.println("Gojo Health: " + gojo.getHealth());
        }
        System.out.println("Madara Health: " + madara.getHealth());
    }

    public Player getNaruto() {
        return naruto;
    }

    public Player getGojo() {
        if (gojo == null) {
            gojo = new Player("gojo");
            gojo.setDefaultValues();
        }
        return gojo;
    }

    public Madara getMadara() {
        return madara;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void switchActivePlayer(int playerNumber) {
        if (playerNumber == 1) {
            activePlayer = naruto;
            System.out.println("Switched to Naruto.");
        } else if (playerNumber == 2 && gojo != null) {
            activePlayer = gojo;
            System.out.println("Switched to Gojo.");
        }
    }

    public boolean isPlayerNearMadara() {
        return isNear(activePlayer);
    }

    private boolean isNear(Player player) {
        return Math.abs(player.getX() - madara.getX()) < 50 &&
                Math.abs(player.getY() - madara.getY()) < 50;
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
}
