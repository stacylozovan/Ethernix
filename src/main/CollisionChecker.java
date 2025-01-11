package main;

import entity.Entity;
import tile.Map;

import javax.swing.*;

public class CollisionChecker {
    private final Map map;

    public CollisionChecker(Map map) {
        this.map = map;
    }

    public boolean checkTile(Entity entity) {
        int entityLeftX = entity.x + entity.solidArea.x;
        int entityRightX = entity.x + entity.solidArea.x + entity.solidArea.width;
        int entityTopY = entity.y + entity.solidArea.y;
        int entityBottomY = entity.y + entity.solidArea.y + entity.solidArea.height;


        int entityLeftCol, entityRightCol, entityTopRow, entityBottomRow;

        switch (entity.direction) {
            case "up" -> {
                entityTopRow = (entityTopY - entity.speed) / Map.TILE_SIZE;
                entityLeftCol = entityLeftX / Map.TILE_SIZE;
                entityRightCol = entityRightX / Map.TILE_SIZE;

                if (isTileCollision(entityLeftCol, entityTopRow) ||
                        isTileCollision(entityRightCol, entityTopRow)) {
                    return true;
                }
            }
            case "down" -> {
                entityBottomRow = (entityBottomY + entity.speed) / Map.TILE_SIZE;
                entityLeftCol = entityLeftX / Map.TILE_SIZE;
                entityRightCol = entityRightX / Map.TILE_SIZE;

                if (isTileCollision(entityLeftCol, entityBottomRow) ||
                        isTileCollision(entityRightCol, entityBottomRow)) {
                    return true;
                }
            }
            case "left" -> {
                entityLeftCol = (entityLeftX - entity.speed) / Map.TILE_SIZE;
                entityTopRow = entityTopY / Map.TILE_SIZE;
                entityBottomRow = entityBottomY / Map.TILE_SIZE;

                if (isTileCollision(entityLeftCol, entityTopRow) ||
                        isTileCollision(entityLeftCol, entityBottomRow)) {
                    return true;
                }
            }
            case "right" -> {
                entityRightCol = (entityRightX + entity.speed) / Map.TILE_SIZE;
                entityTopRow = entityTopY / Map.TILE_SIZE;
                entityBottomRow = entityBottomY / Map.TILE_SIZE;

                if (isTileCollision(entityRightCol, entityTopRow) ||
                        isTileCollision(entityRightCol, entityBottomRow)) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isTileCollision(int col, int row) {
        if (col < 0 || col >= map.getWidth() || row < 0 || row >= map.getHeight()) {
            return true;
        }

        return Map.tile[row][col] != null && Map.tile[row][col].collision;
    }
}
