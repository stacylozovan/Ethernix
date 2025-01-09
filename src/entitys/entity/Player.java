package entity;

import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.SaxionApp;
import tile.Map;

import java.awt.*;

public class Player extends Entity {
    private final String[] downImages = new String[9];
    private final String[] upImages = new String[9];
    private final String[] leftImages = new String[9];
    private final String[] rightImages = new String[9];
    public static final int PLAYER_SIZE = 64;
    main.CollisionChecker cChecker;

    public Player(main.CollisionChecker cChecker){
        setDefaultValues();
        getPlayerImage();
        this.cChecker = cChecker;
    }

    public void setDefaultValues() {
        x = 500;
        y = 500;
        speed = 6;
        direction = "down";
        width = PLAYER_SIZE;
        height = PLAYER_SIZE;
        solidArea = new Rectangle();
        solidArea.x = 16;
        solidArea.y = 30;
        solidArea.width = 32;
        solidArea.height = 34;
    }

    private String getImagePath(String direction, int frame) {
        return String.format("src/res/player/naruto/%s/naruto_%s%d.png",direction, direction, frame);
    }

    public void getPlayerImage() {
        for (int i = 0; i <= 8; i++) {
            downImages[i] = getImagePath("down", i);
            upImages[i] = getImagePath("up", i);
            leftImages[i] = getImagePath("left", i);
            rightImages[i] = getImagePath("right", i);
        }
    }

    public void update(boolean[] keys, tile.Map gameMap) {
        boolean keyPressed = false;

        if (keys[KeyboardEvent.VK_UP] || keys[KeyboardEvent.VK_W]) {
            direction = "up";
            keyPressed = true;
        }
        if (keys[KeyboardEvent.VK_DOWN] || keys[KeyboardEvent.VK_S]) {
            direction = "down";
            keyPressed = true;
        }
        if (keys[KeyboardEvent.VK_LEFT] || keys[KeyboardEvent.VK_A]) {
            direction = "left";
            keyPressed = true;
        }
        if (keys[KeyboardEvent.VK_RIGHT] || keys[KeyboardEvent.VK_D]) {
            direction = "right";
            keyPressed = true;
        }

//        CHECK TILE COLLISION
        collisionOn = this.cChecker.checkTile(this);
//        IF COLLISION IS FALSE PLAYER CAN MOVE
        if(keyPressed && !collisionOn){
            switch(direction){
                case "up": this.y -= speed; break;
                case "down": this.y += speed; break;
                case "left": this.x -= speed; break;
                case "right": this.x += speed; break;
            }
        }

        if (keyPressed) {
            spriteCounter++;
            if (spriteCounter >= 2) {
                spriteCounter = 0;
                spriteNum = spriteNum % 9 + 1;
            }
        }
    }

    public void draw(int screenX, int screenY) {
        String image = "";
        spriteNum = Math.max(1, Math.min(spriteNum, 9));

        image = switch (direction) {
            case "up" -> setImageUp(spriteNum);
            case "down" -> setImageDown(spriteNum);
            case "left" -> setImageLeft(spriteNum);
            case "right" -> setImageRight(spriteNum);
            default -> image;
        };
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
        if (spriteNum >= 1 && spriteNum <= 9) {
            return downImages[spriteNum - 1];
        }
        return "Invalid sprite number";
    }

    private String setImageUp(int spriteNum) {
        if (spriteNum >= 1 && spriteNum <= 9) {
            return upImages[spriteNum - 1];
        }
        return "Invalid sprite number";
    }

    private String setImageRight(int spriteNum) {
        if (spriteNum >= 1 && spriteNum <= 9) {
            return rightImages[spriteNum - 1];
        }
        return "Invalid sprite number";
    }

    private String setImageLeft(int spriteNum) {
        if (spriteNum >= 1 && spriteNum <= 9) {
            return leftImages[spriteNum - 1];
        }
        return "Invalid sprite number";
    }

}

