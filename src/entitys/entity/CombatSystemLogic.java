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

    private int attackStep = 0;
    private boolean isAnimatingAttack = false;
    private String currentProjectileImage = null;
    private final String narutoBattleSprite = "src/res/battle/naruto_battle.png";
    private final String gojoBattleSprite = "src/res/battle/gojo_battle.png";
    private final String madaraBattleSprite = "src/res/battle/madara_battle.png";
    private final String madaraNormalBall = "src/res/object/balls/ball1.png";
    private final String madaraSpecialBall = "src/res/object/balls/ball2.png";
    private final String madaraUltimateBall = "src/res/object/balls/ball3.png";

    private final String narutoNormalBall = "src/res/object/balls/ball4.png";
    private final String narutoSpecialBall = "src/res/object/balls/ball5.png";
    private final String narutoUltimateBall = "src/res/object/balls/ball6.png";
    private long narutoLastAttackTime = 0;
    private final long narutoAttackCooldown = 1000;
    private boolean gojoUltimateCooldown = false;
    private boolean inBattleMode = false;


    public CombatSystemLogic(Player naruto, Player gojo, Madara madara) {
        this.naruto = naruto;
        this.gojo = gojo;
        this.madara = madara;

        this.activePlayer = naruto;
        teleportToBattlePositions();

        startMadaraAttackCycle();
    }
    public void startBattle() {
        inBattleMode = true;
        System.out.println("Battle mode started.");
    }

    public void endBattle() {
        inBattleMode = false;
        System.out.println("Battle mode ended.");
    }

    public void teleportToBattlePositions() {
        naruto.setPosition(300, 800);
        gojo.setPosition(500, 600);
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
        if (inBattleMode) {
            // Draw Naruto if active
            if (activePlayer == naruto) {
                SaxionApp.drawImage(narutoBattleSprite, 150, 500, 300, 300);
            }

            // Draw Gojo if active
            if (activePlayer == gojo) {
                SaxionApp.drawImage(gojoBattleSprite, 150, 500, 300, 300);
            }

            // Draw Madara
            SaxionApp.drawImage(madaraBattleSprite, 600, 200, 400, 400);
        } else {
            // Draw regular sprites for the map
            if (activePlayer == naruto) {
                naruto.draw(300, 800);
            } else if (activePlayer == gojo) {
                gojo.draw(300, 800);
            }
            madara.draw(700, 400);
        }

        if (isAnimatingAttack) {
            drawAttackAnimation();
        }
    }

    public void handleCombat() {
        if (playerTurn) {
            if (activePlayer == naruto) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - narutoLastAttackTime < narutoAttackCooldown) {
                    System.out.println("Naruto is on cooldown!");
                    return;
                }
                narutoLastAttackTime = currentTime;
            }

            int playerDamage = 15;
            madara.takeDamage(playerDamage);
            startNarutoAttackAnimation("normal");

            System.out.println(activePlayer.getName() + " attacks Madara for " + playerDamage + " damage!");

            if (madara.getHealth() <= 0) {
                System.out.println("Madara is defeated! Battle is over.");
                return;
            }

            playerTurn = false;
        } else {
            if (madara.getHealth() > 0) {
                int madaraDamage = 20;
                activePlayer.takeDamage(madaraDamage);

                System.out.println("Madara attacks " + activePlayer.getName() + " for " + madaraDamage + " damage!");

                if (activePlayer.getHealth() <= 0) {
                    System.out.println(activePlayer.getName() + " has been defeated!");

                    if (activePlayer == naruto && gojo != null) {
                        switchPlayer(2);
                        System.out.println("Switching to Gojo!");
                    } else if (activePlayer == gojo) {
                        System.out.println("Game Over! All players defeated.");
                        return;
                    }
                }
            }

            playerTurn = true;
        }


        System.out.println("Naruto Health: " + naruto.getHealth());
        if (gojo != null) {
            System.out.println("Gojo Health: " + gojo.getHealth());
        }
        System.out.println("Madara Health: " + madara.getHealth());
    }

    private void applyStunEffect(Madara madara, int duration) {
        madara.setStunned(true);
        System.out.println("Madara is stunned!");


        SaxionApp.setFill(Color.GRAY);
        SaxionApp.drawRectangle(madara.getX() - 10, madara.getY() - 10, 20, 20); // Example visual


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                madara.setStunned(false);
                System.out.println("Madara is no longer stunned.");
            }
        }, duration);
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
            int baseDamage = 20; // Normal attack damage
            activePlayer.takeDamage(baseDamage);
            System.out.println("Madara attacks " + (activePlayer == naruto ? "Naruto" : "Gojo") + " for " + baseDamage + " damage!");


            if (Math.random() < 0.2) {
                String effect = (Math.random() < 0.5) ? "stun" : "slow"; // 50% chance for either effect
                applyStatusEffect(activePlayer, effect);
            }
        }
    }
    private void applyStatusEffect(Player player, String effect) {
        if (effect.equals("stun")) {
            player.setStunned(true); // Assuming Player has a setStunned method
            System.out.println(player.getName() + " is stunned!");
            Timer stunTimer = new Timer();
            stunTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    player.setStunned(false);
                    System.out.println(player.getName() + " is no longer stunned!");
                }
            }, 5000); // Stun duration: 5 seconds
        } else if (effect.equals("slow")) {
            int originalSpeed = player.getSpeed();
            player.setSpeed(originalSpeed / 2);
            System.out.println(player.getName() + " is slowed!");
            Timer slowTimer = new Timer();
            slowTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    player.setSpeed(originalSpeed);
                    System.out.println(player.getName() + " is no longer slowed!");
                }
            }, 5000); // Slow duration: 5 seconds
        }
    }

    private void checkMadaraHealthForUltimate() {
        int madaraMaxHealth = 300;

        // Trigger ultimate at 40% health
        if (!ultimateUsedAt50 && madara.getHealth() <= madaraMaxHealth * 0.4) {
            startMadaraAttackAnimation("ultimate");
            ultimateUsedAt50 = true;
            System.out.println("Madara's ultimate attack triggered at 40% health!");
        }

        if (!ultimateUsedAt25 && madara.getHealth() <= madaraMaxHealth * 0.5) {
            startMadaraAttackAnimation("ultimate");
            ultimateUsedAt25 = true;


            applyMadaraBuff();
            System.out.println("Madara gains a temporary buff at 25% health!");
        }
    }
    private void applyMadaraBuff() {
        madara.setAttackDamage(madara.getAttackDamage() + 10);

        if (attackTimer != null) {
            attackTimer.cancel();
        }
        attackTimer = new Timer();
        attackTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (madara.getHealth() > 0) {
                    startMadaraAttackAnimation("normal");
                }
            }
        }, 0, 15000);
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
            System.out.println("Naruto uses his ultimate attack! Madara takes 100 damage.");
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
