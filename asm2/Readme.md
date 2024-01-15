## How to Run the Code

To build the project, run:

```sh
gradle build
```

To run the project, run:

```sh
gradle run
```

## Design Patterns Implemented

### Builder Pattern

**Involved Files and Classes:**

* Enemy.java
* Bunker.java
  * EnemyBuilder.java (Inner Class)
  * BunkerBuilder.java (Inner Class)

### Strategy Pattern

**Involved Files and Classes:**

* ShootStrategy.java
* FastShootStrategy.java
* SlowShootStrategy.java

### State Pattern

**Involved Files and Classes:**

* BunkerState.java
* GreenState.java
* YellowState.java
* RedState.java

### Factory Pattern

**Involved Files and Classes:**

* Projectile.java
* SlowStraightProjectile.java
* FastStraightProjectile.java
* ProjectileFactory.java
* SlowStraightProjectileFactory.java
* FastStraightProjectileFactory.java

## Game End Notice

At the game's end (either game over or game win), the ending information will be output to the terminal, including the game’s duration and the number of aliens killed.

## Game Logic Explanation

In the game, there are two rows of Enemies. A Game Over state is triggered when Enemy objects descend to the bottom of the game interface. During gameplay, aliens teleport downward uniformly when touching the game screen sides (position y plus 10). When the remaining Enemies move on the route closest to the bottom of the screen and do not collide with the Player, the bottom-most Enemy will exceed the bottom of the game interface, thereby triggering a Game Over when the remaining Enemies in either the first or second row touch the screen boundary again.

**Note:** Due to the design logic of GameEngine, all game objects must remain on the game screen, so the specific behavior of triggering Game Over by Enemies exceeding the bottom of the screen may not be intuitively displayed on the screen during actual gameplay. And if the Enemy wants to fall and hits the player, the game will also gameover, and the Enemy will not show falling on the screen.

