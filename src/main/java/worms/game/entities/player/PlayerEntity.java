package worms.game.entities.player;


import worms.game.ServerGame;
import worms.game.action.Action;
import worms.game.entities.platform.PlatformEntity;
import worms.game.entities.physics.Vector2D;
import worms.game.entities.physics.GravitationalEntity;
import worms.game.entities.physics.HorDirectionedEntity;
import worms.game.entities.projectile.ProjectileEntity;
import worms.game.entities.weapon.CannonEntity;
import worms.game.entities.weapon.PistolEntity;
import worms.game.entities.weapon.WeaponEntity;

public class PlayerEntity extends ServerGame.Entity implements HorDirectionedEntity, GravitationalEntity {
    private HorDirection horDirection;
    private double xVel, yVel;
    private WeaponEntity weaponEntity;

    public PlayerEntity( ServerGame game,  double x,  double y,
                         HorDirection direction) {
        super(game, 1, 0.5, x, y);

        this.horDirection = direction;

        xVel = 0;
        yVel = 0;

        // starting weapon is a pistol
        this.weaponEntity = new PistolEntity(this);

    }

    @Override
    public double getYVel() {
        return yVel;
    }

    @Override
    public void setYVel( double yVel) {
        this.yVel = yVel;
    }

    @Override
    public double getXVel() {
        return xVel;
    }

    @Override
    public void setXVel( double xVel) {
        this.xVel = xVel;
    }

    @Override
    public HorDirection getHorDirection() {
        return horDirection;
    }

    @Override
    public void setHorDirection( HorDirection horDirection) {
        this.horDirection = horDirection;
    }

    @Override
    public void tick() {
        for ( Action action : getActionSet().getInstantActions()) {
            switch (action) {
                case JUMP -> {
                    if (getYVel() != 0) {
                        break;
                    }
                    shiftYVel(ServerGame.GameSettings.JUMP_VEL);
                }
                case SHOOT -> {
                    weaponEntity.release();
                }
                case SWITCH_WEAPON -> {
                    switchWeapon();
                }
                default -> ServerGame.getLogger().warning("Unknown action \"" + action + "\" in instant actions.");
            }
        }
        getActionSet().getInstantActions().clear();

        for ( Action action : getActionSet().getLongActions()) {
            switch (action) {
                case LEFT_WALK: {
                    setHorDirection(HorDirection.LEFT);
                    shiftX(-ServerGame.GameSettings.WALK_SPEED);
                    break;
                }

                case RIGHT_WALK: {
                    setHorDirection(HorDirection.RIGHT);
                    shiftX(ServerGame.GameSettings.WALK_SPEED);
                    break;
                }

                default:
                    ServerGame.getLogger().warning("Unknown action \"" + action + "\" in long actions.");
                    break;
            }
        }

        //physics
        applyGravity();
        applyVelocity();
    }

    public void switchWeapon() {
        getGame().removeEntity(weaponEntity.getId());
        weaponEntity = weaponEntity instanceof PistolEntity ? new CannonEntity(this) : new PistolEntity(this);
    }

    @Override
    public void handleCollision( ServerGame.Entity otherEntity) {
        if (otherEntity instanceof PlatformEntity) {
             Vector2D collisionNormal = getCollisionNormal(otherEntity);

            if (collisionNormal.getX() > 0) {
                this.setX(otherEntity.getX() - this.getWidth());
                this.setXVel(0);
            } else if (collisionNormal.getX() < 0) {
                this.setX(otherEntity.getX() + otherEntity.getWidth());
                this.setXVel(0);
            }

            if (collisionNormal.getY() > 0) {
                this.setY(otherEntity.getY() - this.getHeight());
                this.setYVel(0);
            } else if (collisionNormal.getY() < 0) {
                this.setY(otherEntity.getY() + otherEntity.getHeight());
                this.setYVel(0);
            }
        } else if (otherEntity instanceof ProjectileEntity) {
            die();
        }
    }

    private void die() {
        getGame().removeEntity(getId());
    }
}
