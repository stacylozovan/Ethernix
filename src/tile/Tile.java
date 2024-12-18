package tile;

import nl.saxion.app.SaxionApp;

public class Tile {
    public String image;
    public int x, y;
    public boolean isSolid;

    public void drawTile() {
        SaxionApp.drawImage(image, x, y, tile.Map.TILE_SIZE, tile.Map.TILE_SIZE);
    }
}
