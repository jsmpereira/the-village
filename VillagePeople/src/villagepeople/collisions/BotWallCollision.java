package villagepeople.collisions;

import villagepeople.entities.Bot;
import villagepeople.entities.StaticEntity;
import villagepeople.events.CollisionWithStaticEntityEventArgs;

/**
 * Represents a collision between a bot and some wall.
 */
public class BotWallCollision extends Collision {

    /**
     * Creates a new collision between the given bot and wall.
     *
     * @param bot The bot that participates in the collision.
     * @param wall The wall hit by the bot.
     */
    public BotWallCollision(Bot bot, StaticEntity wall) {
        super(bot, wall);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void handle() {
        Bot bot = (Bot) entityA;
        StaticEntity entity = (StaticEntity) entityB;

        bot.restoreLocation();

        bot.onCollisionWithStaticEntity().fire(
                new CollisionWithStaticEntityEventArgs(entity));
    }
}
