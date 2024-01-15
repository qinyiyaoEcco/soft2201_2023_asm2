package invaders.entities;

import invaders.GameObject;
import javafx.scene.Node;
import invaders.rendering.Renderable;

public interface EntityView {
    void update(double xViewportOffset, double yViewportOffset);

    boolean matchesEntity(Renderable entity);


    Node getNode();

}
