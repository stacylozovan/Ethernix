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
    private int cutsceneStep = 0; // Tracks the step of the cutscene
    private long cutsceneStartTime = 0; // Tracks the start time of each dialogue
    private String currentSpeaker = ""; // Speaker for current dialogue
    private String currentDialogue = ""; // Current dialogue text
    private boolean cutsceneActive = false; // Whether the cutscene is active

    private int tutorialStep = 0; // Tracks the step of the tutorial
    private boolean tutorialActive = false; // Whether the tutorial is active

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
        SaxionApp.drawRectangle(275, 70, madaraHealthWidth, 20);
        SaxionApp.setFill(Color.BLACK);
        SaxionApp.drawText("Madara Health: " + madara.getHealth(), 275, 70, 20);

        if (activePlayer != null) {
            int healthBarWidth = activePlayer.getHealth() * 2;
            int healthBarX = 160;
            int healthBarY = (activePlayer == naruto) ? 925 : 950;

            SaxionApp.setFill(Color.RED);
            SaxionApp.drawRectangle(healthBarX, healthBarY, healthBarWidth, 10);
            SaxionApp.setFill(Color.BLACK);
            SaxionApp.drawText(
                    (activePlayer == naruto ? "Naruto" : "Gojo") + " Health: " + activePlayer.getHealth(),
                    healthBarX, healthBarY - 12, 12
            );
        }
    }
    public void startBattleCutscene() {
        cutsceneStep = 0;
        cutsceneActive = true;
        tutorialActive = false; // Ensure tutorial isn't active
        inBattleMode = false;   // Ensure battle isn't active
        cutsceneStartTime = System.currentTimeMillis();
        showNextCutsceneDialogue();
    }

    private void showNextCutsceneDialogue() {
        String[][] cutsceneDialogues = {
                {"madara", "So, you've finally arrived, Naruto."},
                {"madara", "This multiverse bends to my will. You're just a fragment of chaos."},
                {"naruto", "Madara! We’re here to stop you and free everyone!"},
                {"madara", "Fools. Do you truly believe you can defeat me?"},
                {"gojo", "Defeat you? Oh, we’re way past that. It’s your turn to fear us, old man!"},
                {"madara", "Then face me, and witness true despair!"}
        };

        if (cutsceneStep < cutsceneDialogues.length) {
            currentSpeaker = cutsceneDialogues[cutsceneStep][0];
            currentDialogue = cutsceneDialogues[cutsceneStep][1];
            cutsceneStartTime = System.currentTimeMillis();
            cutsceneStep++;
        } else {
            cutsceneActive = false; // End cutscene
            startTutorial();       // Start tutorial after cutscene ends
        }
    }
    public void startTutorial() {
        tutorialStep = 0;
        tutorialActive = true;
        cutsceneActive = false;  // Ensure cutscene is deactivated
        inBattleMode = false;    // Ensure battle isn't active
        System.out.println("Tutorial started.");
    }


    public void endTutorialAndStartBattle() {
        tutorialActive = false;  // End tutorial
        inBattleMode = true;     // Transition to battle mode
        System.out.println("Tutorial ended. Battle started!");
    }


    public void drawCutscene() {
        if (cutsceneActive) {
            SaxionApp.setFill(Color.BLACK);
            SaxionApp.drawRectangle(50, 700, 900, 150);
            SaxionApp.setFill(Color.WHITE);
            SaxionApp.drawText(currentSpeaker + ": " + currentDialogue, 70, 750, 20);

            // Progress to the next dialogue after 2 seconds
            if (System.currentTimeMillis() - cutsceneStartTime > 2000) {
                showNextCutsceneDialogue();
            }
        }
    }

    public void drawTutorial() {
        if (!tutorialActive) return;

        String[] tutorialSteps = {
                "Click the left mouse button to perform a Normal Attack.",
                "Press 'E' to perform a Special Attack. Build points by using Normal Attacks.",
                "Press 'Q' to perform an Ultimate Attack. Make sure it’s ready before using!",
                "Well done! You’re ready for the real battle!"
        };

        if (tutorialStep < tutorialSteps.length) {
            SaxionApp.setFill(Color.LIGHT_GRAY);
            SaxionApp.drawRectangle(50, 700, 900, 150);
            SaxionApp.setFill(Color.BLACK);
            SaxionApp.drawText(tutorialSteps[tutorialStep], 70, 750, 20);

            if (playerActionCompleted(tutorialStep)) {
                tutorialStep++;
            }
        } else {
            endTutorialAndStartBattle(); // Transition to battle after tutorial
        }
    }

    private boolean playerActionCompleted(int step) {
        // Simulate player actions (replace with real logic)
        return true; // For now, simulate success
    }
