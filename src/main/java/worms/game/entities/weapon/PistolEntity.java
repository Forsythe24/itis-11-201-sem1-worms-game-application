package worms.game.entities.weapon;


import worms.game.ServerGame;
import worms.game.entities.axistype.XAxisType;
import worms.game.entities.axistype.YAxisType;
import worms.game.entities.physics.HorDirectionedEntity;
import worms.game.entities.player.PlayerEntity;
import worms.game.entities.projectile.BulletEntity;

public class PistolEntity extends WeaponEntity {
    private final PlayerEntity owner;

    private final static double X_PISTOL_OFFSET = 0.15;

    public PistolEntity( PlayerEntity owner) {
        super(owner.getGame(), 2. / 8., 3./ 8., Double.NaN, Double.NaN);

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
        return owner.getBottomY() + 0.1;
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
            new BulletEntity(getGame(), owner, this.getLeftX() - X_PISTOL_OFFSET, getTopY(),
                    -ServerGame.GameSettings.BULLET_SPEED,
                    XAxisType.LEFT, YAxisType.TOP);
        } else if (this.getHorDirection() == HorDirectionedEntity.HorDirection.RIGHT) {
            new BulletEntity(getGame(), owner, this.getRightX() + X_PISTOL_OFFSET, getTopY(),
                    ServerGame.GameSettings.BULLET_SPEED,
                    XAxisType.RIGHT, YAxisType.TOP);
        } else {
            ServerGame.getLogger().warning("Unknown direction \"" + getHorDirection() + "\".");
        }
    }
}
