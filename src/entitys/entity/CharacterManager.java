package entity;

public class CharacterManager {
    private final Player player;
    private final Madara madara;

    public CharacterManager() {
        this.player = new Player();
        this.madara = new Madara();

        this.player.setDefaultValues();
        this.madara.setDefaultValues();
    }

    public void update(boolean[] keys) {
        player.update(keys);

    }

    public void handleCharacterInteractions() {
        if (Math.abs(player.getX() - madara.getX()) < 50 && Math.abs(player.getY() - madara.getY()) < 50) {
            player.takeDamage(10);
        }

        // Handle player death
        if (player.getHealth() <= 0) {
            System.out.println("Player is dead!");

        }

        // Handle Madara's death
        if (madara.getHealth() <= 0) {
            System.out.println("Madara is defeated!");

        }
    }

    public void draw(int playerScreenX, int playerScreenY, int cameraX, int cameraY) {
        player.draw(playerScreenX, playerScreenY);
        int madaraScreenX = madara.getX() - cameraX;
        int madaraScreenY = madara.getY() - cameraY;
        madara.draw(madaraScreenX, madaraScreenY);
    }

    public void displayHealthStatus() {
        System.out.println("Player Health: " + player.getHealth());
        System.out.println("Madara Health: " + madara.getHealth());
    }

    public Player getPlayer() {
        return player;
    }

    public Madara getMadara() {
        return madara;
    }

    // Check if the player is near Madara
    public boolean isPlayerNearMadara() {
        int playerX = player.getX();
        int playerY = player.getY();

        int madaraX = madara.getX();
        int madaraY = madara.getY();

        // Return true if the player is within a 50-pixel range of Madara's position
        return Math.abs(playerX - madaraX) < 50 && Math.abs(playerY - madaraY) < 50;
    }
}
