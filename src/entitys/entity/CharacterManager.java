package entity;

public class CharacterManager {
    private Player player;
    private Madara madara;

    public CharacterManager() {
        this.player = new Player();
        this.madara = new Madara();

        this.player.setDefaultValues();
        this.madara.setDefaultValues();
    }

    public void update(boolean[] keys) {
        player.update(keys);
        madara.update(player.getX(), player.getY());
    }

    public void handleCharacterInteractions() {
        if (Math.abs(player.getX() - madara.getX()) < 50 && Math.abs(player.getY() - madara.getY()) < 50) {
            player.takeDamage(10);
        }

        if (player.getHealth() <= 0) {
            System.out.println("Player is dead!");
        }

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
}
