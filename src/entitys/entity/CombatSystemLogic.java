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
    private String actionMessage = null; // The message to display
    private long messageDisplayTime = 0; // The time when the message started
    private final long messageDuration = 2000; // Display duration in milliseconds
    private boolean isTutorialPhase = true; // Starts in the tutorial phase
    private final String tutorialImage = "src/res/battle/tutorial.png"; // Path to the tutorial image
    private final String tutorialImage1 = "src/res/battle/tutorial1.png";
    private final String tutorialImage2 = "src/res/battle/tutorial2.png";
    private final String tutorialImage3 = "src/res/battle/tutorial3.png";
    private final String tutorialImage4 = "src/res/battle/tutorial4.png";
    private final String tutorialImage5 = "src/res/battle/tutorial5.png";
    private boolean isBattleCompleted = false;



    public CombatSystemLogic(Player naruto, Player gojo, Madara madara) {
        this.naruto = naruto;
        this.gojo = gojo;
        this.madara = madara;

        this.activePlayer = naruto;
        teleportToBattlePositions();

        startMadaraAttackCycle();
    }
    private final String[] tutorialImages = {
            "src/res/battle/tutorial1.png",
            "src/res/battle/tutorial2.png",
            "src/res/battle/tutorial3.png",
            "src/res/battle/tutorial4.png",
            "src/res/battle/tutorial5.png",
            "src/res/battle/tutorial.png" // Final tutorial image
    };
    private int currentTutorialStep = 0; // Track the current step globally


    public void startBattle() {
        if (isBattleCompleted) {
            System.out.println("Battle has already ended. Tutorial phase will not restart.");
            return; // Do not start the tutorial phase if the battle is already over
        }

        System.out.println("Starting tutorial phase...");

        Timer tutorialTimer = new Timer();
        tutorialTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentTutorialStep < tutorialImages.length - 1) {
                    currentTutorialStep++; // Move to the next tutorial step
                    System.out.println("Moved to tutorial step: " + currentTutorialStep);
                } else {
                    isTutorialPhase = false; // End tutorial phase
                    inBattleMode = true; // Begin battle mode
                    tutorialTimer.cancel(); // Stop the timer
                    System.out.println("Transitioned to battle mode!");
                }
            }
        }, 0, 3000); // Change tutorial images every 3 seconds
    }



    public void endBattle() {
        if (!isBattleCompleted) {
            inBattleMode = false;
            isBattleCompleted = true; // Mark the battle as completed
            System.out.println("Battle mode ended. Tutorial phase will not restart.");
        }
    }

    private void teleportToBattlePositions() {
        System.out.println("Teleporting players to battle positions...");

        naruto.setPosition(200, 500); // Example position
        if (gojo != null) {
            gojo.setPosition(300, 500); // Example position
        }
        madara.setPosition(500, 200); // Example position

        activePlayer = naruto; // Ensure activePlayer is initialized
        System.out.println("Active player set to Naruto.");
        System.out.println("Naruto's Position: " + naruto.getX() + ", " + naruto.getY());
        if (gojo != null) {
            System.out.println("Gojo's Position: " + gojo.getX() + ", " + gojo.getY());
        }
        System.out.println("Madara's Position: " + madara.getX() + ", " + madara.getY());
    }



    public void drawHealthBars() {
        SaxionApp.setFill(Color.RED);
        int madaraHealthWidth = madara.getHealth() * 2;
        SaxionApp.drawRectangle(270, 78, madaraHealthWidth, 24);
        SaxionApp.setFill(Color.BLACK);
        SaxionApp.drawText("Madara Health: " + madara.getHealth(), 270, 80, 20);

        if (activePlayer != null) {
            int healthBarWidth = activePlayer.getHealth() * 2;
            int healthBarX = 160;
            int healthBarY = (activePlayer == naruto) ? 925 : 950;

            SaxionApp.setFill(Color.RED);
            SaxionApp.drawRectangle(healthBarX, healthBarY, healthBarWidth, 15);
            SaxionApp.setFill(Color.getHSBColor(0.16f, 0.3f, 1.0f)); // Light yellow for text
            SaxionApp.drawText(
                    (activePlayer == naruto ? "Naruto" : "Gojo") + " Health: " + activePlayer.getHealth(),
                    healthBarX, healthBarY - 15, 20
            );
        }
    }


    public void drawBattleField() {
        if (isTutorialPhase) {
            // Draw the current tutorial image
            if (currentTutorialStep < tutorialImages.length) {
                String currentImage = tutorialImages[currentTutorialStep];
                SaxionApp.drawImage(currentImage, 0, 0, 1000, 1000);
                System.out.println("Displaying tutorial image: " + currentImage);
            }
            return;
        }

        if (inBattleMode) {

            SaxionApp.drawImage(charactersChoiceImage, 150, 730, 300, 250);


            if (activePlayer == naruto) {
                SaxionApp.drawImage(narutoBattleSprite, 150, 470, 300, 300);
            } else if (activePlayer == gojo) {
                SaxionApp.drawImage(gojoBattleSprite, 150, 470, 300, 300);
            }


            SaxionApp.drawImage(madaraBattleSprite, 350, 230, 420, 420);
        } else {
            // Draw players and Madara in non-battle mode
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

        // Draw the action message if it's set
        if (actionMessage != null) {
            long elapsedTime = System.currentTimeMillis() - messageDisplayTime;
            if (elapsedTime < messageDuration) {
                // Draw message background
                SaxionApp.setFill(Color.LIGHT_GRAY);
                SaxionApp.drawRectangle(300, 20, 450, 40);

                // Draw the message text
                SaxionApp.setFill(Color.BLACK);
                SaxionApp.drawText(actionMessage, 320, 30, 20);
            } else {
                actionMessage = null; // Hide the message after the duration
            }
        }
    }


    public void handleCombat() {
        if (inBattleMode && playerTurn) {
            System.out.println("Player's turn: " + activePlayer.getName());

            // Show the action menu only during the player's turn
            displayActionMenu();
        } else {
            System.out.println("Madara's turn.");

            // Madara attacks only when it's his turn
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    // Make Madara perform an attack (regular, special, or ultimate) here
                    if (madara.getHealth() > 0) {
                        startMadaraAttackAnimation("normal"); // Adjust this to decide the attack type
                    }
                }
            }, 1000); // Delay before Madara attacks (1 second)
        }

        // Display health at the end of the turn
        System.out.println("Naruto Health: " + naruto.getHealth());
        if (gojo != null) {
            System.out.println("Gojo Health: " + gojo.getHealth());
        }
        System.out.println("Madara Health: " + madara.getHealth());
    }

    public void displayActionMenu() {
        if (inBattleMode && !isTutorialPhase && playerTurn) {
            // Draw the menu background
            SaxionApp.setFill(Color.LIGHT_GRAY);
            SaxionApp.drawRectangle(550, 600, 400, 270);

            // Display menu header
            SaxionApp.setFill(Color.BLACK);
            SaxionApp.drawText("What are you going to do?", 575, 650, 20);

            // Determine and draw Normal Attack
            if (System.currentTimeMillis() - narutoLastAttackTime >= narutoAttackCooldown) {
                SaxionApp.setFill(Color.getHSBColor(0.16f, 0.3f, 1.0f)); // Highlight background for available attack
                SaxionApp.drawRectangle(575, 698, 250, 30); // Rectangle around the text
                SaxionApp.setFill(Color.BLACK); // Text color
            } else {
                SaxionApp.setFill(Color.DARK_GRAY); // Unavailable attack
            }
            SaxionApp.drawText("1. Normal Attack (Click)", 575, 700, 20);

            // Determine and draw Special Attack
            if (narutoSpecialReady) {
                SaxionApp.setFill(Color.getHSBColor(0.16f, 0.3f, 1.0f)); // Highlight background for available special attack
                SaxionApp.drawRectangle(575, 748, 300, 30); // Rectangle around the text
                SaxionApp.setFill(Color.BLACK); // Text color
            } else {
                SaxionApp.setFill(Color.DARK_GRAY); // Unavailable attack
            }
            SaxionApp.drawText("2. Special Attack (Press 'E')", 575, 750, 20);

            // Determine and draw Ultimate Attack
            if (narutoUltimateReady) {
                SaxionApp.setFill(Color.getHSBColor(0.16f, 0.3f, 1.0f)); // Highlight background for available ultimate attack
                SaxionApp.drawRectangle(575, 798, 300, 30); // Rectangle around the text
                SaxionApp.setFill(Color.BLACK); // Text color
            } else {
                SaxionApp.setFill(Color.DARK_GRAY); // Unavailable attack
            }
            SaxionApp.drawText("3. Ultimate Attack (Press 'Q')", 575, 800, 20);
        }
    }



    private void displayActionMessage(String attacker, String attackType, String target, boolean isPlayer) {
        if (isPlayer) {
            // Player's turn
            actionMessage = "You " + " used " + capitalize(attackType) + " Attack!";
        } else {
            // Madara's turn
            actionMessage = "Madara used " + capitalize(attackType) + " Attack " + "!";
        }
        messageDisplayTime = System.currentTimeMillis();
    }


    public void handlePlayerAction(nl.saxion.app.interaction.MouseEvent mouseEvent, KeyboardEvent keyboardEvent) {
        if (playerTurn) {
            // Handle normal attack via left mouse button
            if (mouseEvent != null && mouseEvent.isLeftMouseButton() && mouseEvent.isMouseDown()) {
                startNarutoAttackAnimation("normal");
                madara.takeDamage(15); // Normal attack damage
                System.out.println("Player performed a normal attack. Madara takes 15 damage.");

                // Display action message
                displayActionMessage(activePlayer.getName(), "normal", "Madara", true);

                endPlayerTurn();
                return;
            }

            // Handle special attack via 'E' key
            if (keyboardEvent != null && keyboardEvent.getKeyCode() == KeyboardEvent.VK_E) {
                if (narutoSpecialReady || gojoUltimateCooldown) {
                    triggerSpecialAttack(); // Execute special attack

                    // Display action message
                    displayActionMessage(activePlayer.getName(), "special", "Madara", true);
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

                    // Display action message
                    displayActionMessage(activePlayer.getName(), "ultimate", "Madara", true);
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
            madara.takeDamage(15);
            narutoDamageCounter += 15;
            System.out.println("Normal attack dealt 15 damage to Madara.");
            playerTurn = false;
        } else {
            System.out.println("It's not the player's turn!");
        }
    }

    private void endPlayerTurn() {
        playerTurn = false;


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handleCombat();
            }
        }, 1000);
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

            // Determine start and end points for the projectile
            if (currentProjectileImage.equals(madaraNormalBall) || currentProjectileImage.equals(madaraSpecialBall) || currentProjectileImage.equals(madaraUltimateBall)) {
                startX = madara.getX();
                startY = madara.getY();
                endX = activePlayer.getX();
                endY = activePlayer.getY();
            } else {
                startX = naruto.getX();
                startY = naruto.getY() + 50; // Lower the starting Y position even more
                endX = madara.getX();
                endY = madara.getY() + 100; // Lower the ending Y position even more to aim further at Madara's lower body
            }

            // Calculate the current position of the projectile based on progress
            double progress = attackStep / 20.0;
            int currentX = (int) (startX + progress * (endX - startX));
            int currentY = (int) (startY + progress * (endY - startY));

            // Draw the projectile at the current position
            SaxionApp.drawImage(currentProjectileImage, currentX - 15, currentY - 15, 60, 60);
        }
    }

    private void startMadaraAttackCycle() {
        attackTimer = new Timer();
        attackTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Check if it's Madara's turn and if he's alive
                if (!playerTurn && madara.getHealth() > 0) {
                    String attackType = decideMadaraAttackType(); // Decide the attack type
                    startMadaraAttackAnimation(attackType);      // Start the chosen attack animation
                }
            }
        }, 0, 20000); // Adjust the interval as per your game's needs (e.g., every 20 seconds)
    }
    private String decideMadaraAttackType() {
        if (madara.getHealth() <= 75 && !ultimateUsedAt25) {
            ultimateUsedAt25 = true;
            return "ultimate"; // Use ultimate attack at 25% health if not used already
        } else if (madara.getHealth() <= 150 && !ultimateUsedAt50) {
            ultimateUsedAt50 = true;
            return "ultimate"; // Use ultimate attack at 50% health if not used already
        } else {
            // Randomize between normal and special attacks
            return Math.random() < 0.5 ? "normal" : "special";
        }
    }

    private void startMadaraAttackAnimation(String attackType) {
        isAnimatingAttack = true;
        attackStep = 0;

        // Set attack type to one of the Madara's attack types
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
                    madaraRegularAttack(); // Execute Madara's attack once the animation is done
                } else {
                    attackStep++;
                }
            }
        }, 0, 50); // Adjust the speed of the animation as necessary
    }

    private void madaraRegularAttack() {
        if (madara.getHealth() > 0) {
            if (activePlayer == null) {
                System.out.println("No active players remaining! Game Over.");
                playerTurn = true; // Prevent Madara from continuing.
                return;
            }

            String attackType = decideMadaraAttackType(); // Decide the attack type
            int madaraDamage = calculateMadaraDamage(attackType); // Calculate damage based on attack type

            activePlayer.takeDamage(madaraDamage);

            String targetName = activePlayer != null ? activePlayer.getName() : "Unknown";

            // Display Madara's action message
            displayActionMessage("Madara", attackType, targetName, false);

            System.out.println("Madara attacks " + targetName + " with " + attackType + " attack for " + madaraDamage + " damage!");

            if (activePlayer.getHealth() <= 0) {
                System.out.println(targetName + " has been defeated!");
                if (activePlayer == naruto && gojo != null) {
                    switchPlayer(2);
                } else if (activePlayer == gojo) {
                    System.out.println("All players defeated. Game Over!");
                    activePlayer = null; // Mark as no active player.
                    return;
                }
            }

            playerTurn = true;
        }
    }

    private int calculateMadaraDamage(String attackType) {
        switch (attackType) {
            case "normal":
                return 20;
            case "special":
                return 35;
            case "ultimate":
                return 70;
            default:
                return 0;
        }
    }

    private String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
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
        int madaraMaxHealth = 200;


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
            madara.takeDamage(50); // Special attack deals 50 damage
            narutoSpecialReady = false; // Reset readiness
            narutoDamageCounter = 0; // Reset damage counter for special
            System.out.println("Naruto used his special attack! Madara takes 50 damage.");
        } else {
            System.out.println("Special attack is not ready.");
        }
    }

    public void triggerUltimateAttack() {
        if (narutoUltimateReady && activePlayer == naruto) {
            startNarutoAttackAnimation("ultimate");
            madara.takeDamage(100); // Ultimate attack deals 100 damage
            narutoUltimateReady = false; // Reset readiness
            narutoDamageCounter = 0; // Reset damage counter for ultimate
            System.out.println("Naruto uses his ultimate attack! Madara takes 100 damage.");
        } else {
            System.out.println("Ultimate attack is not ready.");
        }
    }

    public void switchPlayer(int playerNumber) {
        if (playerNumber == 1 && naruto.getHealth() > 0) {
            activePlayer = naruto;
            System.out.println("Switched to Naruto.");
        } else if (playerNumber == 2 && gojo != null && gojo.getHealth() > 0) {
            activePlayer = gojo;
            System.out.println("Switched to Gojo.");
        } else {
            System.out.println("No valid player to switch to!");
            activePlayer = null; // This indicates no active players remaining.
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
