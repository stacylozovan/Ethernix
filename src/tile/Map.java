import nl.saxion.app.SaxionApp;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Map {
    private Tile[][] tile;
    private final String[] tileImages = {
            "/object/tiles/grass.png",
            "/object/tiles/wall.png",
            "/object/tiles/water.png",
            "/object/tiles/earth.png",
            "/object/tiles/tree.png",
            "/object/tiles/sand.png"
    };

    public Map() {
        String mapPath = Objects.requireNonNull(getClass().getResource("/object/simple_map.txt"),
                "Map file not found!").getPath();
        System.out.println("Map file path: " + mapPath);
        loadMapFromFile(mapPath);
    }


    public void loadMapFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int rows = 64;
            int cols = 64;
            tile = new Tile[rows][cols];

            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < rows) {
                String[] values = line.split(" ");
                for (int col = 0; col < values.length && col < cols; col++) {
                    int tileType = Integer.parseInt(values[col]);
                    tile[row][col] = new Tile();
                    tile[row][col].image = Objects.requireNonNull(
                            getClass().getResource(tileImages[tileType]),
                            "Image not found: " + tileImages[tileType]
                    ).getPath();
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
        int startCol = Math.max(0, cameraX / 50);
        int endCol = Math.min(tile[0].length, (cameraX + 1000) / 50 + 1);

        int startRow = Math.max(0, cameraY / 50);
        int endRow = Math.min(tile.length, (cameraY + 1000) / 50 + 1);

        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                if (tile[row][col] != null) {
                    int drawX = tile[row][col].x - cameraX;
                    int drawY = tile[row][col].y - cameraY;

                    SaxionApp.drawImage(tile[row][col].image, drawX, drawY, 50, 50);
                }
            }
        }
    }

    public int getWidth() {
        return tile[0].length;
    }

    public int getHeight() {
        return tile.length;
    }
}
