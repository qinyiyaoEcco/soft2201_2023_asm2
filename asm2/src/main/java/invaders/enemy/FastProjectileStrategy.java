package invaders.enemy;
import invaders.Projectil.FastStraightProjectileFactory;
import invaders.Projectil.Projectile;
import invaders.physics.Vector2D;
import java.util.List;
import invaders.Projectil.ProjectileFactory;


public class FastProjectileStrategy implements ShootStrategy {
    private final ProjectileFactory factory;

    public FastProjectileStrategy() {
        this.factory = new FastStraightProjectileFactory();
    }

    @Override
    public Projectile shoot(Vector2D position, List<Projectile> projectiles) {
        if (projectiles.size() < 3) {
            Vector2D direction = new Vector2D(0, 1); // 或者您可以根据需求自定义方向
            Projectile projectile=factory.createProjectile(position, direction);
            projectiles.add(projectile);
            return projectile;
        }
        return null;
    }
}
