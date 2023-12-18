package worms.game.entities.projectile;


import worms.game.ServerGame;
import worms.game.entities.axistype.XAxisType;
import worms.game.entities.axistype.YAxisType;
import worms.game.entities.physics.GravitationalEntity;
import worms.game.entities.physics.HorDirectionedEntity;
import worms.game.entities.physics.Vector2D;
import worms.game.entities.platform.PlatformEntity;
import worms.game.entities.player.PlayerEntity;
import worms.game.entities.player.TeamedPlayerEntity;

public class LaunchedMolotovCocktailEntity extends ProjectileEntity implements GravitationalEntity {
    private final PlayerEntity shooter;
    private int age;
    private final double horVel;
    private double xVel, yVel;


    public LaunchedMolotovCocktailEntity(ServerGame game, PlayerEntity shooter, double x, double y,
                                         double horVel,
                                         XAxisType xAxisType,
                                         YAxisType yAxisType) {
        super(game, 1. / 3., 1. / 3., x, y);

        this.shooter = shooter;

        switch (xAxisType) {
            case LEFT:
                setX(x);
                break;
            case RIGHT:
                setX(x - this.getWidth());
                break;
            case CENTER:
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
                break;
        }

        age = 0;
        this.horVel = horVel;
    }

    @Override
    public void tick() {
        if (age >= ServerGame.GameSettings.MOLOTOV_COCKTAIL_LIFESPAN) {
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
        if (otherEntity instanceof PlatformEntity ||
                otherEntity instanceof TeamedPlayerEntity playerEntity &&
                        playerEntity.getTeam() != ((TeamedPlayerEntity) shooter).getTeam()) {
            getGame().addEntity(new FireEntity(getGame(), getX(), getY()));
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
