package worms.gui.view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import worms.game.ClientGame;
import worms.game.ServerGame;
import worms.game.entities.platform.PlatformEntity;
import worms.game.entities.player.PlayerEntity;
import worms.game.entities.player.TeamedPlayerEntity;
import worms.game.entities.projectile.BulletEntity;
import worms.game.entities.projectile.CannonballCollisionEntity;
import worms.game.entities.projectile.CannonballEntity;
import worms.game.entities.weapon.CannonEntity;
import worms.game.entities.weapon.PistolEntity;
import worms.game.entities.weapon.WeaponEntity;


public class GameView extends Region {
    private final double[][] gameViewRanges = new double[][] { { 0, 10 }, { 0, 10 } }; // {xRange, yRange}
    private final ClientGame game;
    private final Sprites sprites = new Sprites();

    private final Canvas canvas ;
    private final int numColumns ;
    private final int numRows ;
    private GraphicsContext gc;


    public GameView(int numColumns, int numRows, ClientGame game) {
        this.game = game;

        this.numColumns = numColumns ;
        this.numRows = numRows ;
        canvas = new Canvas();

        getChildren().add(canvas);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                layoutChildren();
            }
        };
        timer.start();
    }

    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight() ;

        canvas.setWidth(w+1);
        canvas.setHeight(h+1);

        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, w, h);
        
        

        gc.drawImage(sprites.backgroundSprite, 0, 0, w, h);

        //drawing entities
        for (ServerGame.Entity entity : game.getEntities().values()) {
            drawEntitySprite(entity);
        }
    }

    private void drawEntitySprite(ServerGame.Entity entity) {
         int x1 = remapXCoords(entity.getX()),
                y1 = remapYCoords(entity.getY()),
                x2 = x1 + rescaleWidth(entity.getWidth()),
                y2 = y1 + rescaleHeight(entity.getHeight());

         int minX = Math.min(x1, x2),
                minY = Math.min(y1, y2),
                maxX = Math.max(x1, x2),
                maxY = Math.max(y1, y2);

         int width = maxX - minX, height = maxY - minY;

         Image sprite;

        if (entity instanceof  PlayerEntity playerEntity) {
            if (playerEntity instanceof  TeamedPlayerEntity teamedPlayerEntity) {
                sprite = switch (teamedPlayerEntity.getTeam()) {
                    case RED -> switch (playerEntity.getHorDirection()) {
                        case LEFT -> sprites.leftPlayerSprite;
                        case RIGHT -> sprites.rightPlayerSprite;
                    };
                    case BLUE -> switch (playerEntity.getHorDirection()) {
                        case LEFT -> sprites.leftBluePlayerSprite;
                        case RIGHT -> sprites.rightBluePlayerSprite;
                    };
                };
            } else {
                sprite = switch (playerEntity.getHorDirection()) {
                    case LEFT -> sprites.leftPlayerSprite;
                    case RIGHT -> sprites.rightPlayerSprite;
                };
            }
        } else if (entity instanceof WeaponEntity) {
            if (entity instanceof  PistolEntity pistolEntity) {
                sprite = switch (pistolEntity.getHorDirection()) {
                    case LEFT -> sprites.leftPistolSprite;
                    case RIGHT -> sprites.rightPistolSprite;
                };
            } else if (entity instanceof  CannonEntity cannonEntity) {
                // if-statement is now redundant, but in the future, when more weaponry is added, it will come in handy
                sprite = switch (cannonEntity.getHorDirection()) {
                    case LEFT -> sprites.leftCannonSprite;
                    case RIGHT -> sprites.rightCannonSprite;
                };
            } else {
                throw new IllegalArgumentException("Entity of unknown type");
            }

        } else if (entity instanceof  BulletEntity bulletEntity) {
            sprite = switch (bulletEntity.getHorDirection()) {
                case LEFT -> sprites.leftBulletSprite;
                case RIGHT -> sprites.rightBulletSprite;
            };
        } else if (entity instanceof CannonballEntity) {

            sprite = sprites.cannonBallSprite;

        } else if (entity instanceof PlatformEntity) {

            sprite = sprites.grassBlockSprite;

        } else if (entity instanceof CannonballCollisionEntity) {
            sprite = sprites.cannonBallCollisionSprite;
        }

        else {
            throw new IllegalArgumentException("Entity of unknown type");
        }
        gc.drawImage(sprite, minX, minY, width, height);
    }

    @Override
    protected double computePrefWidth(double height) {
        return 20 * numColumns;
    }

    @Override
    protected double computePrefHeight(double width) {
        return 20 * numRows ;
    }


    private static class Sprites {
        private final Image rightPlayerSprite;
        private final Image leftPlayerSprite;
        private final Image rightBluePlayerSprite;
        private final Image rightCannonSprite;
        private final Image leftCannonSprite;
        private final Image leftBluePlayerSprite;
        private final Image rightPistolSprite;
        private final Image leftPistolSprite;
        private final Image rightBulletSprite;
        private final Image leftBulletSprite;

        private final Image cannonBallSprite;

        private final Image grassBlockSprite;
        
        private final Image backgroundSprite;

        private final Image cannonBallCollisionSprite;

        private Sprites() {
            rightPlayerSprite = new Image("right-facing-radioactive-worm.png");
            leftPlayerSprite = new Image("left-facing-radioactive-worm.png");

            rightBluePlayerSprite = new Image("right-facing-normal-worm.png");
            leftBluePlayerSprite = new Image("left-facing-normal-worm.png");

            rightPistolSprite = new Image("right-facing-pistol.png");
            leftPistolSprite = new Image("left-facing-pistol.png");

            rightCannonSprite = new Image("right-facing-cannon.png");
            leftCannonSprite = new Image("left-facing-cannon.png");

            rightBulletSprite = new Image("right-facing-bullet.png");
            leftBulletSprite = new Image ("left-facing-bullet.png");

            cannonBallSprite = new Image("snowball.png");

            grassBlockSprite = new Image("snow-ice-platform.png");
            
            backgroundSprite = new Image("christmas-background.png");

            cannonBallCollisionSprite = new Image("explosion.png");
        }
    }



    private int remapXCoords( double gameX) {
        return remap(gameX, gameViewRanges[0][0], gameViewRanges[0][1], 0, getWidth());
    }

    private int remapYCoords( double gameY) {
        return remap(gameY, gameViewRanges[1][0], gameViewRanges[1][1], getHeight(), 0);
    }

    private int remap( double initialPoint,  double initialBottom,  double initialTop,
                       double newBottom,  double newTop) {

         double ratio = (initialPoint - initialBottom) / (initialTop - initialBottom);

         double newDist = (newTop - newBottom) * ratio;

         double newPoint = newBottom + newDist;
        return (int) Math.round(newPoint);
    }

    private int rescaleWidth( double gameWidth) {
        return rescale(gameWidth, gameViewRanges[0][1] - gameViewRanges[0][0], getWidth());
    }

    private int rescaleHeight( double gameHeight) {
        return rescale(gameHeight, gameViewRanges[1][1] - gameViewRanges[1][0], -getHeight());
    }

    private int rescale( double initialLength,  double initialRange,  double newRange) {

         double ratio = initialLength / initialRange;

         double newLength = newRange * ratio;
        return (int) Math.round(newLength);
    }
}