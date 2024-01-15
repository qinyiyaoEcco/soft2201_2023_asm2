package invaders.enemy;
import invaders.physics.Vector2D;
import invaders.Projectil.Projectile;
import java.util.List;


public interface ShootStrategy {
    Projectile shoot(Vector2D position, List<Projectile> projectiles);
}
