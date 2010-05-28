package villagepeople.events;

import villagepeople.entities.Bot;
import villagepeople.weaponry.Projectile;
import explicitlib.event.EventArgs;


/**
 * Arguments for the "on hit" event.
 */
public class HitEventArgs extends EventArgs {

    /**
     * Creates new EventArgs with the given parameters.
     *
     * @param bot The bot that was hit.
     * @param projectile The projectile that hit the bot.
     */
    public HitEventArgs(Bot bot, Projectile projectile) {
        this.bot = bot;
        this.projectile = projectile;
    }


    /**
     * @return The bot that was hit.
     */
    public Bot getBot() {
        return this.bot;
    }


    /**
     * @return The projectile that hit the bot.
     */
    public Projectile getProjectile() {
        return this.projectile;
    }


    /** The bot that was hit. */
    private Bot bot;


    /** The projectile that hit the bot. */
    private Projectile projectile;
}
