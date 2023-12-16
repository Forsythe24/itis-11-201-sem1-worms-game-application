package worms.game.entities.player;

import worms.game.ServerGame;
import worms.game.entities.projectile.ProjectileEntity;

public class TeamedPlayerEntity extends PlayerEntity {
    public enum Team {
        RED, BLUE;
    }

    private final Team team;


    public TeamedPlayerEntity( ServerGame game,  Team team,  double x,  double y,
                               HorDirection direction) {
        super(game, x, y, direction);
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public void handleCollision( ServerGame.Entity otherEntity) {
        if (otherEntity instanceof  ProjectileEntity projectile) {
            if (projectile.getShooter() instanceof  TeamedPlayerEntity shooter) {
                if (shooter.team == this.team) {
                    return;
                }
            }
        }

        super.handleCollision(otherEntity);
    }
}
