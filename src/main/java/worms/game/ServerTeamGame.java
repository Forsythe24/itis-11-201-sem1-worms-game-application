package worms.game;

import worms.game.entities.physics.HorDirectionedEntity;
import worms.game.entities.player.TeamedPlayerEntity;
import worms.utils.ArraySet;

public class ServerTeamGame extends ServerGame {
    private final ArraySet<TeamedPlayerEntity>
            redTeamPlayers = new ArraySet<>(),
            blueTeamPlayers = new ArraySet<>();

    public ServerTeamGame() {
        super();

        System.out.println("Playing teams.");
    }

    @Override
    public int spawnPlayerEntity() {
        TeamedPlayerEntity playerEntity;
        if (redTeamPlayers.size() < blueTeamPlayers.size()) {
            playerEntity = new TeamedPlayerEntity(this, TeamedPlayerEntity.Team.RED, 0, 3,
                    HorDirectionedEntity.HorDirection.RIGHT);
            redTeamPlayers.add(playerEntity);
            System.out.println("New red team player.");
        } else {
            playerEntity = new TeamedPlayerEntity(this, TeamedPlayerEntity.Team.BLUE, 8, 3,
                    HorDirectionedEntity.HorDirection.LEFT);
            blueTeamPlayers.add(playerEntity);
            System.out.println("New blue team player.");
        }
        return playerEntity.getId();
    }

    @Override
    public void removeEntity( int id) {
        if (getEntities().get(id) instanceof  TeamedPlayerEntity teamedPlayerEntity) {
            if (teamedPlayerEntity.getTeam() == TeamedPlayerEntity.Team.RED) {
                redTeamPlayers.remove(teamedPlayerEntity);
            } else {
                blueTeamPlayers.remove(teamedPlayerEntity);
            }
        }
        super.removeEntity(id);
    }
}
