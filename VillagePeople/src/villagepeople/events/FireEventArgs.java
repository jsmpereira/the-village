package villagepeople.events;

import villagepeople.entities.Bot;
import villagepeople.weaponry.Projectile;
import explicitlib.event.EventArgs;


/**
 * Arguments for the "on fire" event.
 */
public class FireEventArgs extends EventArgs {

    /**
     * Creates new EventArgs with the given parameters.
     *
     * @param bot The bot that fired.
     * @param projectile The projectile that was fired.
     */
    public FireEventArgs(Bot bot, Projectile projectile) {
        this.bot = bot;
        this.projectile = projectile;
    }


    /**
     * @return The bot that fired.
     */
    public Bot getBot() {
        return this.bot;
    }


    /**
     * @return The projectile that was fired.
     */
    public Projectile getProjectile() {
        return this.projectile;
    }


    /** The bot that fired. */
    private Bot bot;


    /** The projectile that was fired. */
    private Projectile projectile;
}
