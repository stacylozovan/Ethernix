package entity;

import nl.saxion.app.SaxionApp;

public class Madara extends Entity {
    private final String[] downImages = new String[9];
    private final String[] upImages = new String[9];
    private final String[] leftImages = new String[9];
    private final String[] rightImages = new String[9];

    private double exactX, exactY;

    public Madara() {
        setDefaultValues();
        getMadaraImages();
    }

    public void setDefaultValues() {
        x = 1180;
        y = 300;
        exactX = x;
        exactY = y;
        speed = 3;
        direction = "down";
        health = 400;
    }

    private String getImagePath(String direction, int frame) {
        return String.format("src/res/madara/%s/%s%d.png", direction, direction, frame);
    }

    public void getMadaraImages() {
        for (int i = 0; i <= 8; i++) {
            downImages[i] = getImagePath("down", i);
            upImages[i] = getImagePath("up", i);
            leftImages[i] = getImagePath("left", i);
            rightImages[i] = getImagePath("right", i);
        }
    }

    public void draw(int screenX, int screenY) {
        String image = "";

        switch (direction) {
            case "up" -> image = upImages[spriteNum - 1];
            case "down" -> image = downImages[spriteNum - 1];
            case "left" -> image = leftImages[spriteNum - 1];
            case "right" -> image = rightImages[spriteNum - 1];
            default -> image = downImages[0];
        }

        SaxionApp.drawImage(image, screenX, screenY, 66, 66);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    public int getHealth() {
        return health;
    }

    // New method to set position for teleportation
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.exactX = x;
        this.exactY = y;
    }
}
