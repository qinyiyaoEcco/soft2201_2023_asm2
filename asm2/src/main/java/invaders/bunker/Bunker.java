package invaders.bunker;

import invaders.GameObject;
import invaders.logic.Damagable;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.paint.Color;
import invaders.physics.BoxCollider;
import javafx.scene.image.Image;
import java.io.File;
public class Bunker implements Damagable, Renderable, GameObject {
    private int health = 3;  // 初始化时有3点健康值
    private BoxCollider collider;
    private BunkerState state;
    private Vector2D position;
    private Image image;
    private boolean alive;
    private double width;
    private double height;

    private Bunker(BunkerBuilder builder) {
        this.width = builder.width;
        this.height = builder.height;
        this.position = builder.position;
        this.collider = builder.collider;
        this.alive = true;
        this.image = new Image(new File(builder.imagePath).toURI().toString(), this.width, this.height, false, true);
        this.state = new GreenState();
    }
    @Override
    public void takeDamage(double amount) {
        health--;
        if (health > 0) {
            state = state.getNextState(); // 改变状态
        } else {
            this.alive = false;
        }
    }


    public BoxCollider getCollider() {
        return this.collider;
    }
    @Override
    public double getHealth() {
        return health;
    }
    @Override
    public boolean isAlive() {
        return health > 0;
    }

    public Color getColor() {
        return state.getColor();
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        state.getImage();

    }

    public Image getImage() {
        return state.getImage();

    }

    @Override
    public double getWidth() { return width;}

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }
    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    public static class BunkerBuilder {
        private double width;
        private double height;
        private Vector2D position;
        private String imagePath;
        private BoxCollider collider;

        public BunkerBuilder setSize(double width, double height) {
            this.width = width;
            this.height = height;
            return this;
        }
        public BunkerBuilder setImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public BunkerBuilder setPosition(Vector2D position) {
            this.position = position;
            return this;
        }

        public BunkerBuilder setCollider(BoxCollider collider) {
            this.collider = collider;
            return this;
        }
        public Bunker build() {
            return new Bunker(this);
        }
        
    }
}
