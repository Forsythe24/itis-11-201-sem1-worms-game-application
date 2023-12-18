package worms.game.entities.projectile;

import worms.game.ServerGame;
import worms.game.entities.player.PlayerEntity;

public class FireEntity extends ServerGame.Entity {

    private int age;
    public FireEntity(ServerGame game,  double x,  double y) {
        super(game, 2. / 3., 2. / 3., x, y);
        age = 0;
    }

    @Override
    public void tick() {
        if (age >= ServerGame.GameSettings.FIRE_LIFESPAN) {
            getGame().removeEntity(getId());
        }

        age++;
    }

    @Override
    public void handleCollision(ServerGame.Entity otherEntity) {
    }
}
