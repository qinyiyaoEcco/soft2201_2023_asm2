package invaders.Projectil;
import invaders.physics.BoxCollider;
import invaders.GameObject;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;

import javafx.scene.image.Image;
import java.io.File;
public abstract class Projectile implements Renderable, GameObject {

    protected Vector2D position;
    protected Vector2D direction;
    protected Image image;

    protected final double width = 10;
    protected final double height = 10;
    protected double SPEED;
    protected BoxCollider collider;
    Object shooter; // 子弹的发射者

    public Object getShooter() {
        return shooter;
    }

    public void setShooter(Object shooter) {
        this.shooter = shooter;
    }



    // 构造函数
    public Projectile(Vector2D position, Vector2D direction) {

        this.position = position;
        this.direction = direction;
        this.collider = new BoxCollider(width, height, position);
        this.image = new Image(new File("src/main/resources/clipart1216920.png").toURI().toString(), width, height, false, true);
    }


    public void start() {
    }

    public abstract void update();



    // 获取位置
    public Vector2D getPosition() {
        return position;
    }

    // 获取方向
    public Vector2D getDirection() {
        return direction;
    }

    // 设置方向
    public void setDirection(Vector2D direction) {
        this.direction = direction;
    }

    public BoxCollider getCollider() {
        return collider;
    }



    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public double getWidth() {return width;}

    @Override
    public double getHeight() {return height;}

    @Override
    public Renderable.Layer getLayer() {
        return Renderable.Layer.FOREGROUND;
    }

}

