package villagepeople.collisions;

import villagepeople.entities.Bot;
import villagepeople.events.CollisionWithBotEventArgs;
import explicitlib.geometry.Point;
import explicitlib.geometry.Vector;

/**
 * Represents a collision between two bots.
 */
public class BotBotCollision extends Collision {

    /**
     * Creates a new collision between two bots.
     *
     * @param entityA {@inheritDoc}
     * @param entityB {@inheritDoc}
     * @param toOther The vector from entityA to entityB.
     * @param distance The distance between both entities.
     * @param overlapAmount The amount overlapping between both entities.
     */
    public BotBotCollision(Bot entityA, Bot entityB, Vector toOther,
            double distance, double overlapAmount) {
        super(entityA, entityB);

        this.toOther = toOther;
        this.distance = distance;
        this.overlapAmount = overlapAmount;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void handle() {
        Bot botA = (Bot) entityA;
        Bot botB = (Bot) entityB;

        if (distance == 0) {
            distance = 0.001;
        }

        assert distance != 0;

        Point oldLocation = botA.getLocation();
        botA.setLocation(botA.getLocation().plus(
                toOther.multiplyBy(-overlapAmount / distance)));

        // Issue a notification that there has been a collision between the bots
        botA.onCollisionWithBot().fire(new CollisionWithBotEventArgs(botB));
        botB.onCollisionWithBot().fire(new CollisionWithBotEventArgs(botA));

        // If botA is now colliding with some wall, try moving botB instead
        for (Point point : botA.getOccupyingCells()) {
            if (!botA.getGame().getMap().cellAt(point).isFree()) {
                botA.setLocation(oldLocation);
                oldLocation = botB.getLocation();
                botB.setLocation(botB.getLocation().plus(
                        toOther.multiplyBy(overlapAmount / distance)));
            }

            // If botB is colliding, give up
            for (Point cell : botB.getOccupyingCells()) {
                if (!botB.getGame().getMap().cellAt(cell).isFree()) {
                    botB.setLocation(oldLocation);
                    break;
                }
            }
        }
    }


    /** Vector that goes from entityA to entityB. */
    private Vector toOther;

    /** The distance between the bots. */
    private double distance;

    /** Amount of overlapping. */
    private double overlapAmount;
}
