package entity;

import nl.saxion.app.SaxionApp;
import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

public class CombatSystemLogic {
    private final Player naruto;
    private final Player gojo;
    private final Madara madara;

    private Player activePlayer;
    private boolean playerTurn = true;

    private Timer attackTimer;
    private boolean ultimateUsedAt50 = false;
    private boolean ultimateUsedAt25 = false;

    private int narutoDamageCounter = 0;
    private int narutoSpecialPoints = 0;
    private boolean narutoSpecialReady = false;
    private boolean narutoUltimateReady = false;

    private int attackStep = 0; // For animation steps
    private boolean isAnimatingAttack = false; // Track if animation is running
    private String currentProjectileImage = null;

    private final String madaraNormalBall = "src/res/object/balls/ball1.png";
    private final String madaraSpecialBall = "src/res/object/balls/ball2.png";
    private final String madaraUltimateBall = "src/res/object/balls/ball3.png";

    private final String narutoNormalBall = "src/res/object/balls/ball4.png";
    private final String narutoSpecialBall = "src/res/object/balls/ball5.png";
    private final String narutoUltimateBall = "src/res/object/balls/ball6.png";

    public CombatSystemLogic(Player naruto, Player gojo, Madara madara) {
        this.naruto = naruto;
        this.gojo = gojo;
        this.madara = madara;

        this.activePlayer = naruto;
        teleportToBattlePositions();

        startMadaraAttackCycle();
    }

    public void teleportToBattlePositions() {
        naruto.setPosition(300, 800);
        gojo.setPosition(500, 800);
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
            int healthBarY = activePlayer.getY() - 25;

            SaxionApp.setFill(Color.RED);
            SaxionApp.drawRectangle(healthBarX, healthBarY, healthBarWidth, 10);
            SaxionApp.setFill(Color.BLACK);
            SaxionApp.drawText(
                    (activePlayer == naruto ? "Naruto" : "Gojo") + " Health: " + activePlayer.getHealth(),
                    healthBarX, healthBarY - 10, 10
            );
        }
    }

    public void drawBattleField() {
        if (activePlayer == naruto) {
            naruto.draw(300, 800);
        } else if (activePlayer == gojo) {
            gojo.draw(300, 800);
        }
        madara.draw(700, 400);

        if (isAnimatingAttack) {
            drawAttackAnimation();
        }
    }

    public void handleCombat() {
        if (playerTurn) {
            int damage = 15;
            madara.takeDamage(damage);
            startNarutoAttackAnimation("normal");

            if (activePlayer == naruto) {
                narutoDamageCounter += damage;

                if (narutoDamageCounter >= 50 && !narutoSpecialReady) {
                    narutoSpecialPoints += 10;
                }

                if (narutoSpecialPoints >= 100 && !narutoSpecialReady) {
                    narutoSpecialReady = true;
                    System.out.println("Naruto's special attack is ready! Press 'E' to use.");
                }

                if (narutoDamageCounter >= 150 && !narutoUltimateReady) {
                    narutoUltimateReady = true;
                    System.out.println("Naruto's ultimate attack is ready! Press 'Q' to use.");
                }
            }

            System.out.println((activePlayer == naruto ? "Naruto" : "Gojo") + " attacks Madara for " + damage + " damage!");
            checkMadaraHealthForUltimate();
        }

        playerTurn = !playerTurn;

        System.out.println("Naruto Health: " + naruto.getHealth());
        if (gojo != null) {
            System.out.println("Gojo Health: " + gojo.getHealth());
        }
        System.out.println("Madara Health: " + madara.getHealth());
    }

    private void startNarutoAttackAnimation(String attackType) {
        isAnimatingAttack = true;
        attackStep = 0;

        switch (attackType) {
            case "normal":
                currentProjectileImage = narutoNormalBall;
                break;
            case "special":
                currentProjectileImage = narutoSpecialBall;
                break;
            case "ultimate":
                currentProjectileImage = narutoUltimateBall;
                break;
        }

        Timer animationTimer = new Timer();
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (attackStep >= 20) {
                    isAnimatingAttack = false;
                    animationTimer.cancel();
                } else {
                    attackStep++;
                }
            }
        }, 0, 50);
    }

    private void drawAttackAnimation() {
        if (currentProjectileImage != null) {
            int startX, startY, endX, endY;

            if (currentProjectileImage.equals(madaraNormalBall) || currentProjectileImage.equals(madaraSpecialBall) || currentProjectileImage.equals(madaraUltimateBall)) {
                startX = madara.getX();
                startY = madara.getY();
                endX = activePlayer.getX();
                endY = activePlayer.getY();
            } else {
                startX = naruto.getX();
                startY = naruto.getY();
                endX = madara.getX();
                endY = madara.getY();
            }

            double progress = attackStep / 20.0;
            int currentX = (int) (startX + progress * (endX - startX));
            int currentY = (int) (startY + progress * (endY - startY));

            SaxionApp.drawImage(currentProjectileImage, currentX - 15, currentY - 15, 30, 30);
        }
    }

    private void startMadaraAttackCycle() {
        attackTimer = new Timer();
        attackTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (madara.getHealth() > 0) {
                    startMadaraAttackAnimation("normal");
                }
            }
        }, 0, 20000);
    }

    private void startMadaraAttackAnimation(String attackType) {
        isAnimatingAttack = true;
        attackStep = 0;

        switch (attackType) {
            case "normal":
                currentProjectileImage = madaraNormalBall;
                break;
            case "special":
                currentProjectileImage = madaraSpecialBall;
                break;
            case "ultimate":
                currentProjectileImage = madaraUltimateBall;
                break;
        }

        Timer animationTimer = new Timer();
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (attackStep >= 20) {
                    isAnimatingAttack = false;
                    animationTimer.cancel();
                    madaraRegularAttack();
                } else {
                    attackStep++;
                }
            }
        }, 0, 50);
    }

    private void madaraRegularAttack() {
        if (activePlayer != null && activePlayer.getHealth() > 0) {
            activePlayer.takeDamage(15);
            System.out.println("Madara attacks " + (activePlayer == naruto ? "Naruto" : "Gojo") + " for 15 damage!");
        }
    }

    private void checkMadaraHealthForUltimate() {
        int madaraMaxHealth = 200;

        if (!ultimateUsedAt50 && madara.getHealth() <= madaraMaxHealth * 0.5) {
            startMadaraAttackAnimation("ultimate");
            ultimateUsedAt50 = true;
        }

        if (!ultimateUsedAt25 && madara.getHealth() <= madaraMaxHealth * 0.25) {
            startMadaraAttackAnimation("ultimate");
            ultimateUsedAt25 = true;
        }
    }

    public void triggerSpecialAttack() {
        if (narutoSpecialReady && activePlayer == naruto) {
            startNarutoAttackAnimation("special");
            madara.takeDamage(50);
            narutoSpecialPoints = 0;
            narutoSpecialReady = false;
            System.out.println("Naruto used his special attack! Madara takes 50 damage.");
        }
    }

    public void triggerUltimateAttack() {
        if (narutoUltimateReady && activePlayer == naruto) {
            startNarutoAttackAnimation("ultimate");
            madara.takeDamage(100);
            narutoUltimateReady = false;
            System.out.println("Naruto used his ultimate attack! Madara takes 100 damage.");
        }
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

    public void stopCombat() {
        if (attackTimer != null) {
            attackTimer.cancel();
        }
    }
}
