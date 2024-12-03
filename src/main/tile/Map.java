package tile;

public class Map {
    private Tile[][] tile;

    public Map() {
        int tileSize = 32;
        int rows = 64;
        int cols = 64;

        tile = new Tile[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                tile[row][col] = new Tile();
                tile[row][col].image = "src/object/tiles/wall.png";
                tile[row][col].x = col * tileSize;
                tile[row][col].y = row * tileSize;
            }
        }
    }

    public void draw() {
        for (int row = 0; row < tile.length; row++) {
            for (int col = 0; col < tile[row].length; col++) {
                tile[row][col].drawTile();
            }
        }
    }
}
