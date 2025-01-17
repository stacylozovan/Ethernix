package entity;
import nl.saxion.app.SaxionApp;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TriviaSystem {
    private final List<TriviaQuestion> questions;
    private final Set<Integer> askedQuestions;
    private TriviaQuestion currentQuestion;
    private boolean triviaActive = false;
    private int playerAttempts = 0;
    private static final int MAX_ATTEMPTS = 2;
    private int questionIndex = -1;
    private String message = "";
    private boolean showMessage = false;
    private long messageStartTime = 0;
    private static final int MESSAGE_DISPLAY_DURATION = 1000;
    ArrayList<String> correctMessages = new ArrayList<>();
    ArrayList<String> wrongMessages = new ArrayList<>();


    public TriviaSystem() {
        questions = new ArrayList<>();
        createTriviaQuestions();
        askedQuestions = new HashSet<>();

        correctMessages.add("CORRECT! Health gained +50");
        correctMessages.add("CORRECT! Stamina gained +30");
        correctMessages.add("CORRECT! New attack unlocked: Special attack!");
        correctMessages.add("CORRECT! Experience increased +15");
        correctMessages.add("CORRECT! Power boost +15");
        correctMessages.add("CORRECT! Craig´s optimism gained");

        wrongMessages.add("WRONG! Health lost -35");
        wrongMessages.add("WRONG! Stamina lost -15");
        wrongMessages.add("WRONG! Attack locked: Special attack!");
        wrongMessages.add("WRONG! Experience decreased -20");
        wrongMessages.add("WRONG! Power drain -15");
        wrongMessages.add("WRONG! Gained Peter´s wooclap curse");
        // THIS IS HERE SO ITS ONLY CREATED ONCE, AND IT WONT REPEAT AGAIN AND AGAIN.
    }
    private void createTriviaQuestions() {
        questions.add(new TriviaQuestion("What is the name of the legendary sword used by Guts in Berserk?",
                new String[]{"Dragon Slayer", "Excalibur", "Soul Eater"}, 1));
        questions.add(new TriviaQuestion("What is the primary mission of the Survey Corps?",
                new String[]{"Defend Marley", "Explore the world beyond the walls", "Hunt down Saxion workers"}, 2));
        questions.add(new TriviaQuestion("Who is the author of 'One Piece'?",
                new String[]{"Masashi Kishimoto", "Eiichiro Oda", "Tite Kubo"}, 2));
        questions.add(new TriviaQuestion("Which character is known as the 'Pirate King' in One Piece?",
                new String[]{"Gol D. Roger", "Monkey D. Luffy", "Shanks"}, 1));
        questions.add(new TriviaQuestion("What is Goku’s signature attack?",
                new String[]{"Kamehameha", "Final Flash", "Bankai"}, 1));
        questions.add(new TriviaQuestion("Yoriichi is the creator of which breathing style in Demon Slayer?",
                new String[]{"Water Breathing", "Patrick's Breathing", "Sun Breathing"}, 3));
    }

    public void startTrivia() {
        if (askedQuestions.size() == questions.size()) {
            triviaActive = false; // EVERYTHING HAS BEEEEEN ASKED
            return;
        }
        triviaActive = true;
        playerAttempts = 0;

        message = "";
        showMessage = false;

        // Select a random question that hasn't been asked
        do {
            questionIndex = new Random().nextInt(questions.size());
        } while (askedQuestions.contains(questionIndex));

        askedQuestions.add(questionIndex);
        currentQuestion = questions.get(questionIndex);
    }


    public void drawTriviaScreen() {
        if (!triviaActive  && !showMessage) return;

        SaxionApp.setTextDrawingColor(Color.white);
        SaxionApp.setFill(Color.black);
        SaxionApp.drawRectangle(250, 250, 500, 500);

        SaxionApp.setTextDrawingColor(Color.white);
        SaxionApp.drawText("Trivia Time!", 425, 285, 30);

        if (triviaActive) {
            drawWrappedText(currentQuestion.getQuestion(), 270, 350, 460, 40);
            String[] options = currentQuestion.getOptions();
            for (int i = 0; i < options.length; i++) {
                SaxionApp.drawText((i + 1) + ": " + options[i], 275, 450 + (i * 50), 20);
            }
            SaxionApp.drawText("Press 1, 2, or 3 to answer.", 275, 625, 20);
        }

        if (showMessage) {
            if (message.contains("WRONG!")) {
                SaxionApp.setTextDrawingColor(Color.red);
            } else {
                SaxionApp.setTextDrawingColor(Color.green);
            }
            drawWrappedText(message, 275, 500, 850,40);

            if (System.currentTimeMillis() - messageStartTime > MESSAGE_DISPLAY_DURATION) {
                showMessage = false;
                if (triviaActive) {
                    startTrivia();
                }
            }
        }

    }

    private void drawWrappedText(String text, int x, int y, int maxWidth, int lineHeight) {
        // so the text wont go outside the border
        int charLimitPerLine = maxWidth / 10;
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int currentY = y;

        for (String word : words) {
            if (line.length() + word.length() + 1 > charLimitPerLine) {
                SaxionApp.drawText(line.toString(), x, currentY, 20);
                line = new StringBuilder(word + " ");
                currentY += lineHeight;
            } else {
                line.append(word).append(" ");
            }
        }

        if (line.length() > 0) {
            SaxionApp.drawText(line.toString(), x, currentY, 20);
        }
    }
    public void handleAnswer(int choice) {
        if (!triviaActive) return;

        if (choice == currentQuestion.getCorrectAnswer()) {
            int index = new Random().nextInt(correctMessages.size());
            message = correctMessages.get(index);
            correctMessages.remove(index);
            triviaActive = false;

        } else {
            playerAttempts++;
            int index = new Random().nextInt(wrongMessages.size());
            message = wrongMessages.get(index);
            wrongMessages.remove(index);
            triviaActive = false;

        }

        // Set up the message display
        showMessage = true;
        messageStartTime = System.currentTimeMillis();
    }


    public boolean isTriviaActive() {
        return triviaActive;
    }

    public boolean isShowingMessage() {
        return showMessage;
    }
    public void startTrivia(int questionIndex) {
        // find the index of the question to ask the correct specific question
        if (questionIndex < 0 || questionIndex >= questions.size() || askedQuestions.contains(questionIndex)) {
            triviaActive = false;
            return;
        }

        triviaActive = true;
        playerAttempts = 0;

        message = "";
        showMessage = false;

        askedQuestions.add(questionIndex);
        currentQuestion = questions.get(questionIndex);
    }

}
