package entity;

import java.awt.*;

public class Entity {
    public int x, y;
    public int speed;
    public int width, height;
    public String down0, down1, down2, down3, down4, down5, down6, down7, down8,down9,down10,down11,down12,down13,down14,down15,down16;
    public String left0, left1, left2, left3, left4, left5, left6, left7, left8;
    public String right0, right1, right2, right3, right4, right5, right6, right7, right8;
    public String up0, up1, up2, up3, up4, up5, up6, up7, up8;
    public Rectangle solidArea;
    public boolean collisionOn = false;

    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public int health;

    public Entity() {
        this.x = 0;
        this.y = 0;
        this.speed = 0;
        this.direction = "down";
        this.health = 100;
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


    public int getX() {
        return this.x;
    }


    public int getY() {
        return this.y;
    }


    public void reset() {
        this.x = 0;
        this.y = 0;
        this.speed = 0;
        this.health = 100;
        this.direction = "down";
    }
}
