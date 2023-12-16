package worms.game.entities.weapon;


import worms.game.ServerGame;

public abstract class WeaponEntity extends ServerGame.Entity {
    public WeaponEntity(ServerGame game, double width, double height, double x, double y) {
        super(game, width, height, x, y);
    }

    public void release(){}
}
