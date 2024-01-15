package invaders.enemy;
import invaders.GameObject;
import invaders.physics.Moveable;
import invaders.rendering.Renderable;
import invaders.physics.Vector2D;
import invaders.physics.BoxCollider;
import invaders.Projectil.Projectile;
import javafx.scene.image.Image;
import java.util.*;
import java.io.File;



public class Enemy implements Moveable, Renderable, GameObject {
    private static final List<Projectile> projectiles = new ArrayList<>();
    private static long lastShootTime = System.currentTimeMillis();
    private Vector2D position;
    private BoxCollider collider;
    private final Image image;
    private final double width = 25;
    private final double height = 30;

    private boolean alive;

    private long startTime;

    private Vector2D direction = new Vector2D(1, 0);  // 初始方向可以设置为向右
    private static final double speed = 0.5;  // 基础速度  速度作为静态字段：所有的Enemy对象共享这个字段，当其值改变时，所有的Enemy实例都会受到影响。
    private static double speedIncrement = 0.0;   // 速度增量，开始时为0

    private final ShootStrategy shootStrategy;



    private Enemy(EnemyBuilder builder) {
        this.position = builder.position;
        this.collider = builder.collider;
        this.shootStrategy = builder.shootStrategy;
        this.alive = true;
        this.image = new Image(new File(builder.imagePath).toURI().toString(), width, height, false, true);
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public boolean isAlive() {
        return this.alive;
    }


    public void up() {
        return;
    }

    @Override
    public void down() {
        this.position.setY(this.position.getY() + 30);
    }

    @Override
    public void left() {
        this.position.setX(this.position.getX() - 1);
    }

    @Override
    public void right() {
        this.position.setX(this.position.getX() + 1);
    }

    public Image getImage() {
        return this.image;
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

    // 获取玩家在渲染层中的位置（前景）
    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    public BoxCollider getCollider() {
        return this.collider;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        // ... 其他初始化逻辑
    }

    private void move() {
        position.setX(position.getX() + direction.getX() * getCurrentSpeed());
        position.setY(position.getY() + direction.getY() * getCurrentSpeed());
        collider.setPosition(position);
    }

    public static void increaseEnemySpeed() {
        speedIncrement += 0.3;
    }
    public static void removeProjectile(Projectile projectile) {
        projectiles.remove(projectile);
    }

    public Projectile shoot() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - Enemy.lastShootTime >= randomShootInterval() && Enemy.projectiles.size() < 3) {
            Projectile projectile = shootStrategy.shoot(new Vector2D(position), Enemy.projectiles);
            if (projectile != null) {
                projectile.setShooter(this);
            }
            Enemy.lastShootTime = currentTime;
            return projectile;
        }
        return null;
    }


    public void update() {
        move();
    }
    private long randomShootInterval() {
        Random random = new Random();
        return 1000 + random.nextInt(2000);
    }

    public void reverseDirection() {

        this.direction.setX(-this.direction.getX());
    }

    public double getCurrentSpeed() {
        return speed + speedIncrement;  // 返回基础速度加上增量
    }



    public static class EnemyBuilder {
            private Vector2D position;
            private BoxCollider collider;
            private String imagePath;
            private ShootStrategy shootStrategy;
            public EnemyBuilder setProjectileType(String projectileType) {
                switch (projectileType) {
                    case "slow_straight":
                        this.shootStrategy = new SlowProjectileStrategy();
                        break;
                    case "fast_straight":
                        this.shootStrategy = new FastProjectileStrategy();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid projectileType: " + projectileType);

                }
                return this;
            }

            public EnemyBuilder setImagePath(String imagePath) {
                this.imagePath = imagePath;
                return this;
            }

            public EnemyBuilder setPosition(Vector2D position) {
                this.position = position;
                return this;
            }

            public EnemyBuilder setCollider(BoxCollider collider) {
                this.collider = collider;
                return this;
            }

            public Enemy build() {
                return new Enemy(this);
            }


        }


    }

