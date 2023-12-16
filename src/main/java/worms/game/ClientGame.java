package worms.game;


import worms.game.action.ActionSet;
import worms.game.entities.player.PlayerEntity;
import worms.net.Client;

import worms.game.ServerGame.Entity;

import java.util.TreeMap;

public class ClientGame {
    private final int playerId;
    private final Client client;

    private final ActionSet actionSet;

    private TreeMap<Integer, ServerGame.Entity> entities;

    public ClientGame(Client client, int clientId) {
        this.client = client;
        playerId = clientId;
        actionSet = new ActionSet();
    }

    public ActionSet getActionSet() {
        return actionSet;
    }

    @Deprecated
    public PlayerEntity getPlayerEntity() {
        return (PlayerEntity) entities.get(playerId);
    }

    public void addEntity(ServerGame.Entity entity) {
        entities.put(entity.getId(), entity);
    }

    public TreeMap<Integer, ServerGame.Entity> getEntities() {
        return entities;
    }

    public void processEntityList(TreeMap<Integer, Entity> incomingEntityList) {
        entities = incomingEntityList;
    }

    public void tick() {
        if (!getEntities().containsKey(playerId)) {
            client.disconnect();
        }
    }
}
