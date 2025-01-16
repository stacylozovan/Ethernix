import entity.AudioHelper;
import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.MouseEvent;
import java.awt.Color;
import nl.saxion.app.interaction.KeyboardEvent;

public class MainMenu {
    private boolean inMenu = true; // Tracks if we are in the menu
    private boolean gameStarted = false; // tracks if the game has started or not
    private boolean inSettings = false; // tracks the settings menu
    private AudioHelper audioHelper;


    public void drawMainMenu() {
            if (!AudioHelper.isPlaying() || !AudioHelper.getFilename().equals("src/res/audio/background_music.wav")) {
                AudioHelper.newSong("src/res/audio/background_music.wav", false);
                }

            SaxionApp.clear();
            SaxionApp.drawImage("src/res/menu/background_5.jpg",0,0,1000,1000);
            SaxionApp.setFill(Color.BLACK);
            SaxionApp.drawImage("src/res/menu/ethernix9.png",375,235,300,60);
            SaxionApp.drawImage("src/res/menu/play.png", 405, 335, 241, 133);
            SaxionApp.drawImage("src/res/menu/tipsFinal.png", 443, 495, 160, 65);
            SaxionApp.drawImage("src/res/menu/quit.png", 410, 595, 246, 138);
    }

    public boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
            // this method basically checks whether or not we clicked a button
    }

    public boolean mouseEvent(MouseEvent mouseEvent) {
        // this method checks what we clicked, play, settings or quit button.
        if (mouseEvent.isMouseDown() && mouseEvent.isLeftMouseButton()) {
            int mouseX = mouseEvent.getX();
            int mouseY = mouseEvent.getY();

            if (inMenu) {
                if (isMouseOver(mouseX, mouseY, 440, 367, 157, 65)) {
                    return true;
                }
                else if (isMouseOver(mouseX, mouseY, 440, 491, 160, 65)) {
                    inSettings = true;
                    return false;
                }
                else if (isMouseOver(mouseX, mouseY, 441, 630, 155, 65)) {
                    SaxionApp.quit();
                }
            }
        }
        return false;
    }

    public boolean isInSettings() {
        return inSettings; // checks if we are in the settings menu
    }

    public void exitSettings() {
        inSettings = false; // Go back to the main menu
    }

    public boolean handlingKeyboardEscapeButton(KeyboardEvent keyboardEvent) {
        // If mr patrick clicks the ESC key, he returns to the main menu from settings or game
        if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_ESCAPE) {
            if (isInSettings()) {
                exitSettings();
                return true;
            } else if (!gameStarted) {
                inMenu = true;
                return true;
            }
        }
        return false;
    }
}
