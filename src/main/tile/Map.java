package tile;

import java.awt.*;

public class Map {
    public Tile[][] tile;
    public Map() {
        int width=100;
        int height=100;
        tile = new Tile[16][16];
        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile[i].length; j++) {
                tile[i][j] = new Tile();
                this.tile[i][j].image = "src/object/tiles/grass.png";
                tile[i][j].x=width*i;
                tile[i][j].y=height*j;
            }
        }
    }

    public void draw(){
        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile[i].length; j++){
                tile [i][j].drawTile();
            }

        }
    }
}
