package worms.game;

import worms.game.action.ActionSet;
import worms.game.entities.physics.HorDirectionedEntity;
import worms.game.entities.platform.PlatformEntity;
import worms.game.entities.player.PlayerEntity;
import worms.game.entities.physics.Vector2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Logger;


public class ServerGame {
    public static abstract class Entity implements Serializable {
        private transient final ServerGame game;
        private final int id;

        private final double width, height;

        private double x, y;
        private ActionSet actionSet;

        public Entity( ServerGame game,  double width,  double height,  double x,
                 double y) {
            this.game = game;
            this.id = this.game.getSmallestAvailableId();
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;

            actionSet = new ActionSet();

            game.addEntity(this);
        }

        public ServerGame getGame() {
            return game;
        }

        public ActionSet getActionSet() {
            return actionSet;
        }

        public  int getId() {
            return id;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }

        public double shiftX( double shiftFactor) {
            return x += shiftFactor;
        }

        public double shiftY( double shiftFactor) {
            return y += shiftFactor;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getLeftX() {
            return getX();
        }

        public double getBottomY() {
            return getY();
        }

        public double getRightX() {
            return getLeftX() + getWidth();
        }

        public double getTopY() {
            return getBottomY() + getHeight();
        }

        public double getCenterX() {
            return getLeftX() + getWidth() / 2;
        }

        public double getCenterY() {
            return getBottomY() + getHeight() / 2;
        }

        public void setX( double x) {
            this.x = x;
        }

        public void setY( double y) {
            this.y = y;
        }

        public void setActionSet( ActionSet actionSet) {
            this.actionSet = actionSet;
        }

        public abstract void tick();

        public abstract void handleCollision(Entity otherEntity);

        public boolean isColliding( Entity otherEntity) {
            return getX() < otherEntity.getX() + otherEntity.getWidth() && getX() + getWidth() > otherEntity.getX()
                    && getY() < otherEntity.getY() + otherEntity.getHeight()
                    && getY() + getHeight() > otherEntity.getY()
                    && this != otherEntity;
        }

        public Vector2D getCollisionNormal(Entity otherEntity) {
             double xOverlap = Math.min(this.getX() + this.getWidth(), otherEntity.getX() + otherEntity.getWidth())
                    - Math.max(this.getX(), otherEntity.getX());
             double yOverlap = Math.min(this.getY() + this.getHeight(),
                    otherEntity.getY() + otherEntity.getHeight())
                    - Math.max(this.getY(), otherEntity.getY());

            if (xOverlap > yOverlap) {
                return new Vector2D(0, Math.signum(otherEntity.getY() - this.getY()));
            } else if (xOverlap < yOverlap) {
                return new Vector2D(Math.signum(otherEntity.getX() - this.getX()), 0);
            } else {
                return new Vector2D(Math.signum(otherEntity.getX() - this.getY()),
                        Math.signum(otherEntity.getY() - this.getY()));
            }
        }
    }

    public static class GameSettings {
        public static final double GLOBAL_GRAVITY = -0.05;

        public static final double WALK_SPEED = 0.125;
        public static final double JUMP_VEL = 0.5;
        public static final double FLIGHT_VEL = 0.5;
        public static final double BULLET_SPEED = 0.25;
        public static final int BULLET_LIFESPAN = 20;
        public static final double CANNONBALL_SPEED = 0.15;
        public static final double MOLOTOV_COCKTAIL_SPEED = 0.15;
        public static final int CANNONBALL_LIFESPAN = 60;
        public static final int MOLOTOV_COCKTAIL_LIFESPAN = 60;
        public static final int CANNONBALL_COLLISION_LIFESPAN = 1;
        public static final int FIRE_LIFESPAN = 30;
    }

    private static final Logger logger = Logger.getLogger("Server");

    public static Logger getLogger() {
        return logger;
    }

    private int smallestAvailableId = 0;
    private final TreeMap<Integer, Entity> entities;

    public ServerGame() {
        entities = new TreeMap<>();

        init();
    }

    public TreeMap<Integer, Entity> getEntities() {
        return entities;
    }

    public void updateActionSet( int id,  ActionSet actionSet) {
        entities.get(id).setActionSet(actionSet);
    }

    public void removeEntity( int id) {
        entities.remove(id);
    }

    public void tick() {
        //to avoid concurrent modification exceptions
         TreeMap<Integer, Entity> entitiesCopy = new TreeMap<>(entities);
        for (Entity entity : entitiesCopy.values()) {
            entity.tick();
        }

        // collisions
        for ( Entity entity1 : entitiesCopy.values()) {
            for ( Entity entity2 : entitiesCopy.values()) {
                if (entity1.isColliding(entity2)) {
                    entity1.handleCollision(entity2);
                }
            }
        }
    }

    public int spawnPlayerEntity() {
         PlayerEntity player = new PlayerEntity(this, 4.5, 5, HorDirectionedEntity.HorDirection.LEFT);
        return player.getId();
    }

    private int getSmallestAvailableId() {
        return smallestAvailableId++;
    }

    private void init() {
        try (Scanner platforms = new Scanner(new File("src/main/resources/ice-map-platform-rectangles.csv"))) {
            while (platforms.hasNextLine()) {
                 String[] dimensionsString = platforms.nextLine().split(",");
                new PlatformEntity(this,
                        Double.parseDouble(dimensionsString[0]),
                        Double.parseDouble(dimensionsString[1]),
                        Double.parseDouble(dimensionsString[2]),
                        Double.parseDouble(dimensionsString[3]));
            }
        } catch ( FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getId(), entity);
    }
}
