package entity;

import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.KeyboardEvent;

import java.awt.Color;
import java.awt.event.MouseEvent;
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
    private final String charactersChoiceImage = "src/res/object/choice1.png";
    private final String attackChoiceImage = "src/res/object/attack_choice.png";
    private final String madaraNormalBall = "src/res/object/balls/ball.madara1.png";
    private final String madaraSpecialBall = "src/res/object/balls/ball.madara2.png";
    private final String madaraUltimateBall = "src/res/object/balls/ball.madara3.png";

    private final String narutoNormalBall = "src/res/object/balls/ball.player1.png";
    private final String narutoSpecialBall = "src/res/object/balls/ball.player2.png";
    private final String narutoUltimateBall = "src/res/object/balls/ball.player3.png";
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

    private void teleportToBattlePositions() {
        System.out.println("Teleporting players to battle positions...");

        // Set predefined positions for battle
        naruto.setPosition(200, 500); // Example position
        if (gojo != null) {
            gojo.setPosition(300, 500); // Example position
        }
        madara.setPosition(500, 200); // Example position

        System.out.println("Naruto's Position: " + naruto.getX() + ", " + naruto.getY());
        if (gojo != null) {
            System.out.println("Gojo's Position: " + gojo.getX() + ", " + gojo.getY());
        }
        System.out.println("Madara's Position: " + madara.getX() + ", " + madara.getY());
    }


    public void drawHealthBars() {
        SaxionApp.setFill(Color.RED);
        int madaraHealthWidth = madara.getHealth() * 2;
        SaxionApp.drawRectangle(275, 70, madaraHealthWidth, 20);
        SaxionApp.setFill(Color.BLACK);
        SaxionApp.drawText("Madara Health: " + madara.getHealth(), 275, 70, 20);

        if (activePlayer != null) {
            int healthBarWidth = activePlayer.getHealth() * 2;
            int healthBarX = 150;
            int healthBarY = (activePlayer == naruto) ? 900 : 950;

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

             SaxionApp.drawImage(charactersChoiceImage, 150, 730, 300, 250);
//
//            SaxionApp.drawImage("src/res/object/attack_choice.png", 550, 700, 550, 400);

            if (activePlayer == naruto) {
                SaxionApp.drawImage(narutoBattleSprite, 150, 470, 300, 300);
            } else if (activePlayer == gojo) {
                SaxionApp.drawImage(gojoBattleSprite, 150, 470, 300, 300);
            }


            SaxionApp.drawImage(madaraBattleSprite, 350, 230, 400, 400);
        } else {

            if (activePlayer == naruto) {
                naruto.draw(400, 600);
            } else if (activePlayer == gojo) {
                gojo.draw(400, 600);
            }
            madara.draw(600, 800);
        }

        // Draw attack animation if active
        if (isAnimatingAttack) {
            drawAttackAnimation();
        }
    }




    public void handleCombat() {
        if (playerTurn) {
            System.out.println("Player's turn: " + activePlayer.getName());

            // Show the action menu only during the player's turn
            displayActionMenu();
        } else {
            System.out.println("Madara's turn.");

            // Introduce a delay before Madara's attack
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    madaraRegularAttack();
                }
            }, 1000); // Delay of 1 second (adjust as needed)
        }

        // Display health at the end of the turn
        System.out.println("Naruto Health: " + naruto.getHealth());
        if (gojo != null) {
            System.out.println("Gojo Health: " + gojo.getHealth());
        }
        System.out.println("Madara Health: " + madara.getHealth());
    }



    public void displayActionMenu() {
        if (playerTurn) {
            // Draw the menu background
            SaxionApp.setFill(Color.LIGHT_GRAY);
            SaxionApp.drawRectangle(550, 600, 400, 270);

            // Display text options for actions
            SaxionApp.setFill(Color.BLACK);
            SaxionApp.drawText("What are you going to do?", 575, 650, 20);
            SaxionApp.drawText("1. Normal Attack (Click)", 575, 700, 20);
            SaxionApp.drawText("2. Special Attack (Press 'E')", 575, 750, 20);
            SaxionApp.drawText("3. Ultimate Attack (Press 'Q')", 575, 800, 20);
        }
    }



    public void handlePlayerAction(nl.saxion.app.interaction.MouseEvent mouseEvent, KeyboardEvent keyboardEvent) {
        if (playerTurn) {
            // Handle normal attack via left mouse button
            if (mouseEvent != null && mouseEvent.isLeftMouseButton() && mouseEvent.isMouseDown()) {
                startNarutoAttackAnimation("normal");
                madara.takeDamage(15); // Normal attack damage
                System.out.println("Player performed a normal attack. Madara takes 15 damage.");
                endPlayerTurn();
                return;
            }

            // Handle special attack via 'E' key
            if (keyboardEvent != null && keyboardEvent.getKeyCode() == KeyboardEvent.VK_E) {
                if (narutoSpecialReady || gojoUltimateCooldown) {
                    triggerSpecialAttack(); // Execute special attack
                } else {
                    System.out.println("Special attack is not ready.");
                }
                endPlayerTurn();
                return;
            }

            // Handle ultimate attack via 'Q' key
            if (keyboardEvent != null && keyboardEvent.getKeyCode() == KeyboardEvent.VK_Q) {
                if (narutoUltimateReady) {
                    triggerUltimateAttack(); // Execute ultimate attack
                } else {
                    System.out.println("Ultimate attack is not ready.");
                }
                endPlayerTurn();
                return;
            }
        } else {
            System.out.println("It's not the player's turn.");
        }
    }

    public boolean isPlayerTurn() {

        return playerTurn;
    }


    private void handleNormalAttack() {
        if (playerTurn) {
            startNarutoAttackAnimation("normal");
            madara.takeDamage(15); // Normal attack deals fixed damage
            System.out.println("Normal attack dealt 15 damage to Madara.");
            playerTurn = false; // End player's turn
        } else {
            System.out.println("It's not the player's turn!");
        }
    }
    private void endPlayerTurn() {
        playerTurn = false;

        // Introduce a delay before Madara's turn
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handleCombat(); // Trigger Madara's turn
            }
        }, 1000); // Delay of 1 second (adjust as needed)
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

            SaxionApp.drawImage(currentProjectileImage, currentX - 15, currentY - 15, 60, 60);
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
        if (madara.getHealth() > 0) {
            int madaraDamage = 20; // Fixed damage for Madara
            activePlayer.takeDamage(madaraDamage);

            System.out.println("Madara attacks " + activePlayer.getName() + " for " + madaraDamage + " damage!");
            System.out.println(activePlayer.getName() + "'s Health: " + activePlayer.getHealth());

            if (activePlayer.getHealth() <= 0) {
                System.out.println(activePlayer.getName() + " has been defeated!");

                // Switch to Gojo if Naruto is defeated
                if (activePlayer == naruto && gojo != null) {
                    switchPlayer(2);
                    System.out.println("Switching to Gojo!");
                } else if (activePlayer == gojo) {
                    System.out.println("Game Over! All players defeated.");
                    return; // End the game if all players are defeated
                }
            }
        }

        // End Madara's turn and switch back to the player
        playerTurn = true;
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
        } else {
            System.out.println("Invalid player switch request or Gojo is unavailable.");
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
