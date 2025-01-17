package entity;

import nl.saxion.app.SaxionApp;
import tile.Map;

import java.awt.*;

public class NPC extends Entity {
    public String name;
    public boolean isVisible;
    public String[] dialogue;
    public String movementPattern;

    public int currentDialogueIndex = 0;
    private boolean isKeyHeld = false;

    public NPC(String name, int x, int y, String[] dialogue, String direction, String movementPattern) {
        this.name = name;
        this.dialogue = dialogue;
        this.movementPattern = movementPattern;
        this.isVisible = true;
        this.x = x;
        this.y = y;
        this.width = 64;
        this.height = 64;
        solidArea = new Rectangle();
        solidArea.x = 20;
        solidArea.y = 10;
        solidArea.width = 24;
        solidArea.height = 54;

        if (movementPattern.equals("static")) {
            this.direction = "down";
        } else {
            this.direction = direction;
        }
    }

    public void interact(boolean isKeyPressed) {
        if (dialogue != null && currentDialogueIndex < dialogue.length) {
            int boxWidth = 900;
            int boxHeight = 150;
            int boxX = 50;
            int boxY = 600;
            int textPadding = 20;

            SaxionApp.setFill(Color.WHITE);
            SaxionApp.drawRectangle(boxX, boxY, boxWidth, boxHeight);

            SaxionApp.setBorderColor(Color.BLACK);
            SaxionApp.drawRectangle(boxX, boxY, boxWidth, boxHeight);

            SaxionApp.setFill(Color.BLACK);
            SaxionApp.drawText(name + ":", boxX + textPadding, boxY + textPadding + 20, 20);
            SaxionApp.drawText(dialogue[currentDialogueIndex], boxX + textPadding, boxY + textPadding + 60, 24);

            if (isKeyPressed && !isKeyHeld) {
                currentDialogueIndex++;
                isKeyHeld = true;
            }
        } else {
            currentDialogueIndex = 0;
        }
    }

    public void releaseKey() {
        isKeyHeld = false;
    }

    public boolean isPlayerNear(int playerX, int playerY) {
        return Math.abs(playerX - x) < 50 && Math.abs(playerY - y) < 50;
    }

    public void draw(int screenX, int screenY) {
        String imagePath = String.format("src/res/npcs/%s/%s.png", this.name, this.name);
        SaxionApp.drawImage(imagePath, screenX, screenY, this.width, this.height);
    }

    public String getName() {
        return name;
    }

    public void moveRight(int tiles) {
        this.x += tiles * Map.TILE_SIZE;
    }
}
