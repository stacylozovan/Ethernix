package tile;

import nl.saxion.app.SaxionApp;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Map {
    private tile.Tile[][] tile;
    public static final int TILE_SIZE = 50;
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
            tile = new tile.Tile[rows][cols];

            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < rows) {
                String[] values = line.split(" ");
                for (int col = 0; col < values.length && col < cols; col++) {
                    int tileType = Integer.parseInt(values[col]);
                    tile[row][col] = new tile.Tile();
                    tile[row][col].image = Objects.requireNonNull(
                            getClass().getResource(tileImages[tileType]),
                            "Image not found: " + tileImages[tileType]
                    ).getPath();
                    tile[row][col].x = col * TILE_SIZE;
                    tile[row][col].y = row * TILE_SIZE;
                    tile[row][col].isSolid = (tileType == 1 || tileType == 4); // define solids tiles
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(int cameraX, int cameraY) {
        int startCol = Math.max(0, cameraX / TILE_SIZE);
        int endCol = Math.min(tile[0].length, (cameraX + 1000) / TILE_SIZE + 1);

        int startRow = Math.max(0, cameraY / TILE_SIZE);
        int endRow = Math.min(tile.length, (cameraY + 1000) / TILE_SIZE + 1);

        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                if (tile[row][col] != null) {
                    int drawX = tile[row][col].x - cameraX;
                    int drawY = tile[row][col].y - cameraY;

                    SaxionApp.drawImage(tile[row][col].image, drawX, drawY, TILE_SIZE, TILE_SIZE);
//                    tile[row][col].drawCollisionBox(cameraX, cameraY);
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

    public boolean isTileSolid(int pixelX, int pixelY) {
        int tileX = (pixelX + Map.TILE_SIZE / 2) / Map.TILE_SIZE;
        int tileY = (pixelY + Map.TILE_SIZE / 2) / Map.TILE_SIZE;

        if (tileX < 0 || tileY < 0 || tileX >= tile[0].length || tileY >= tile.length) {
            return true;
        }

        return tile[tileY][tileX].isSolid;
    }

    public boolean isCollision(Rectangle playerBox) {
        int tileSize = Map.TILE_SIZE;

        // Limitar os tiles que serão verificados
        int startCol = Math.max(0, playerBox.x / tileSize);
        int endCol = Math.min(tile[0].length - 1, (playerBox.x + playerBox.width) / tileSize);

        int startRow = Math.max(0, playerBox.y / tileSize);
        int endRow = Math.min(tile.length - 1, (playerBox.y + playerBox.height) / tileSize);

        // Verificar apenas os tiles dentro da área do personagem
        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                if (tile[row][col] != null && tile[row][col].isSolid) {
                    Rectangle tileBox = tile[row][col].getCollisionBox();
                    if (playerBox.intersects(tileBox)) {
                        return true; // Colisão detectada
                    }
                }
            }
        }

        return false; // Sem colisão
    }


}
