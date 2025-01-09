package entity;

import nl.saxion.app.SaxionApp;

public class NPC extends Entity {
    public String name;
    public boolean isVisible;
    public String[] dialogue;
    public String movementPattern;

    private int currentDialogueIndex = 0;
    private int animationFrame = 0; // Track the current animation frame

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

    // Display dialogue when interacting with NPC
    public void interact() {
        if (dialogue != null && currentDialogueIndex < dialogue.length) {
            SaxionApp.drawText(name + ": " + dialogue[currentDialogueIndex], 50, 50, 20);
        }
    }

    // Check if there's more dialogue
    public boolean hasNextDialogue() {
        return dialogue != null && currentDialogueIndex < dialogue.length - 1;
    }

    // Move to the next dialogue
    public void nextDialogue() {
        if (hasNextDialogue()) {
            currentDialogueIndex++;
        }
    }

    // Check if player is near the NPC
    public boolean isPlayerNear(int playerX, int playerY) {
        return Math.abs(playerX - x) < 50 && Math.abs(playerY - y) < 50;
    }

    // Draw the NPC with animations
    public void draw(int screenX, int screenY, int frameCount) {
        // Update animation frame every 10 frames
        if (frameCount % 10 == 0) {
            animationFrame = (animationFrame + 1) % 3; // Loop through 3 animation frames
        }

        // Build the path to the current frame's image
        String imagePath = String.format("src/res/npcs/%s/%s_%d.png", name, name, animationFrame + 1);

        // Draw the NPC image at the specified screen position
        SaxionApp.drawImage(imagePath, screenX, screenY, this.width, this.height);
    }
}
