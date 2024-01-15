package invaders.bunker;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;


public interface BunkerState {
    Color getColor();
    BunkerState getNextState();
    boolean isDestroyed();
    Image getImage();
}




