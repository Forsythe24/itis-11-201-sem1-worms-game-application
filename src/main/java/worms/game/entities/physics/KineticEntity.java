package worms.game.entities.physics;

public interface KineticEntity {
    void setX(double newX);

    void setY(double newY);

    void setXVel(double newXVel);

    void setYVel(double newYVel);

    double getX();

    double getXVel();

    double getY();

    double getYVel();

    default double shiftX( double dX) {
        setX(getX() + dX);
        return getX();
    }

    default double shiftXVel( double dXVel) {
        setXVel(getXVel() + dXVel);
        return getXVel();
    }

    default double shiftY( double dY) {
        setY(getY() + dY);
        return getY();
    }

    default double shiftYVel( double dYVel) {
        setYVel(getYVel() + dYVel);
        return getYVel();
    }

    default void applyVelocity() {
        shiftX(getXVel());
        shiftY(getYVel());
    }
}
