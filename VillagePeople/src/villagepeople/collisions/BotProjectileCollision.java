package villagepeople.collisions;

import villagepeople.entities.Bot;
import villagepeople.events.HitEventArgs;
import villagepeople.weaponry.Projectile;

/**
 * Represents a collision between a bot and a projectile.
 */
public class BotProjectileCollision extends Collision {

    /**
     * Creates a new collision between the given bot and projectile.
     *
     * @param bot The bot that participates in the collision.
     * @param projectile The projectile that hit the bot.
     */
    public BotProjectileCollision(Bot bot, Projectile projectile) {
        super(bot, projectile);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void handle() {
        Bot bot = (Bot) entityA;
        Projectile projectile = (Projectile) entityB;

        // Issue notification that the bot has been hit.
        bot.onHit().fire(new HitEventArgs(bot, projectile));
        projectile.onHitBot().fire(new HitEventArgs(bot, projectile));
    }
}
