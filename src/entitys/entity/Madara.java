package entity;

import nl.saxion.app.SaxionApp;

public class Madara extends Entity {
    public Madara() {
        setDefaultValues();
        getMadaraImage();
    }

    public void setDefaultValues() {
        x = 500;
        y = 500;
        speed = 8;
        direction = "down";
        health = 100; // Default health value
    }

    public void getMadaraImage() {
        down1 = "src/res/madara/madara_fight_1.png";
        down2 = "src/res/madara/madara_fight_2.png";
        down3 = "src/res/madara/madara_fight_3.png";
        down4 = "src/res/madara/madara_fight_4.png";
        down5 = "src/res/madara/madara_fight_5.png";
        down6 = "src/res/madara/madara_fight_6.png";
        down7 = "src/res/madara/madara_fight_7.png";
        down8 = "src/res/madara/madara_fight_8.png";
        down9 = "src/res/madara/madara_fight_9.png";
        down10 = "src/res/madara/madara_fight_10.png";
        down11 = "src/res/madara/madara_fight_11.png";
        down12 = "src/res/madara/madara_fight_12.png";
        down13 = "src/res/madara/madara_fight_13.png";
        down14 = "src/res/madara/madara_fight_14.png";
        down15 = "src/res/madara/madara_fight_15.png";
        down16 = "src/res/madara/madara_fight_16.png";
        right1 = "src/res/madara/madara_fight_1.png";
        left1 = "src/res/madara/madara_fight_2.png";
    }

    public void update(int playerX, int playerY) {
        // Logic to move towards the player's position
        if (playerX < x) {
            x = Math.max(0, x - speed);
            direction = "left";
        } else if (playerX > x) {
            x = Math.min(x + speed, 1000); // Assuming screen width is 1000
            direction = "right";
        }

        if (playerY < y) {
            y = Math.max(0, y - speed);
            direction = "up";
        } else if (playerY > y) {
            y = Math.min(y + speed, 1000); // Assuming screen height is 1000
            direction = "down";
        }

        spriteCounter++;
        if (spriteCounter > 15) {
            spriteNum = (spriteNum % 16) + 1;
            spriteCounter = 0;
        }
    }

    public void draw(int screenX, int screenY) {
        String image = switch (direction) {
            case "up" -> down1;
            case "down" -> setImageDown(spriteNum);
            case "left" -> left1;
            case "right" -> right1;
            default -> "";
        };
        SaxionApp.drawImage(image, screenX, screenY, 64, 64);
    }

    private String setImageDown(int spriteNum) {
        return switch (spriteNum) {
            case 1 -> down1;
            case 2 -> down2;
            case 3 -> down3;
            case 4 -> down4;
            case 5 -> down5;
            case 6 -> down6;
            case 7 -> down7;
            case 8 -> down8;
            case 9 -> down9;
            case 10 -> down10;
            case 11 -> down11;
            case 12 -> down12;
            case 13 -> down13;
            case 14 -> down14;
            case 15 -> down15;
            case 16 -> down16;
            default -> down1; // Default image fallback
        };
    }
}