package tile;

import nl.saxion.app.SaxionApp;

public class Tile {
    String image;
    int x;
    int y;
    int width,height;

    public void drawTile(){
        SaxionApp.drawImage(image, x, y);
    }
}
