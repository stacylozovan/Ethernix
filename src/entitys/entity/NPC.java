package entity;

import nl.saxion.app.SaxionApp;
import java.awt.Color;

public class NPC extends Entity {
    public String name;
    public boolean isVisible;
    public String[] dialogue;
    public String movementPattern;

    public int currentDialogueIndex = 0;
    private boolean awaitingKeyPress = false;

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

    public void interact(boolean isKeyPressed) {
        if (dialogue != null && currentDialogueIndex < dialogue.length) {
            int boxWidth = 600;
            int boxHeight = 150;
            int boxX = 100;
            int boxY = 600;
            int textPadding = 20;

            SaxionApp.setFill(Color.WHITE);
            SaxionApp.drawRectangle(boxX, boxY, boxWidth, boxHeight);

            SaxionApp.setBorderColor(Color.BLACK);
            SaxionApp.drawRectangle(boxX, boxY, boxWidth, boxHeight);

            SaxionApp.setFill(Color.BLACK); // Black text
            SaxionApp.drawText(name + ":", boxX + textPadding, boxY + textPadding + 20, 20); // Name with smaller font
            SaxionApp.drawText(dialogue[currentDialogueIndex], boxX + textPadding, boxY + textPadding + 60, 24); // Dialogue text

            if (isKeyPressed) {
                currentDialogueIndex++;
            }
        } else {
            currentDialogueIndex = 0;
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
