package invaders.bunker;

import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.io.File;

public class GreenState implements BunkerState {
    private double width;
    private double height;
    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    @Override
    public BunkerState getNextState() {
        return new YellowState();
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }

    public Image getImage() {
        return new Image(new File("src/main/resources/Space_invaders_character_Green.jpeg").toURI().toString(),this.width, this.height, false, true);
    }
}