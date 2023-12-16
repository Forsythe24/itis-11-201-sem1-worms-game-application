package worms.game.entities.projectile;


import worms.game.ServerGame;
import worms.game.entities.axistype.XAxisType;
import worms.game.entities.axistype.YAxisType;
import worms.game.entities.physics.HorDirectionedEntity;
import worms.game.entities.platform.PlatformEntity;
import worms.game.entities.player.PlayerEntity;

public class BulletEntity extends ProjectileEntity {
    private final PlayerEntity shooter;
    private int age;
    private final double horVel;

    public BulletEntity(final ServerGame game, final PlayerEntity shooter, final double x, final double y,
                        final double horVel,
                        final XAxisType xAxisType, final YAxisType yAxisType) {
        super(game, 2. / 16., 1. / 16., x, y);

        this.shooter = shooter;

        switch (xAxisType) {
            case LEFT:
                setX(x);
                break;
            case RIGHT:
                setX(x - this.getWidth());
                break;
            case CENTER:
                // idk
                break;
        }

        switch (yAxisType) {
            case BOTTOM:
                setY(y);
                break;
            case TOP:
                setY(y - this.getHeight());
                break;
            case CENTER:
                // idk
                break;
        }

        age = 0;
        this.horVel = horVel;
    }

    @Override
    public void tick() {
        if (age >= ServerGame.GameSettings.BULLET_LIFESPAN) {
            getGame().removeEntity(getId());
            return;
        }

        shiftX(horVel);

        age++;
    }

    @Override
    public void handleCollision(final ServerGame.Entity otherEntity) {
        if (otherEntity instanceof PlatformEntity) {
            getGame().removeEntity(getId());
        }
    }

    @Override
    public HorDirectionedEntity.HorDirection getHorDirection() {
        return horVel > 0 ? HorDirectionedEntity.HorDirection.RIGHT : HorDirectionedEntity.HorDirection.LEFT;
    }

    @Override
    public PlayerEntity getShooter() {
        return shooter;
    }

    @Override
    public void setHorDirection(final HorDirectionedEntity.HorDirection horDirection) {
        throw new UnsupportedOperationException("Unimplemented method 'setHorDirection'");
    }
}
