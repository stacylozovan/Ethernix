package entity;

import nl.saxion.app.SaxionApp;

public class NPC extends Entity {
    public String name;
    public boolean isVisible;
    public String[] dialogue;
    public String movementPattern;

    private int currentDialogueIndex = 0;

    public NPC(String name, int x, int y, String[] dialogue, String direction, String movementPattern) {
        this.name = name;
        this.dialogue = dialogue;
        this.movementPattern = movementPattern;
        this.isVisible = true;
        this.x = x;
        this.y = y;
        this.width = 64;
        this.height = 64;

        if (movementPattern.equals("static")) {
            this.direction = "down";
        } else {
            this.direction = direction;
        }
    }

    public void interact() {
        if (dialogue != null && currentDialogueIndex < dialogue.length) {
            SaxionApp.drawText(name + ": " + dialogue[currentDialogueIndex], 50, 50, 20);
            currentDialogueIndex++;
        } else {
            currentDialogueIndex = 0; // Reset dialogue
        }
    }

    public boolean isPlayerNear(int playerX, int playerY) {
        return Math.abs(playerX - x) < 50 && Math.abs(playerY - y) < 50;
    }

    public void draw(int screenX, int screenY) {
        String imagePath = String.format("src/res/npcs/%s/%s_1.png", this.name, this.name);
        SaxionApp.drawImage(imagePath, screenX, screenY, this.width, this.height);
    }
}
