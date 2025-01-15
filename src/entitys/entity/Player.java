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

    private final String design;
    main.CollisionChecker cChecker;

    private double exactX, exactY;
    private int shield = 0;
    private double attackMultiplier = 1.0;
    private boolean isBuffed = false;
    public static final int PLAYER_SIZE = 64;

    public Player(String design, main.CollisionChecker cChecker) {
        this.design = design;
        this.cChecker = cChecker;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 900;
        y = 1100;
        exactX = x;
        exactY = y;
        speed = 10;
        health = 200;
        direction = "down";
        width = PLAYER_SIZE;
        height = PLAYER_SIZE;
        solidArea = new Rectangle();
        solidArea.x = 16;
        solidArea.y = 30;
        solidArea.width = 32;
        solidArea.height = 34;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.exactX = x;
        this.exactY = y;
    }

    private String getImagePath(String direction, int frame) {
        String basePath;
        if (design.equals("naruto")) {
            basePath = "src/res/player/naruto";
        } else if (design.equals("gojo")) {
            basePath = "src/res/player.gojo";
        } else {
            return "";
        }
        return String.format("%s/%s/%s_%s%d.png", basePath, direction, design, direction, frame);
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

        // CHECK NPC COLLISION
        boolean npcCollision = this.cChecker.checkNPCCollision(this, CharacterManager.npcs);

//        IF COLLISION IS FALSE PLAYER CAN MOVE
        if(keyPressed && !collisionOn && !npcCollision) {
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

        if (design.equals("gojo")) {
            SaxionApp.drawImage(image, screenX, screenY, 75, 75);
        } else {
            SaxionApp.drawImage(image, screenX, screenY, 64, 64);
        }
    }

    @Override
    public void takeDamage(int damage) {
        if (shield > 0) {
            int remainingDamage = damage - shield;
            shield = Math.max(0, shield - damage);
            if (remainingDamage > 0) {
                health -= remainingDamage;
            }
        } else {
            health -= damage;
        }
        if (health < 0) {
            health = 0;
        }
    }

    public void setShield(int shield) {
        this.shield = shield;
        System.out.println(getName() + " gains a shield of " + shield + " HP!");
    }

    public void removeShield() {
        this.shield = 0; // Set shield value to 0
        System.out.println(getName() + "'s shield has been removed!");
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
