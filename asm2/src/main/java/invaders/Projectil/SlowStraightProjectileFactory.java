package invaders.Projectil;
import invaders.physics.Vector2D;

public class SlowStraightProjectileFactory implements ProjectileFactory {
    @Override
    public Projectile createProjectile(Vector2D position, Vector2D direction) {
        return new SlowStraightProjectile(position, direction);
    }
}