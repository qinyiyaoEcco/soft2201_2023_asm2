package invaders.Projectil;
import invaders.physics.Vector2D;

// 具体工厂2
public class FastStraightProjectileFactory implements ProjectileFactory {
    @Override
    public Projectile createProjectile(Vector2D position, Vector2D direction) {
        return new FastStraightProjectile(position, direction);
    }
}