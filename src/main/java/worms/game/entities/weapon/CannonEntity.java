package worms.game.entities.weapon;


import worms.game.ServerGame;
import worms.game.entities.axistype.XAxisType;
import worms.game.entities.axistype.YAxisType;
import worms.game.entities.physics.HorDirectionedEntity;
import worms.game.entities.player.PlayerEntity;
import worms.game.entities.projectile.CannonballEntity;

public class CannonEntity extends WeaponEntity {
    private final PlayerEntity owner;


    public CannonEntity( PlayerEntity owner) {
        super(owner.getGame(), 2. / 3., 2. / 3., Double.NaN, Double.NaN);

        this.owner = owner;
    }

    @Override
    public void tick() {
    }

    @Override
    public void handleCollision( ServerGame.Entity otherEntity) {
    }

    @Override
    public double getX() {
        return getCenterX() - getWidth() / 2;
    }

    @Override
    public double getY() {
        
        return owner.getBottomY() - 0.1;
    }

    @Override
    public double getCenterX() {
        return owner.getCenterX() + owner.getHorDirection().getSign() * getXOffset();
    }

    private double getXOffset() {
        return owner.getWidth() / 2. + this.getWidth() / 2.;
    }

    public HorDirectionedEntity.HorDirection getHorDirection() {
        return owner.getHorDirection();
    }

    @Override
    public void release() {
        if (this.getHorDirection() == HorDirectionedEntity.HorDirection.LEFT) {
            (new CannonballEntity(getGame(), owner, this.getLeftX(), getTopY(),
                    -ServerGame.GameSettings.CANNONBALL_SPEED,
                    XAxisType.LEFT, YAxisType.TOP))
                    .shiftYVel(ServerGame.GameSettings.JUMP_VEL);


        } else if (this.getHorDirection() == HorDirectionedEntity.HorDirection.RIGHT) {
            new CannonballEntity(getGame(), owner, this.getRightX(), getTopY(),
                    ServerGame.GameSettings.CANNONBALL_SPEED,
                    XAxisType.RIGHT, YAxisType.TOP)
                    .shiftYVel(ServerGame.GameSettings.JUMP_VEL);
        } else {
            ServerGame.getLogger().warning("Unknown direction \"" + getHorDirection() + "\".");
        }
    }
}
