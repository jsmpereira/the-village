package villagepeople.steering;

import villagepeople.entities.Bot;
import explicitlib.geometry.Vector;


/**
 * Abstract steering behavior.
 *
 * All steering behaviors work by calculating the velocity that should be
 * applied to the bots in order to satisfy their definition.
 *
 * Individual steering behaviors can be enabled or disabled, and there can be
 * several of them active at the same time. In fact, that's the way most
 * flocking behaviors, like the flight of birds, are simulated.
 *
 * @see http://www.red3d.com/cwr/steer/
 */
public abstract class SteeringBehavior {

    /**
     * {@inheritDoc}
     *
     * @param agent {@inheritDoc}
     */
    public SteeringBehavior(Bot agent) {
        this.agent = agent;
    }


    /**
     * Calculates the velocity to apply to the bot in order to create the motion
     * desired by this steering behavior.
     *
     * @param timeElapsed Time elapsed since last call
     * @return The new velocity to apply to the bot.
     */
    public abstract Vector calculateVelocity(long timeElapsed);


    /**
     * The agent this behavior applies to.
     */
    protected Bot agent;
}
