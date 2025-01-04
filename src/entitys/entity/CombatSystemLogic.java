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
        gojo.setPosition(500, 800);
        madara.setPosition(700, 400);
    }

    public void drawHealthBars() {
        // Naruto Health Bar
        SaxionApp.setFill(Color.RED);
        int narutoHealthWidth = naruto.getHealth() * 2;
        SaxionApp.drawRectangle(250, 750, narutoHealthWidth, 20);
        SaxionApp.setFill(Color.BLACK);
        SaxionApp.drawText("Naruto Health: " + naruto.getHealth(), 250, 740, 12);

        // Gojo Health Bar
        if (gojo != null) {
            SaxionApp.setFill(Color.RED);
            int gojoHealthWidth = gojo.getHealth() * 2;
            SaxionApp.drawRectangle(450, 750, gojoHealthWidth, 20);
            SaxionApp.setFill(Color.BLACK);
            SaxionApp.drawText("Gojo Health: " + gojo.getHealth(), 450, 740, 12);
        }

        // Madara Health Bar
        SaxionApp.setFill(Color.RED);
        int madaraHealthWidth = madara.getHealth() * 2;
        SaxionApp.drawRectangle(650, 350, madaraHealthWidth, 20);
        SaxionApp.setFill(Color.BLACK);
        SaxionApp.drawText("Madara Health: " + madara.getHealth(), 650, 340, 12);
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
