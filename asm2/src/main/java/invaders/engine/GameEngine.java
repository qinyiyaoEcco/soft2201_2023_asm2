package invaders.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import invaders.GameObject;
import invaders.entities.Player;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import invaders.Projectil.Projectile;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileNotFoundException;

import invaders.enemy.Enemy;
import invaders.physics.BoxCollider;
import invaders.bunker.Bunker;



/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine {

	private List<GameObject> gameobjects;
	private List<Renderable> renderables;
	List<Enemy> shootingEnemies = new ArrayList<>();
	private Player player;

	private boolean left;
	private boolean right;

	private boolean gameRunning = true;
	private int aliensKilled = 0;
	private GameWindow gameWindow;  // 增加一个指向GameWindow的引用
	private long startTime = System.currentTimeMillis(); // 记录开始时间

	boolean hasAliveAliens;



	public GameEngine(String config){
		// read the config here
		gameobjects = new ArrayList<GameObject>();
		renderables= new ArrayList<>();

		player = new Player("src/main/resources/config.json");
		renderables.add(player);

		initializeFromConfig(config);
	}
	private void initializeFromConfig(String config) {
        JSONParser parser = new JSONParser();
        // 读取并解析文件
        JSONObject jsonObject = null;
        try {
            Object obj = parser.parse(new FileReader(config));
            jsonObject = (JSONObject) obj;
            // other handling, such as showing a user-friendly message
        } catch (FileNotFoundException e) {
            // handle file not found
        } catch (IOException e) {
            // handle ioexception
        } catch (ParseException e) {
            // handle file not found
        }

        // 初始化外星人
        JSONArray aliensArray = (JSONArray) jsonObject.get("Enemies");
        for (int i = 0; i < aliensArray.size(); i++) {
            JSONObject alienDetails = (JSONObject) aliensArray.get(i);
            JSONObject alienPosition = (JSONObject) alienDetails.get("position");
			double alienX = ((Number) alienPosition.get("x")).doubleValue();
			double alienY = ((Number) alienPosition.get("y")).doubleValue();
            String projectileType = (String) alienDetails.get("projectile");

            String imagePath;
			if (i < 5) {
				imagePath = "src/main/resources/alien_1.png";
			} else if (i < 10) {
				imagePath = "src/main/resources/alien_2.png";
			} else {
				imagePath = "src/main/resources/alien_3.png";
			}

            Enemy alien = new Enemy.EnemyBuilder()
                    .setPosition(new Vector2D(alienX, alienY))
                    .setCollider(new BoxCollider(25, 30, new Vector2D(alienX, alienY))) // 如果需要的话
                    .setProjectileType(projectileType)
                    .setImagePath(imagePath)
                    .build();
            gameobjects.add(alien);
            renderables.add(alien);
        }
		//初始化bunker
		JSONArray bunkersArray = (JSONArray) jsonObject.get("Bunkers");
		for (int i = 0; i<  bunkersArray.size();i++){
			JSONObject bunkersDetails = (JSONObject)  bunkersArray.get(i);
			JSONObject bunkerPosition = (JSONObject) bunkersDetails.get("position");
			double bunkerX = ((Number) bunkerPosition.get("x")).doubleValue();
			double bunkerY = ((Number) bunkerPosition.get("y")).doubleValue();
			JSONObject bunkerSize = (JSONObject) bunkersDetails.get("size");
			double bunkerWidth = ((Number) bunkerSize.get("x")).doubleValue();
			double bunkerHeight = ((Number) bunkerSize.get("y")).doubleValue();
			String imagePath;
			imagePath = "src/main/resources/Space_invaders_character_2.jpeg";
			Bunker bunker = new  Bunker.BunkerBuilder()
					.setPosition(new Vector2D(bunkerX, bunkerY))
					.setCollider(new BoxCollider(bunkerWidth, bunkerHeight, new Vector2D(bunkerX, bunkerY)))
					.setSize(bunkerWidth, bunkerHeight)
					.setImagePath(imagePath)
					.build();
			gameobjects.add(bunker);
			renderables.add(bunker);
		}
		//初始化Player


    }

	public void setGameWindow(GameWindow gameWindow) {
		this.gameWindow = gameWindow;
	}

	/**
	 * Updates the game/simulation
	 */
	public void update() {
		movePlayer();

		// 需要移除的对象列表
		List<GameObject> toRemoveObjects = new ArrayList<>();
		List<Renderable> toRemoveRenderables = new ArrayList<>();
		List<Renderable> toRemoveEnemys = new ArrayList<>();
		boolean changeDirection = false;
		for (GameObject go : gameobjects) {
			if (go instanceof Enemy) {
				Enemy enemy = (Enemy) go;
				if (enemy.getPosition().getX() <= 5 || enemy.getPosition().getX() + enemy.getWidth() >= 635    ) {
					changeDirection = true;
					break;
				}
			}
		}
		hasAliveAliens = false;
		for (int i = 0; i < gameobjects.size(); i++) {
			GameObject go=gameobjects.get(i);
			if (changeDirection && go instanceof Enemy) {
				Enemy enemy = (Enemy) go;
				enemy.down(); // 向下移动
				enemy.reverseDirection(); // 反转方向
			}

			go.update();

			if (go instanceof Projectile) {
				Projectile outerProjectile = (Projectile) go;
				for (GameObject innerGo : gameobjects) {
					if (innerGo instanceof Projectile && innerGo != go) {
						Projectile innerProjectile = (Projectile) innerGo;
						if (outerProjectile.getShooter() instanceof Player && innerProjectile.getShooter() instanceof Enemy
								|| outerProjectile.getShooter() instanceof Enemy && innerProjectile.getShooter() instanceof Player) {
							if (outerProjectile.getCollider().isColliding(innerProjectile.getCollider())) {
								toRemoveObjects.add(outerProjectile);
								toRemoveObjects.add(innerProjectile);
								toRemoveRenderables.add(outerProjectile);
								toRemoveRenderables.add(innerProjectile);
								Enemy.removeProjectile(innerProjectile);
								player.clearProjectile();

							}
						}
					}
				}
			}

			// 如果是Projectile并标记为删除
			if (go instanceof Projectile) {
				Projectile projectile = (Projectile) go;
				if (projectile.getPosition().getY() <= 5 || projectile.getPosition().getY() >= 390) {
					toRemoveObjects.add(go);
					toRemoveRenderables.add(projectile);
					player.clearProjectile();
					if (projectile.getShooter() instanceof Enemy){
						Enemy.removeProjectile(projectile);
					}

				}

				// 检查与敌人的碰撞
				if(projectile.getShooter() instanceof Player){

					for (GameObject innerGo : gameobjects) {
						if (innerGo instanceof Enemy) {
							Enemy enemy = (Enemy) innerGo;
							if (projectile.getCollider().isColliding(enemy.getCollider()) && enemy.isAlive()) {
								enemy.setAlive(false);
								aliensKilled++;
								Enemy.increaseEnemySpeed(); // 增加所有外星人的速度
								toRemoveRenderables.add(enemy);
								toRemoveRenderables.add(projectile);
								toRemoveObjects.add(enemy);
								toRemoveObjects.add(projectile);
								toRemoveEnemys.add(enemy);
								player.clearProjectile();


							}
						}
						if (innerGo instanceof Bunker) {
							Bunker bunker = (Bunker) innerGo;
							if (projectile.getCollider().isColliding(bunker.getCollider()) && bunker.isAlive()) {
								bunker.takeDamage(1);
								toRemoveObjects.add(projectile);
								toRemoveRenderables.add(projectile);
								player.clearProjectile();

								// 如果掩体不再存活，从游戏中移除
								if (!bunker.isAlive()) {
									toRemoveObjects.add(bunker);
									toRemoveRenderables.add(bunker);
								}

							}

						}
					}
				}
				if(projectile.getShooter() instanceof Enemy){

					for (int j = 0; j < gameobjects.size(); j++) {
						GameObject innerGo=gameobjects.get(j);

						if (innerGo instanceof Bunker) {
							Bunker bunker = (Bunker) innerGo;
							if (projectile.getCollider().isColliding(bunker.getCollider()) && bunker.isAlive()) {
								bunker.takeDamage(1);
								toRemoveObjects.add(projectile);
								toRemoveRenderables.add(projectile);
								Enemy.removeProjectile(projectile);

								// 如果掩体不再存活，从游戏中移除
								if (!bunker.isAlive()) {
									toRemoveObjects.add(bunker);
									toRemoveRenderables.add(bunker);
								}
							}

						}
						if (projectile.getCollider().isColliding(player.getCollider())) {

							player.takeDamage(1);

							Enemy.removeProjectile(projectile);
							//removeGameObject(projectile);
							//removeRenderable(projectile);
							toRemoveObjects.add(projectile);
							toRemoveRenderables.add(projectile);
							break;
						}

					}

				}

			}
			if (go instanceof Bunker) {
				Bunker bunker = (Bunker) go;
				for (GameObject anotherGo : gameobjects) {  // 再次遍历所有游戏对象，寻找 Enemy 对象
					if (anotherGo instanceof Enemy) {
						Enemy enemy = (Enemy) anotherGo;  // 这里 'enemy' 被声明和初始化
						if (enemy.getCollider().isColliding(bunker.getCollider())) {
							toRemoveObjects.add(bunker);
							toRemoveRenderables.add(bunker);
						}
					}
				}
			}

			// 如果是敌人

			if (go instanceof Enemy) {
				Enemy enemy = (Enemy) go;
				hasAliveAliens = true;
				if (player.getCollider().isColliding(enemy.getCollider())) {
					// 玩家与外星人相撞，游戏结束
					gameOver();
					return; // 不需要进一步的更新了
				}

				if (enemy.getCollider().collidesWithBottomBoundary(390)) {
					gameOver();
					return; // 不需要进一步的更新了
				}

				Projectile projectile = enemy.shoot();
				if(projectile != null){
					gameobjects.add(projectile);
					renderables.add(projectile);
				}

			}


		}

		gameobjects.removeAll(toRemoveObjects);
		renderables.removeAll(toRemoveRenderables);
		shootingEnemies.removeAll(toRemoveEnemys);
		if (player.getHealth() <= 0) {
			gameOver();
		}
		if(!hasAliveAliens) gameWin();




		// 将外部边界检查与渲染对象分离
		for(Renderable ro: renderables){
			if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
				continue;
			}
			if(ro.getPosition().getX() + ro.getWidth() >= 640) {
				ro.getPosition().setX(639-ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(1);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= 400) {
				ro.getPosition().setY(399-ro.getHeight());
			}

			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(1);
			}
		}
	}


	public void removeGameObject(GameObject object) {
		gameobjects.remove(object);
	}

	public void removeRenderable(Renderable renderable) {
		renderables.remove(renderable);
	}


	private void gameOver() {
		gameRunning = false;

		gameWindow.stopGameLoop(); // 使用GameWindow引用来停止游戏循环

		long endTime = System.currentTimeMillis();
		long gameTimeMillis = endTime - startTime;
		long gameTimeSeconds = gameTimeMillis / 1000;

		System.out.println("game over!");
		System.out.println("You killed " + aliensKilled + "aliens.");
		System.out.println("Game duration: " + gameTimeSeconds + " second.");
	}

	private void gameWin() {
		gameRunning = false;

		gameWindow.stopGameLoop(); // 使用GameWindow引用来停止游戏循环

		long endTime = System.currentTimeMillis();
		long gameTimeMillis = endTime - startTime;
		long gameTimeSeconds = gameTimeMillis / 1000;

		System.out.println("Congratulations, you won!");
		System.out.println("You killed " + aliensKilled + "aliens.");
		System.out.println("Game duration: " + gameTimeSeconds + " second.");
	}

	public boolean isGameRunning() {
		return gameRunning;
	}

	public List<Renderable> getRenderables(){
		return renderables;
	}
	public List<GameObject> getGameObjects() {
		return this.gameobjects;
	}

	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased(){
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}
	public void rightPressed(){
		this.right = true;
	}

	public boolean shootPressed(){
		Projectile newProjectile = player.shoot();
		if(newProjectile!=null){
			renderables.add(newProjectile);
			gameobjects.add(newProjectile);
			return true;
		}
		return false;
	}

	private void movePlayer(){
		if(left){
			player.left();
		}

		if(right){
			player.right();
		}
	}
}
