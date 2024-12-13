package entity;

public class CharacterManager {

    private Player player;
    private Madara madara;
    private NPC npc;

    public CharacterManager() {

        this.player = new Player();
        this.madara = new Madara();
        this.npc = new NPC("mark", 500, 500, new String[]{"teste de dialogo", "teste dialogo 2"}, "down", "static");

        this.player.setDefaultValues();
        this.madara.setDefaultValues();
    }


    public void update(boolean[] keys) {
        player.update(keys);
        madara.update(player);
    }


    public void handleCharacterInteractions() {
        if (Math.abs(player.getX() - madara.getX()) < 50 && Math.abs(player.getY() - madara.getY()) < 50) {
            player.takeDamage(10);
        }


        if (player.getHealth() <= 0) {
            System.out.println("Player is dead!");

        }

        if (madara.getHealth() <= 0) {
            System.out.println(" is defeated!");

        }
    }


    public void draw() {
        player.draw();
        madara.draw();
        npc.draw();
    }


    public void displayHealthStatus() {
        System.out.println("Player Health: " + player.getHealth());
        System.out.println(" Health: " + madara.getHealth());
    }


    public Player getPlayer() {
        return player;
    }

    public Madara getMadara() {
        return madara;
    }
}