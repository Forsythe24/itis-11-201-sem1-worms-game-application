package worms.game.entities.platform;


import worms.game.ServerGame;

public class PlatformEntity extends ServerGame.Entity {
    public PlatformEntity(ServerGame game, double width, double height, double x,
            final double y) {
        super(game, width, height, x, y);
    }

    @Override
    public void tick() {
    }

    @Override
    public void handleCollision(ServerGame.Entity otherEntity) {
    }
}
