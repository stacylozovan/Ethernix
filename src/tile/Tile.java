package tile;

import nl.saxion.app.SaxionApp;

import java.awt.*;

public class Tile {
    public String image;
    public int x, y;
    public boolean isSolid;

    public void drawTile() {
        SaxionApp.drawImage(image, x, y, tile.Map.TILE_SIZE, tile.Map.TILE_SIZE);
    }

    public Rectangle getCollisionBox() {
        return new Rectangle(this.x, this.y, tile.Map.TILE_SIZE, tile.Map.TILE_SIZE);
    }

    public void drawCollisionBox(int cameraX, int cameraY) {
        if (isSolid) {
            int drawX = x - cameraX;
            int drawY = y - cameraY;

            SaxionApp.setFill(Color.RED); // Define a cor para vermelho
            SaxionApp.drawRectangle(drawX, drawY, tile.Map.TILE_SIZE, tile.Map.TILE_SIZE); // Desenha o retângulo
        }
    }
}
