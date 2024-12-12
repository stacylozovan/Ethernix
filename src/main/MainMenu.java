import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.MouseEvent;
import java.awt.Color;

public class MainMenu {
    private boolean inMenu = true; // Tracks if we are in the menu
    private boolean gameStarted = false;

        public void drawMainMenu() {
            SaxionApp.clear();
            SaxionApp.setFill(Color.BLACK);
            SaxionApp.drawImage("src/res/menu/menu_background.png",230,190,500,500);
            SaxionApp.drawImage("src/res/menu/play.png", 400, 350, 140, 56);
            SaxionApp.drawImage("src/res/menu/options.png", 400, 450, 140, 56);
            SaxionApp.drawImage("src/res/menu/quit.png", 400, 550, 140, 56);
        }



    }
