package tile;

import nl.saxion.app.SaxionApp;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
//import java.util.Map;
import java.util.Objects;

public class Map {
    public static tile.Tile[][] tile;
    public static final int TILE_SIZE = 50;
    private final java.util.Map<Integer, String> tileImages = new java.util.HashMap<>();

    public Map(String path) {
        loadTileImages("/object/tiles");
        String mapPath = Objects.requireNonNull(getClass().getResource(path),
                "Map file not found!").getPath();
        System.out.println("Map file path: " + mapPath);
        loadMapFromFile(mapPath);
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
                    // Extrair o ID do nome do arquivo
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
            int cols = 64;
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
                        tile[row][col].collision = (tileType == 1 || tileType == 4); // Exemplo: ajustar com base nos IDs
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
}