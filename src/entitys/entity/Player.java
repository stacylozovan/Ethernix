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
    public Player(){
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 500;
        y = 500;
        speed = 8;
        direction = "down";
        width = PLAYER_SIZE;
        height = PLAYER_SIZE;
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
            int maxMove = calculateMaxMove(this.x, this.y, 0, -speed, gameMap);
            this.y -= maxMove;
            keyPressed = true;
        }
        if (keys[KeyboardEvent.VK_DOWN] || keys[KeyboardEvent.VK_S]) {
            direction = "down";
            int maxMove = calculateMaxMove(this.x, this.y, 0, speed, gameMap);
            this.y += maxMove;
            keyPressed = true;
        }
        if (keys[KeyboardEvent.VK_LEFT] || keys[KeyboardEvent.VK_A]) {
            direction = "left";
            int maxMove = calculateMaxMove(this.x, this.y, -speed, 0, gameMap);
            this.x -= maxMove;
            keyPressed = true;
        }
        if (keys[KeyboardEvent.VK_RIGHT] || keys[KeyboardEvent.VK_D]) {
            direction = "right";
            int maxMove = calculateMaxMove(this.x, this.y, speed, 0, gameMap);
            this.x += maxMove;
            keyPressed = true;
        }

        if (keyPressed) {
            spriteCounter++;
            if (spriteCounter >= 2) {
                spriteCounter = 0;
                spriteNum = spriteNum % 9 + 1;
            }
        }
    }

    private int calculateMaxMove(int currentX, int currentY, int deltaX, int deltaY, tile.Map gameMap) {
        int maxMove = 0;

        // Iterar de 1 até o valor de speed, verificando colisão passo a passo
        for (int i = 1; i <= Math.abs(deltaX + deltaY); i++) {
            int testX = currentX + (deltaX != 0 ? (deltaX > 0 ? i : -i) : 0);
            int testY = currentY + (deltaY != 0 ? (deltaY > 0 ? i : -i) : 0);

            // Simula a caixa de colisão ajustada
            Rectangle testBox = new Rectangle(testX, testY, 32, 48); // Use as mesmas dimensões da colisão

            if (gameMap.isCollision(testBox)) {
                break; // Parar se houver colisão
            }

            maxMove = i; // Atualizar a distância máxima possível
        }

        return maxMove;
    }


    public void drawCollisionBox(int cameraX, int cameraY) {
        Rectangle collisionBox = getCollisionBox();
        int drawX = collisionBox.x - cameraX;
        int drawY = collisionBox.y - cameraY;

        SaxionApp.setBorderColor(Color.RED);
        SaxionApp.drawRectangle(drawX, drawY, collisionBox.width, collisionBox.height);
    }


//    public void update(boolean[] keys, tile.Map gameMap) {
//        boolean keyPressed = false;
//
//        if (keys[KeyboardEvent.VK_UP] || keys[KeyboardEvent.VK_W]) {
//            direction = "up";
//            if (!gameMap.isTileSolid(this.x, this.y - speed)) {
//                this.y -= speed;
//            }
//            keyPressed = true;
//        }
//        if (keys[KeyboardEvent.VK_DOWN] || keys[KeyboardEvent.VK_S]) {
//            direction = "down";
//            if (!gameMap.isTileSolid(this.x, this.y + speed)) {
//                this.y += speed;
//            }
//            keyPressed = true;
//        }
//        if (keys[KeyboardEvent.VK_LEFT] || keys[KeyboardEvent.VK_A]) {
//            direction = "left";
//            if (!gameMap.isTileSolid(this.x - speed, this.y)) {
//                this.x -= speed;
//            }
//            keyPressed = true;
//        }
//        if (keys[KeyboardEvent.VK_RIGHT] || keys[KeyboardEvent.VK_D]) {
//            direction = "right";
//            if (!gameMap.isTileSolid(this.x + speed, this.y)) {
//                this.x += speed;
//            }
//            keyPressed = true;
//        }
//
//        if (keyPressed) {
//            spriteCounter++;
//            if (spriteCounter >= 2) {
//                spriteCounter = 0;
//                spriteNum = spriteNum % 9 + 1;
//            }
//        }
//    }

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

    public Rectangle getCollisionBox() {
        int boxWidth = 32;  // Reduza o tamanho do retângulo (ajuste fino)
        int boxHeight = 48; // Ajuste a altura para representar o "corpo" do personagem
        int offsetX = (PLAYER_SIZE - boxWidth) / 2;  // Centraliza horizontalmente
        int offsetY = (PLAYER_SIZE - boxHeight) / 2; // Centraliza verticalmente

        return new Rectangle(this.x + offsetX, this.y + offsetY, boxWidth, boxHeight);
    }
}

