package villagepeople.collisions;

import villagepeople.entities.Bot;
import villagepeople.entities.InventoryItem;
import villagepeople.events.ItemCaughtEventArgs;

/**
 * Represents a collision between a bot and an inventory item.
 */
public class BotItemCollision extends Collision {

    /**
     * Creates a new collision between the given bot and the item.
     *
     * @param bot The bot that participates in the collision.
     * @param item The item that the bot collided with.
     */
    public BotItemCollision(Bot bot, InventoryItem item) {
        super(bot, item);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void handle() {
        Bot bot = (Bot) entityA;
        InventoryItem item = (InventoryItem) entityB;

        // Deactivate the item.
        item.deactivate();

        // Issue notifications regarding this collision
        bot.onCatchItem().fire(new ItemCaughtEventArgs(bot, item));
        item.onCaught().fire(new ItemCaughtEventArgs(bot, item));
    }
}
