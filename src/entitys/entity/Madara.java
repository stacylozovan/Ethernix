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
        down16 = "src/res/madara/madara_fight_15.png";

        right1 = "src/res/madara/madara_fight_1.png";
        left1 = "src/res/madara/madara_fight_2.png";
    }

    public void update(boolean[] keys, int mapWidth, int mapHeight, int tileSize) {
        boolean keyPressed = false;

        if (keys[KeyboardEvent.VK_UP] || keys[KeyboardEvent.VK_W]) {
            direction = "up";
            y = Math.max(0, y - speed);
            keyPressed = true;
        }
        if (keys[KeyboardEvent.VK_DOWN] || keys[KeyboardEvent.VK_S]) {
            direction = "down";
            y = Math.min((mapHeight * tileSize) - 64, y + speed);
            keyPressed = true;
        }
        if (keys[KeyboardEvent.VK_LEFT] || keys[KeyboardEvent.VK_A]) {
            direction = "left";
            x = Math.max(0, x - speed);
            keyPressed = true;
        }
        if (keys[KeyboardEvent.VK_RIGHT] || keys[KeyboardEvent.VK_D]) {
            direction = "right";
            x = Math.min((mapWidth * tileSize) - 64, x + speed);
            keyPressed = true;
        }

        if (keyPressed) {
            spriteCounter++;
            if (spriteCounter < 16) {
                spriteNum++;
            } else {
                spriteCounter = 0;
                spriteNum = 1;
            }
        }
    }

    public void draw(int screenX, int screenY) {
        String image = "";
        switch (direction) {
            case "up":
                image = down1;
                break;
            case "down":
                image = setImageDown(spriteNum);
                break;
            case "left":
                image = left1;
                break;
            case "right":
                image = right1;
                break;
        }
        SaxionApp.drawImage(image, screenX, screenY, 64, 64);
    }
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    public int getHealth() {
        return health;
    }

    private String setImageDown(int spriteNum) {
        switch (spriteNum) {
            case 1:
                return down1;
            case 2:
                return down2;
            case 3:
                return down3;
            case 4:
                return down4;
            case 5:
                return down5;
            case 6:
                return down6;
            case 7:
                return down7;
            case 8:
                return down8;
            case 9:
                return down9;
            case 10:
                return down10;
            case 11:
                return down11;
            case 12:
                return down12;
            case 13:
                return down13;
            case 14:
                return down14;
            case 15:
                return down15;
            case 16:
                return down16;
            default:
                return "Invalid sprite number";
        }
    }
}
