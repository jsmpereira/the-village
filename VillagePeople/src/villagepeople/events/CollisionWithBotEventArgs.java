package villagepeople.events;

import villagepeople.entities.Bot;
import explicitlib.event.EventArgs;


/**
 * Arguments for the "on collision with bot" event.
 */
public class CollisionWithBotEventArgs extends EventArgs {

    /**
     * Creates new EventArgs with the given parameters.
     *
     * @param other The other bot collided.
     */
    public CollisionWithBotEventArgs(Bot other) {
        this.other = other;
    }


    /**
     * @return The other bot collided.
     */
    public Bot getOther() {
        return this.other;
    }

    /** The other bot collided. */
    private Bot other;
}
