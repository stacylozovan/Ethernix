package main;

import entity.Entity;
import tile.Map;
import entity.NPC;

import java.util.List;

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
                    return !entity.isInteractable; // Ignore collision for interactable entities
                }
            }
            case "down" -> {
                entityBottomRow = (entityBottomY + entity.speed) / Map.TILE_SIZE;
                entityLeftCol = entityLeftX / Map.TILE_SIZE;
                entityRightCol = entityRightX / Map.TILE_SIZE;

                if (isTileCollision(entityLeftCol, entityBottomRow) ||
                        isTileCollision(entityRightCol, entityBottomRow)) {
                    return !entity.isInteractable; // Ignore collision for interactable entities
                }
            }
            case "left" -> {
                entityLeftCol = (entityLeftX - entity.speed) / Map.TILE_SIZE;
                entityTopRow = entityTopY / Map.TILE_SIZE;
                entityBottomRow = entityBottomY / Map.TILE_SIZE;

                if (isTileCollision(entityLeftCol, entityTopRow) ||
                        isTileCollision(entityLeftCol, entityBottomRow)) {
                    return !entity.isInteractable; // Ignore collision for interactable entities
                }
            }
            case "right" -> {
                entityRightCol = (entityRightX + entity.speed) / Map.TILE_SIZE;
                entityTopRow = entityTopY / Map.TILE_SIZE;
                entityBottomRow = entityBottomY / Map.TILE_SIZE;

                if (isTileCollision(entityRightCol, entityTopRow) ||
                        isTileCollision(entityRightCol, entityBottomRow)) {
                    return !entity.isInteractable; // Ignore collision for interactable entities
                }
            }
        }
        return false;
    }

    public boolean checkNPCCollision(Entity player, List<NPC> npcs) {
        for (NPC npc : npcs) {
            if (npc.isVisible) {
                // Predict the player's next position based on their direction
                int nextX = player.x;
                int nextY = player.y;

                switch (player.direction) {
                    case "up" -> nextY -= player.speed;
                    case "down" -> nextY += player.speed;
                    case "left" -> nextX -= player.speed;
                    case "right" -> nextX += player.speed;
                }

                // Check if the next position overlaps with the NPC's position
                if (nextX + player.solidArea.x < npc.x + npc.solidArea.x + npc.solidArea.width &&
                        nextX + player.solidArea.x + player.solidArea.width > npc.x + npc.solidArea.x &&
                        nextY + player.solidArea.y < npc.y + npc.solidArea.y + npc.solidArea.height &&
                        nextY + player.solidArea.y + player.solidArea.height > npc.y + npc.solidArea.y) {
                    return true; // Collision detected
                }
            }
        }
        return false; // No collision
    }


    private boolean isTileCollision(int col, int row) {
        if (col < 0 || col >= map.getWidth() || row < 0 || row >= map.getHeight()) {
            return true;
        }
        return Map.tile[row][col] != null && Map.tile[row][col].collision;
    }

    public boolean checkProximity(Entity entity1, Entity entity2, int range) {
        return Math.abs(entity1.x - entity2.x) < range &&
                Math.abs(entity1.y - entity2.y) < range;
    }
}
