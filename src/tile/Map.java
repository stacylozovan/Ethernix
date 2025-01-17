package tile;

import nl.saxion.app.SaxionApp;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Map {
    public static tile.Tile[][] tile;
    public static final int TILE_SIZE = 50;
    private final java.util.Map<Integer, String> tileImages = new HashMap<>();
    private final Set<Integer> collisionTileTypes = new HashSet<>();

    public Map(String path) {
        initializeCollisionTileTypes();
        loadTileImages("/object/tiles");
        String mapPath = Objects.requireNonNull(getClass().getResource(path),
                "Map file not found!").getPath();
        System.out.println("Map file path: " + mapPath);
        loadMapFromFile(mapPath);
    }

    private void initializeCollisionTileTypes() {
        int[] collisionTiles = {
                0, 1, 2, 3, 4, 15, 16, 17, 20, 21, 22, 23, 24, 37,
                40, 41, 42, 43, 54, 60, 61, 62, 63, 80, 81, 82, 83, 86, 87, 94,
                100, 101, 102, 103, 104, 108, 109, 110, 111, 112, 113, 114, 115, 117, 120,
                121, 122, 123, 124, 128, 129, 130, 131, 134, 136, 140, 141, 142, 143, 144,
                146, 147, 148, 149, 150, 151, 152, 169, 170, 171, 172, 180, 181, 182,
                183, 184, 200, 201, 202, 203, 204, 205, 209, 210, 211, 212, 220, 221, 222,
                223, 224, 225, 229, 230, 231, 232, 249, 250
        };

        for (int tileId : collisionTiles) {
            collisionTileTypes.add(tileId);
        }
    }


    private void loadTileImages(String tileFolderPath) {
        try {
            File tileFolder = new File(Objects.requireNonNull(getClass().getResource(tileFolderPath)).getPath());
            File[] files = tileFolder.listFiles();

            if (files == null) {
                throw new IOException("Tile folder is empty or not found!");
            }

            for (File file : files) {
                String fileName = file.getName();
                if (fileName.startsWith("GK_JC_Free_") && fileName.endsWith(".png")) {
                    String idStr = fileName.replace("GK_JC_Free_", "").replace(".png", "");
                    try {
                        int id = Integer.parseInt(idStr);
                        tileImages.put(id, file.getPath());
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid tile ID in file name: " + fileName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMapFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int rows = 64;
            int cols = 120;
            tile = new tile.Tile[rows][cols];

            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < rows) {
                String[] values = line.split(" ");
                for (int col = 0; col < values.length && col < cols; col++) {
                    int tileType = Integer.parseInt(values[col]);
                    if (tileImages.containsKey(tileType)) {
                        tile[row][col] = new tile.Tile();
                        tile[row][col].image = tileImages.get(tileType);
                        tile[row][col].x = col * TILE_SIZE;
                        tile[row][col].y = row * TILE_SIZE;

                        tile[row][col].collision = collisionTileTypes.contains(tileType);
                    } else {
                        System.err.println("Tile ID not found: " + tileType);
                    }
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

    public void addCollisionTileType(int tileType) {
        collisionTileTypes.add(tileType);
    }

    public void removeCollisionTileType(int tileType) {
        collisionTileTypes.remove(tileType);
    }
}
