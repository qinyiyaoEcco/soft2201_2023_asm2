package invaders.Projectil;

import invaders.physics.BoxCollider;
import invaders.physics.Vector2D;

// 具体产品1
public class FastStraightProjectile extends Projectile {
    private static final double SPEED = 6.0;
    public FastStraightProjectile(Vector2D position, Vector2D direction) {
        super(position, direction);


    }
    public void update() {

        position.setX(position.getX() + direction.getX() * SPEED);
        position.setY(position.getY() + direction.getY() * SPEED);
        collider = new BoxCollider(width, height, position);

    }
}