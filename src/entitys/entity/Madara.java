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
        x = 500;
        y = 300;
        exactX = x;
        exactY = y;
        speed = 3;
        direction = "down";
        health = 100;
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

//    public void update(int playerX, int playerY) {
//        double deltaX = playerX - exactX;
//        double deltaY = playerY - exactY;
//
//
//        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
//        if (distance > speed) {
//            exactX += (deltaX / distance) * speed;
//            exactY += (deltaY / distance) * speed;
//        }
//
//
//        x = (int) exactX;
//        y = (int) exactY;
//
//
//        if (Math.abs(deltaX) > Math.abs(deltaY)) {
//            direction = (deltaX > 0) ? "right" : "left";
//        } else {
//            direction = (deltaY > 0) ? "down" : "up";
//        }
//
//        // Handle sprite animation
//        spriteCounter++;
//        if (spriteCounter >= 5) { // Change frame every 5 updates for smoother animation
//            spriteCounter = 0;
//            spriteNum = (spriteNum % 9) + 1; // Cycle through frames 1 to 9
//        }
//    }

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
}
