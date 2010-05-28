package villagepeople.events;

import villagepeople.entities.Bot;
import villagepeople.entities.InventoryItem;
import explicitlib.event.EventArgs;


/**
 * Arguments for the "on caught" event.
 */
public class ItemCaughtEventArgs extends EventArgs {

    /**
     * Creates new EventArgs with the given parameters.
     *
     * @param bot The bot that caught the item.
     * @param item The item that was caught.
     */
    public ItemCaughtEventArgs(Bot bot, InventoryItem item) {
        this.bot = bot;
        this.item = item;
    }


    /**
     * @return The bot that was hit.
     */
    public Bot getBot() {
        return this.bot;
    }


    /**
     * @return The item that was caught.
     */
    public InventoryItem getItem() {
        return this.item;
    }


    /** The bot that was hit. */
    private Bot bot;


    /** The The item that was caught. */
    private InventoryItem item;
}
