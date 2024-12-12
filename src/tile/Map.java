import nl.saxion.app.SaxionApp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Map {
    private Tile[][] tile;
        private String[] tileImages = {
            "src/object/tiles/grass.png",
            "src/object/tiles/wall.png",
            "src/object/tiles/water.png",
            "src/object/tiles/earth.png",
            "src/object/tiles/tree.png",
            "src/object/tiles/sand.png",
        };

    public Map() {
        loadMapFromFile("src/object/simple_map.txt");
    }

    public void loadMapFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;

            int rows = Math.max(50, 64);
            int cols = Math.max(50, 64);
            tile = new Tile[rows][cols];

            while ((line = br.readLine()) != null && row < rows) {
                String[] values = line.split(" ");
                for (int col = 0; col < values.length && col < cols; col++) {
                    int tileType = Integer.parseInt(values[col]);
                    tile[row][col] = new Tile();
                    tile[row][col].image = tileImages[tileType];
                    tile[row][col].x = col * 50;
                    tile[row][col].y = row * 50;
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(int cameraX, int cameraY) {
        for (int row = 0; row < tile.length; row++) {
            for (int col = 0; col < tile[row].length; col++) {
                if (tile[row][col] != null) {
                    // Calculate the tile's screen position relative to the camera
                    int drawX = tile[row][col].x - cameraX;
                    int drawY = tile[row][col].y - cameraY;

                    if (drawX + 50 > 0 && drawX < 1000 && drawY + 50 > 0 && drawY < 1000) {
                        SaxionApp.drawImage(tile[row][col].image, drawX, drawY, 50, 50);
                    }
                }
            }
        }
    }

    public int getWidth() {
        return tile[0].length;
    }

    public int getHeight() {
        return tile.length; // Number of rows
    }
}
