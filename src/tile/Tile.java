package tile;

import nl.saxion.app.SaxionApp;

public class Tile {
    public String image;
    public int x, y;

    public void drawTile() {
        SaxionApp.drawImage(image, x, y, 32, 32);
    }
}
