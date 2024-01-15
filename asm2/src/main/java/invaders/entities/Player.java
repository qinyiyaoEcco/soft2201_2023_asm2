package invaders.entities;

import invaders.GameObject;
import invaders.logic.Damagable;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Animator;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;
import java.io.File;

import invaders.Projectil.Projectile;
import invaders.Projectil.SlowStraightProjectile;
import invaders.physics.BoxCollider;
import org.json.simple.JSONObject;
public class Player implements GameObject,Moveable, Damagable, Renderable {

    private Vector2D position;
    private final Animator anim = null;
    private double health;
    private String colour;
    private double speed;
    private final double width = 25;
    private final double height = 30;
    private final Image image;
    private Projectile projectile = null;
    private BoxCollider collider;

    public Player(String configFilePath){
        JSONObject config = ConfigReader.readConfig(configFilePath);
        if (config != null) {
            JSONObject playerConfig = (JSONObject) config.get("Player");
            this.colour = (String) playerConfig.get("colour");
            this.speed = (Long) playerConfig.get("speed");
            this.health = (Long) playerConfig.get("lives");

            JSONObject positionConfig = (JSONObject) playerConfig.get("position");
            double x = (Long) positionConfig.get("x");
            double y = (Long) positionConfig.get("y");
            this.position = new Vector2D(x, y);
        }
        this.image = new Image(new File("src/main/resources/player.png").toURI().toString(), width, height, false, true);
        this.collider = new BoxCollider(width, height, position);
    }

    public BoxCollider getCollider() {
        return this.collider;
    }

    @Override
    public void takeDamage(double amount) {
        this.health -= amount;
    }

    @Override
    public double getHealth() {
        return this.health;
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    @Override
    public void up() {
        return;
    }

    @Override
    public void down() {
        return;
    }

    @Override
    public void left() {
        this.position.setX(this.position.getX() - 1);
        this.collider.getPosition().setX(this.position.getX());
    }

    @Override
    public void right() {
        this.position.setX(this.position.getX() + 1);
        this.collider.getPosition().setX(this.position.getX());
    }

    public Projectile shoot(){
        // todo
        if (projectile == null) {
            Vector2D direction = new Vector2D(0, -1);
            projectile = new SlowStraightProjectile(new Vector2D(position.getX(), position.getY()), direction);
            projectile.setShooter(this);
            return projectile;
        }

        return null;
    }
    public void clearProjectile() {
        this.projectile = null;
    }



    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return width;
    }

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

    @Override
    public void start() {

    }

    @Override
    public void update() {

    }
}
