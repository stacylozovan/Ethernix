package entity;

import nl.saxion.app.CsvReader;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class CharacterManager {

    private final Player player;
    private final Madara madara;
    private final List<NPC> npcs;


    public CharacterManager() {
        CsvReader csvReader = new CsvReader("src/res/npcs/npc_dialogues.csv");
        Map<String, String[]> npcDialogues = DialogueLoader.loadDialogues(csvReader);

        this.player = new Player("naruto");
        this.madara = new Madara();
        this.player.setDefaultValues();
        this.madara.setDefaultValues();

        this.npcs = new ArrayList<>();
        npcs.add(new NPC("mark", 700, 700, npcDialogues.get("mark"), "down", "static"));
        npcs.add(new NPC("lucy", 1150, 600, npcDialogues.get("lucy"), "up", "static"));
    }

    public void update(boolean[] keys) {
        player.update(keys);
        //madara.update(player.getX(), player.getY());
    }

    public void handleCharacterInteractions() {
        if (Math.abs(player.getX() - madara.getX()) < 50 && Math.abs(player.getY() - madara.getY()) < 50) {
            player.takeDamage(10);
        }

        if (player.getHealth() <= 0) {
            System.out.println("Player is dead!");
        }

        if (madara.getHealth() <= 0) {
            System.out.println("Madara is defeated!");
        }
    }

    public void draw(int playerScreenX, int playerScreenY, int cameraX, int cameraY) {
        player.draw(playerScreenX, playerScreenY);
        int madaraScreenX = madara.getX() - cameraX;
        int madaraScreenY = madara.getY() - cameraY;
        madara.draw(madaraScreenX, madaraScreenY);
        
        for (NPC npc : npcs) {
          int npcScreenX = npc.getX() - cameraX;
          int npcScreenY = npc.getY() - cameraY;
          npc.draw(npcScreenX, npcScreenY);
        }
    }

    public void displayHealthStatus() {
        System.out.println("Player Health: " + player.getHealth());
        System.out.println("Madara Health: " + madara.getHealth());
    }

    public Player getPlayer() {
        return player;
    }


    public Madara getMadara() {
        return madara;
    }

//     this method exist just to test the dialogues imports
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

