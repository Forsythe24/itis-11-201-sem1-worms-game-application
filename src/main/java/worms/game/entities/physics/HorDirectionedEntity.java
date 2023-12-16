package worms.game.entities.physics;

public interface HorDirectionedEntity {
    enum HorDirection {
        LEFT(-1), RIGHT(1);

        private final int sign;

        HorDirection(final int sign) {
            this.sign = sign;
        }

        public int getSign() {
            return sign;
        }
    }

    HorDirection getHorDirection();

    void setHorDirection(HorDirection horDirection);
}
