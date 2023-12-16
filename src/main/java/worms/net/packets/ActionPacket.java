package worms.net.packets;


import worms.game.ClientGame;
import worms.game.ServerGame;
import worms.game.action.ActionSet;

public class ActionPacket extends ClientPacket {
    public final ActionSet actionSet;

    @Deprecated
    public ActionPacket(final ServerGame.Entity entity) {
        actionSet = entity.getActionSet();
    }

    public ActionPacket(final ClientGame game) {
        this.actionSet = game.getActionSet();
    }

    @Override
    public String toString() {
        return "Packet [actionSet=" + actionSet + "]";
    }
}
