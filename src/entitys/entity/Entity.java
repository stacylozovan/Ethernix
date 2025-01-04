package entity;

import nl.saxion.app.SaxionApp;

import java.awt.Color;

public class Entity {
    public int x, y;
    public int speed;
    public int width = 50;
    public int height = 50;

    public String down0, down1, down2, down3, down4, down5, down6, down7, down8, down9, down10, down11, down12, down13, down14, down15, down16;
    public String left0, left1, left2, left3, left4, left5, left6, left7, left8;
    public String right0, right1, right2, right3, right4, right5, right6, right7, right8;
    public String up0, up1, up2, up3, up4, up5, up6, up7, up8;

    public String direction = "down";

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public int health = 100;

    public Entity() {}

    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, health);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void reset() {
        this.x = 0;
        this.y = 0;
        this.speed = 0;
        this.health = 100;
        this.direction = "down";
        this.spriteCounter = 0;
        this.spriteNum = 1;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(int screenX, int screenY) {
        SaxionApp.setFill(Color.GRAY);
        SaxionApp.drawRectangle(screenX, screenY, width, height);

        SaxionApp.setFill(Color.RED);
        int healthBarWidth = Math.max(0, health / 2);
        SaxionApp.drawRectangle(screenX, screenY - 10, healthBarWidth, 5);
    }
}
