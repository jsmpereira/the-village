package villagepeople.events;

import villagepeople.entities.Bot;
import explicitlib.event.EventArgs;


/**
 * Arguments for the "on bot created" event.
 */
public class BotCreatedEventArgs extends EventArgs {

    /**
     * Creates new EventArgs with the given parameters.
     *
     * @param bot The bot that was created.
     */
    public BotCreatedEventArgs(Bot bot) {
        this.bot = bot;
    }


    /**
     * @return The bot that was created.
     */
    public Bot getBot() {
        return this.bot;
    }


    /** The bot that was created. */
    private Bot bot;
}
