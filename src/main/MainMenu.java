import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.MouseEvent;
import java.awt.Color;
import nl.saxion.app.interaction.KeyboardEvent;

public class MainMenu {
    private boolean inMenu = true; // Tracks if we are in the menu
    private boolean gameStarted = false; // tracks if the game has started or not
    private boolean inSettings = false; // tracks the settings menu


    public void drawMainMenu() {
            SaxionApp.clear();
            SaxionApp.setFill(Color.BLACK);
            SaxionApp.drawImage("src/res/menu/ethernix.png",300,190,343,155);
            SaxionApp.drawImage("src/res/menu/play.png", 365, 335, 241, 133);
            SaxionApp.drawImage("src/res/menu/settings.png", 340, 465, 296, 122);
            SaxionApp.drawImage("src/res/menu/quit.png", 370, 595, 246, 138);
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
                if (isMouseOver(mouseX, mouseY, 400, 367, 157, 65)) {
                    return true;
                }
                else if (isMouseOver(mouseX, mouseY, 376, 491, 207, 65)) {
                    inSettings = true;
                    return false;
                }
                else if (isMouseOver(mouseX, mouseY, 401, 630, 155, 65)) {
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
        // If ESC key is pressed, return to the main menu from settings or game
        if (keyboardEvent.getKeyCode() == KeyboardEvent.VK_ESCAPE) {
            if (isInSettings()) {
                // Exit settings and go back to the main menu
                exitSettings();
                return true;
            } else if (!gameStarted) {
                // Go back to the main menu from the Play menu
                inMenu = true;
                return true;
            }
        }
        return false;
    }
}
