package tile;

import nl.saxion.app.SaxionApp;

import java.awt.*;

public class Tile {
    public String image;
    public int x, y;
    public boolean collision = false;

    public void drawTile() {
        SaxionApp.drawImage(image, x, y, tile.Map.TILE_SIZE, tile.Map.TILE_SIZE);
    }
}