//    private void endTutorialAndStartBattle() {
//        tutorialActive = false;
//        inBattleMode = true; // Starts battle in CombatSystemLogic
//    }


    public boolean isCutsceneActive() {

        return cutsceneActive;
    }

    public boolean isTutorialActive() {

        return tutorialActive;
    }

    private void displayDialogue(String speaker, String dialogue) {
        // Draws dialogue box and speaker's text
        SaxionApp.setFill(Color.BLACK);
        SaxionApp.drawRectangle(50, 700, 900, 150);
        SaxionApp.setFill(Color.WHITE);
        SaxionApp.drawText(speaker + ": " + dialogue, 70, 750, 20);
    }

    private void waitForNextDialogue() {
        // Use Thread.sleep for a fixed delay or implement player interaction
        try {
            Thread.sleep(2000); // Wait for 2 seconds; adjust as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    private void displayTutorialStep(String message) {
        // Display a tutorial message
        SaxionApp.setFill(Color.LIGHT_GRAY);
        SaxionApp.drawRectangle(50, 700, 900, 150); // Tutorial dialogue box
        SaxionApp.setFill(Color.BLACK);
        SaxionApp.drawText(message, 70, 750, 20);
        try {
            Thread.sleep(2000); // Pause for player to read
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitForPlayerMovement() {
        // Replace this with actual logic to detect player movement
        System.out.println("Waiting for player movement...");
        while (!playerHasMoved()) {
            try {
                Thread.sleep(100); // Check every 100ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Player has moved!");
    }

    private void waitForPlayerAttack(String attackType) {
        // Replace this with actual logic to detect the specified attack
        System.out.println("Waiting for player to perform a " + attackType + " attack...");
        while (!playerPerformedAttack(attackType)) {
            try {
                Thread.sleep(100); // Check every 100ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Player performed " + attackType + " attack!");
    }

    // Mock methods for movement and attack detection
    private boolean playerHasMoved() {
        // Simulate movement detection logic here
        return true; // Placeholder
    }

    private boolean playerPerformedAttack(String attackType) {
        // Simulate specific attack detection logic here
        return true; // Placeholder
    }



    public void drawBattleField() {
        if (inBattleMode) {
            // Draw the character choice image
            SaxionApp.drawImage(charactersChoiceImage, 150, 730, 300, 250);

            // Draw the active player's battle sprite
            if (activePlayer == naruto) {
                SaxionApp.drawImage(narutoBattleSprite, 150, 470, 300, 300);
            } else if (activePlayer == gojo) {
                SaxionApp.drawImage(gojoBattleSprite, 150, 470, 300, 300);
            }

            // Draw Madara's battle sprite
            SaxionApp.drawImage(madaraBattleSprite, 350, 230, 400, 400);
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
                SaxionApp.setFill(Color.YELLOW);
                SaxionApp.drawRectangle(300, 20, 500, 50);

                // Draw the message text
                SaxionApp.setFill(Color.BLACK);
                SaxionApp.drawText(actionMessage, 320, 50, 20);
            } else {
                actionMessage = null; // Hide the message after the duration
            }
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

    private void displayActionMessage(String message) {
        actionMessage = message;
        messageDisplayTime = System.currentTimeMillis(); // Record the start time
    }

    public void handlePlayerAction(nl.saxion.app.interaction.MouseEvent mouseEvent, KeyboardEvent keyboardEvent) {
        if (playerTurn) {
            // Handle normal attack via left mouse button
            if (mouseEvent != null && mouseEvent.isLeftMouseButton() && mouseEvent.isMouseDown()) {
                startNarutoAttackAnimation("normal");
                madara.takeDamage(15); // Normal attack damage
                System.out.println("Player performed a normal attack. Madara takes 15 damage.");

                // Display action message
                displayActionMessage("You used Normal Attack!");

                endPlayerTurn();
                return;
            }

            // Handle special attack via 'E' key
            if (keyboardEvent != null && keyboardEvent.getKeyCode() == KeyboardEvent.VK_E) {
                if (narutoSpecialReady || gojoUltimateCooldown) {
                    triggerSpecialAttack(); // Execute special attack

                    // Display action message
                    displayActionMessage("You used Special Attack!");
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
                    displayActionMessage("You used Ultimate Attack!");
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
            if (activePlayer == null) {
                System.out.println("Error: Active player is null!");
                return;
            }

            int madaraDamage = 20; // Fixed damage for Madara
            activePlayer.takeDamage(madaraDamage);

            // Display action message
            displayActionMessage("Madara used Normal Attack!");

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
