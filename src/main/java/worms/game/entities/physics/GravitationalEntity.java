package worms.game.entities.physics;


import worms.game.ServerGame;

public interface GravitationalEntity extends KineticEntity {
    default double getGravitationalForce() {
        return ServerGame.GameSettings.GLOBAL_GRAVITY;
    }

    default void applyGravity() {
        shiftYVel(getGravitationalForce());
    }
}
