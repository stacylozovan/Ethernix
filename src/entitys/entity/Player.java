package entity;

import nl.saxion.app.SaxionApp;

public class Player extends Entity {
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 64;
        this.height = 64;
    }

    public void move(boolean[] keys) {
        if (keys['W']) this.y -= 5;
        if (keys['A']) this.x -= 5;
        if (keys['S']) this.y += 5;
        if (keys['D']) this.x += 5;
    }

    public void draw(int screenX, int screenY) {
        SaxionApp.drawImage("res/player.naruto", screenX, screenY, this.width, this.height);
    }
}
