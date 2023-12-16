package worms.game.entities.projectile;


import worms.game.ServerGame;
import worms.game.entities.axistype.XAxisType;
import worms.game.entities.axistype.YAxisType;
import worms.game.entities.physics.GravitationalEntity;
import worms.game.entities.physics.HorDirectionedEntity;
import worms.game.entities.player.PlayerEntity;

public class CannonballEntity extends ProjectileEntity implements GravitationalEntity {
    private final PlayerEntity shooter;
    private int age;
    private final double horVel;
    private double xVel, yVel;


    public CannonballEntity(ServerGame game, PlayerEntity shooter, double x, double y,
                            double horVel,
                            XAxisType xAxisType,
                            YAxisType yAxisType) {
        super(game, 1. / 4., 1. / 4., x, y);

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
        if (age >= ServerGame.GameSettings.CANNONBALL_LIFESPAN) {
            getGame().removeEntity(getId());
            return;
        }

        shiftX(horVel);

        applyGravity();
        applyVelocity();

        age++;
    }

    @Override
    public void handleCollision(ServerGame.Entity otherEntity) {
        getGame().addEntity(new CannonballCollisionEntity(getGame(), getX(), getY()));
        getGame().removeEntity(getId());
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
    public void setHorDirection(HorDirectionedEntity.HorDirection horDirection) {
        throw new UnsupportedOperationException("Unimplemented method 'setHorDirection'");
    }

    @Override
    public double getYVel() {
        return yVel;
    }

    @Override
    public void setYVel(double yVel) {
        this.yVel = yVel;
    }

    @Override
    public double getXVel() {
        return xVel;
    }

    @Override
    public void setXVel(double xVel) {
        this.xVel = xVel;
    }
}
