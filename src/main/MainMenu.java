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

        public void mouseEvent(MouseEvent mouseEvent) {
            if (mouseEvent.isMouseDown() && mouseEvent.isLeftMouseButton()) {
                int mouseX = mouseEvent.getX();
                int mouseY = mouseEvent.getY();

                if (inMenu) {
                    // Check if "Start Game" button is clicked
                    if (isMouseOver(mouseX, mouseY, 350, 350, 200, 50)) {
                        inMenu = false;
                        gameStarted = true;
                    }
                    else if (isMouseOver(mouseX, mouseY, 350, 450, 200, 50)) {
                        inMenu = false;
                        gameStarted = true;
                        SaxionApp.drawText("Settings", 350,550,50);
                    }
                    else if (isMouseOver(mouseX, mouseY, 350, 550, 200, 50)) {
                        SaxionApp.quit();
                    }
                }
            }
        }

        public boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }

    }
