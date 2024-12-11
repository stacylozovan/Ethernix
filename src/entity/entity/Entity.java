package entity;

public class Entity {
    public int x, y;
    public int speed;
    public String down1, down2, down3, down4, down5, down6, down7, down8, down9, down10, down11, down12, down13, down14, down15, down16;
    public String left1;
    public String right1;

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
