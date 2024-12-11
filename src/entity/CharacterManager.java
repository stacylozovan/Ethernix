public class CharacterManager {
        private Player player;
        private Madara madara;

        public CharacterManager(Player player, Madara madara) {
            this.player = player;
            this.madara = madara;
        }

        // Basic interaction: when the player and Madara are close enough, damage is exchanged
        public void handleCharacterInteractions() {
            // Check if Player and Madara are close enough to interact (simple proximity check)
            if (Math.abs(player.getX() - madara.getX()) < 50 && Math.abs(player.getY() - madara.getY()) < 50) {
                // Madara attacks the player (Player takes damage)
                player.takeDamage(10);  // Madara does 10 damage
            }
        }

        // Display health status (for debugging or later extension)
        public void displayHealthStatus() {
            System.out.println("Player Health: " + player.getHealth());
            System.out.println("Madara Health: " + madara.getHealth());
        }
    }