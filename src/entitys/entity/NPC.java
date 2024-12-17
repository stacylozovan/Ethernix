package entity;

import nl.saxion.app.SaxionApp;

public class NPC extends Entity {
    public String name;
    public boolean isQuestGiver;
    public boolean isVisible;
    public String[] dialogue;
    public String movementPattern;

    public NPC(String name, int x, int y,  String[] dialogue, String direction, String movementPattern ) {
        this.name = name;
        this.dialogue = dialogue;
        this.movementPattern = "static";
        this.isVisible = true;
        this.isQuestGiver = false;
        this.x = x;
        this.y = y;
        this.width = 64;
        this.height = 64;
        if (movementPattern.equals("static")) {
            this.direction = "down";
        } else {
            this.direction = direction;
        }

        if (isVisible && movementPattern.equals("static")) {
            setDefaultStaticAsset();
        }
    }

    private void setDefaultStaticAsset() {
        this.down0 = String.format("src/res/npcs/%s/%s_1.png", this.name, this.name);
    }

    public void interact() {
        if (dialogue != null && dialogue.length > 0) {
            System.out.println(name + ": " + dialogue[0]);
            SaxionApp.drawText(name + ": " + dialogue[0], 100, 100, 20);
        }
    }

    public void draw(int screenX, int screenY){
        SaxionApp.drawImage(this.down0, screenX, screenY, this.width, this.height);
    }
}
