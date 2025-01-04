package entity;

import nl.saxion.app.SaxionApp;
import java.awt.Color;

public class CombatSystemLogic {
    private final Player naruto;
    private final Player gojo;
    private final Madara madara;

    private Player activePlayer;
    private boolean playerTurn = true;

    public CombatSystemLogic(Player naruto, Player gojo, Madara madara) {
        this.naruto = naruto;
        this.gojo = gojo;
        this.madara = madara;

        this.activePlayer = naruto;
        teleportToBattlePositions();
    }

    public void teleportToBattlePositions() {
        naruto.setPosition(300, 800);
        gojo.setPosition(500, 800); // Gojo's position is set but not used unless switched to
        madara.setPosition(700, 400);
    }

    public void drawHealthBars() {

        SaxionApp.setFill(Color.RED);
        int madaraHealthWidth = madara.getHealth() * 2;
        SaxionApp.drawRectangle(650, 350, madaraHealthWidth, 20);
        SaxionApp.setFill(Color.BLACK);
        SaxionApp.drawText("Madara Health: " + madara.getHealth(), 650, 340, 12);


        if (activePlayer != null) {
            int healthBarWidth = activePlayer.getHealth() * 2;
            int healthBarX = activePlayer.getX() - (healthBarWidth / 2);
            int healthBarY = activePlayer.getY() - 25; // Adjusted slightly for visibility

            SaxionApp.setFill(Color.RED);
            SaxionApp.drawRectangle(healthBarX, healthBarY, healthBarWidth, 10);
            SaxionApp.setFill(Color.BLACK);
            SaxionApp.drawText(
                    (activePlayer == naruto ? "Naruto" : "Gojo") + " Health: " + activePlayer.getHealth(),
                    healthBarX, healthBarY - 10, 10
            );
        } else {
            System.err.println("Error: Active player is null. Cannot draw health bar.");
        }
    }





    public void drawBattleField() {
        if (activePlayer == naruto) {
            naruto.draw(300, 800);
        } else if (activePlayer == gojo) {
            gojo.draw(300, 800); // Active player always drawn in the same position
        }
        madara.draw(700, 400);
    }

    public void handleCombat() {
        if (playerTurn) {
            madara.takeDamage(20);
            System.out.println((activePlayer == naruto ? "Naruto" : "Gojo") + " attacks Madara for 20 damage!");
        } else {
            if (Math.random() < 0.5) {
                naruto.takeDamage(15);
                System.out.println("Madara attacks Naruto for 15 damage!");
            } else if (gojo != null) {
                gojo.takeDamage(15);
                System.out.println("Madara attacks Gojo for 15 damage!");
            }
        }

        playerTurn = !playerTurn;

        System.out.println("Naruto Health: " + naruto.getHealth());
        if (gojo != null) {
            System.out.println("Gojo Health: " + gojo.getHealth());
        }
        System.out.println("Madara Health: " + madara.getHealth());
    }

    public void switchPlayer(int playerNumber) {
        if (playerNumber == 1) {
            activePlayer = naruto;
            System.out.println("Switched to Naruto.");
        } else if (playerNumber == 2 && gojo != null) {
            activePlayer = gojo;
            System.out.println("Switched to Gojo.");
        }
    }

    public boolean isBattleOver() {
        boolean allPlayersDefeated = naruto.getHealth() <= 0 && (gojo == null || gojo.getHealth() <= 0);
        return allPlayersDefeated || madara.getHealth() <= 0;
    }
}
