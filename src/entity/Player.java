import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.SaxionApp;

public class Player extends entity.Entity {

    public Player(){
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 8;
        direction = "down";
    }

    public void getPlayerImage() {
            down1 = "src/res/player/naruto_down_1.png";
            down2 = "src/res/player/naruto_down_2.png";
            down3 = "src/res/player/naruto_down_3.png";
            down4 = "src/res/player/naruto_down_4.png";
            down5 = "src/res/player/naruto_down_5.png";
            down6 = "src/res/player/naruto_down_6.png";
            down7 = "src/res/player/naruto_down_7.png";
            down8 = "src/res/player/naruto_down_8.png";
            down9 = "src/res/player/naruto_down_9.png";
            down10 = "src/res/player/naruto_down_10.png";
            down11 = "src/res/player/naruto_down_11.png";
            down12 = "src/res/player/naruto_down_12.png";
            down13 = "src/res/player/naruto_down_13.png";
            down14 = "src/res/player/naruto_down_14.png";
            down15 = "src/res/player/naruto_down_15.png";
            down16 = "src/res/player/naruto_down_16.png";

            right1 = "src/res/player/naruto_right_1.png";
            left1 = "src/res/player/naruto_left_1.png";
    }

    public void update(boolean[] keys) {
        boolean keyPressed = false;

        if (keys[KeyboardEvent.VK_UP] || keys[KeyboardEvent.VK_W]) {
            direction = "up";
            this.y -= speed;
            keyPressed = true;
        }
        if (keys[KeyboardEvent.VK_DOWN] || keys[KeyboardEvent.VK_S]) {
            direction = "down";
            this.y += speed;
            keyPressed = true;
        }
        if (keys[KeyboardEvent.VK_LEFT] || keys[KeyboardEvent.VK_A]) {
            direction = "left";
            this.x -= speed;
            keyPressed = true;
        }
        if (keys[KeyboardEvent.VK_RIGHT] || keys[KeyboardEvent.VK_D]) {
            direction = "right";
            this.x += speed;
            keyPressed = true;
        }

        if (keyPressed) {
            spriteCounter++;
            if(spriteCounter < 16){
                spriteNum++;
            } else {
                spriteCounter = 0;
                spriteNum = 1;
            }
        }
    }

    public void draw() {
        String image = "";
        switch (direction) {
            case "up":
                image = down1;
                break;
            case "down":
                image = setImageDown(spriteNum);
                break;
            case "left":
                image = left1;
                break;
            case "right":
                image = right1;
                break;
        }
        SaxionApp.drawImage(image, x, y, 64, 64);
    }

    private String setImageDown(int spriteNum) {
        switch (spriteNum) {
            case 1:
                return down1;
            case 2:
                return down2;
            case 3:
                return down3;
            case 4:
                return down4;
            case 5:
                return down5;
            case 6:
                return down6;
            case 7:
                return down7;
            case 8:
                return down8;
            case 9:
                return down9;
            case 10:
                return down10;
            case 11:
                return down11;
            case 12:
                return down12;
            case 13:
                return down13;
            case 14:
                return down14;
            case 15:
                return down15;
            case 16:
                return down16;
            default:
                return "Invalid sprite number";
        }
    }
}
