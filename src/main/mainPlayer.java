import nl.saxion.app.SaxionApp;

public class mainPlayer {
    int x;
    int y;
    String character; // For now, the player's character is just a string (e.g., "Player")
    String imagePath; // Path to the player's image

    // Constructor to initialize player with name, position, and image path
    public mainPlayer(String characterName, int startX, int startY, String imagePath) {
        this.character = characterName; // Set character name
        this.x = startX;   // Set starting X position
        this.y = startY;   // Set starting Y position
        this.imagePath = imagePath; // Set image path
    }

    // Draw method to display the player with resizing
    public void draw() {
        // Assuming SaxionApp has a method to load and draw images with scaling options
        SaxionApp.drawImage(imagePath, x, y, 70, 70); // Draw the player image at the position scaled to 32px by 32px
    }

    // Method to move the player (move by delta x and delta y)
    public void move(int dx, int dy) {
        this.x += dx;  // Move the player on the X axis
        this.y += dy;  // Move the player on the Y axis
    }
}
