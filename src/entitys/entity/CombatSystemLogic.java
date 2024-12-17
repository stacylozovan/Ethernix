package entity;

import nl.saxion.app.SaxionApp;

import java.awt.Color;

public class CombatSystemLogic {
    private final Player player;
    private final Madara madara;

    private boolean playerTurn = true;

    public CombatSystemLogic(Player player, Madara madara) {
        this.player = player;
        this.madara = madara;

        teleportToBattlePositions();
    }
    public void teleportToBattlePositions() {
        player.setPosition(300, 800);  // Player at the bottom
        madara.setPosition(700, 400);  // Madara a bit higher
    }

    public void drawHealthBars() {
        SaxionApp.setFill(Color.RED);
        int playerHealthWidth = player.getHealth() * 2;
        SaxionApp.drawRectangle(250, 750, playerHealthWidth, 20);

        SaxionApp.setFill(Color.BLACK);
        SaxionApp.drawText("Player Health: " + player.getHealth(), 250, 740, 12);

        SaxionApp.setFill(Color.RED);
        int madaraHealthWidth = madara.getHealth() * 2;
        SaxionApp.drawRectangle(650, 350, madaraHealthWidth, 20);

        SaxionApp.setFill(Color.BLACK);
        SaxionApp.drawText("Madara Health: " + madara.getHealth(), 650, 340, 12);
    }

    public void handleCombat() {
        if (playerTurn) {
            madara.takeDamage(15);
            System.out.println("Player attacks Madara for 15 damage!");
        } else {
            player.takeDamage(10);
            System.out.println("Madara attacks Player for 10 damage!");
        }

        playerTurn = !playerTurn;

        System.out.println("Player Health: " + player.getHealth());
        System.out.println("Madara Health: " + madara.getHealth());
    }

    public boolean isBattleOver() {
        return player.getHealth() <= 0 || madara.getHealth() <= 0;
    }
}
