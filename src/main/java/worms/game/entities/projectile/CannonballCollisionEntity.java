package worms.game.entities.projectile;


import worms.game.ServerGame;

public class CannonballCollisionEntity extends ServerGame.Entity {
    private int age;


    public CannonballCollisionEntity(ServerGame game,  double x,  double y) {
        super(game, 1. / 2., 1. / 2., x, y);
        age = 0;
    }

    @Override
    public void tick() {
        if (age >= ServerGame.GameSettings.CANNONBALL_COLLISION_LIFESPAN) {
            getGame().removeEntity(getId());
        }

        age++;
    }

    @Override
    public void handleCollision(ServerGame.Entity otherEntity) {
    }
}
