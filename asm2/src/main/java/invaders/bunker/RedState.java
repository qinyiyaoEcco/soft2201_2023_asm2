package invaders.bunker;

import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.io.File;

public class RedState implements BunkerState {
    private double width;
    private double height;
    @Override
    public Color getColor() {
        return Color.RED;
    }

    @Override
    public BunkerState getNextState() {
        return this; // No further state after Red.
    }

    @Override
    public boolean isDestroyed() {
        return true;
    }

    public Image getImage() {
        return new Image(new File("src/main/resources/Space_invaders_character_Red.png").toURI().toString(),this.width, this.height, false, true);
    }
}