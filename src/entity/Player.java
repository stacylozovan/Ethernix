import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.SaxionApp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends entity.Entity {

    public Player(){
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 10;
        direction = "down";
    }

    public void getPlayerImage() {
            down1 = "src/res/player/naruto_down_1.png";
//            down2 = "/player/naruto_down_2.png";
//            down3 = "/player/naruto_down_3.png";
//            down4 = "/player/naruto_down_4.png";
//            down5 = "/player/naruto_down_5.png";
//            down6 = "/player/naruto_down_6.png";
//            down7 = "/player/naruto_down_7.png";
//            down8 = "/player/naruto_down_8.png";
//            down9 = "/player/naruto_down_9.png";
//            down10 = "/player/naruto_down_10.png";
//            down11 = "/player/naruto_down_11.png";
//            down12 = "/player/naruto_down_12.png";
//            down13 = "/player/naruto_down_13.png";
//            down14 = "/player/naruto_down_14.png";
//            down15 = "/player/naruto_down_15.png";
//            down16 = "/player/naruto_down_16.png";
            right1 = "src/res/player/naruto_right_1.png";
            left1 = "src/res/player/naruto_left_1.png";
    }

    public void update(boolean[] keys) {
        if (keys[KeyboardEvent.VK_UP] || keys[KeyboardEvent.VK_W]) {
            direction = "up";
            this.y -= speed;
        }
        if (keys[KeyboardEvent.VK_DOWN] || keys[KeyboardEvent.VK_S]) {
            direction = "down";
            this.y += speed;
        }
        if (keys[KeyboardEvent.VK_LEFT] || keys[KeyboardEvent.VK_A]) {
            direction = "left";
            this.x -= speed;
        }
        if (keys[KeyboardEvent.VK_RIGHT] || keys[KeyboardEvent.VK_D]) {
            direction = "right";
            this.x += speed;
        }
    }

    public void draw() {
        String image = "";
        switch (direction) {
            case "up":
                image = down1;
                break;
            case "down":
                image = down1;
                break;
            case "left":
                image = left1;
                break;
            case "right":
                image = right1;
                break;
        }
        SaxionApp.drawImage(image, x, y, 36, 36);
    }
}

