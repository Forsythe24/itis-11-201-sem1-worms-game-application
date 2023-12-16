package worms.game.entities.projectile;

import worms.game.ServerGame;
import worms.game.entities.physics.HorDirectionedEntity;
import worms.game.entities.player.PlayerEntity;

public abstract class ProjectileEntity extends ServerGame.Entity implements HorDirectionedEntity {

    public ProjectileEntity(ServerGame game, double width, double height, double x, double y) {
        super(game, width, height, x, y);
    }

    public PlayerEntity getShooter(){
        return null;
    }
}
