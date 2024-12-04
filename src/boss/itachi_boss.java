import nl.saxion.app.SaxionApp;

public class itachi_boss {
    private String name;
    private int x, y;
    private String imagePath; // Path to the boss image
    private String image; // Actual image path

    // Constructor to initialize the boss
    public itachi_boss(String name, int x, int y, String imagePath) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.imagePath = imagePath;
        this.image = imagePath; // Store the image path
    }

    // Draw method to display the boss
    public void draw() {
        SaxionApp.drawImage(image, x, y, 70, 70); // Draw the image at the boss's x, y position, scaled to 32px by 32px
    }

    public void aiBehavior(mainPlayer player) {
        // Boss follows the player, but only moves slightly in each iteration
        int movementSpeed = 1; // Reduced movement speed

        if (player.x > this.x) {
            this.x += movementSpeed; // Boss moves slowly right towards the player
        } else if (player.x < this.x) {
            this.x -= movementSpeed; // Boss moves slowly left towards the player
        }

        if (player.y > this.y) {
            this.y += movementSpeed; // Boss moves slowly down towards the player
        } else if (player.y < this.y) {
            this.y -= movementSpeed; // Boss moves slowly up towards the player
        }
    }
}
