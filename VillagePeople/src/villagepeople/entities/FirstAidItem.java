
package villagepeople.entities;

import java.awt.image.BufferedImage;

import villagepeople.events.ItemCaughtEventArgs;
import villagepeople.navigation.Map;
import villagepeople.settings.Settings;

import com.golden.gamedev.Game;

import explicitlib.event.Action;
import explicitlib.event.EventArgs;
import explicitlib.geometry.Point;

/**
 * First-aid kit, lying on the floor waiting to be caught.
 */
public class FirstAidItem extends InventoryItem {

    /** First-aid item's bounding radius. */
    private static final double BOUNDING_RADIUS =
        Settings.getDouble("entities.firstAidItem.boundingRadius");


    /** First-aid item's bounding radius. */
    private static final int ACTIVATION_INTERVAL =
        Settings.getInt("entities.firstAidItem.activationInterval");


    /**
     * Amount of health increase that one gets by catching this first-aid kit.
     */
    private static final int HEALTH_AMOUNT =
        Settings.getInt("entities.firstAidItem.healthAmount");


    /**
     * Creates a new first-aid kit for the given map, located at the center of
     * the given cell.
     *
     * @param map The map of the game.
     * @param cell The cell where this first-aid kit will be lying on.
     */
    public FirstAidItem(Map map, Point cell) {
        super(map, cell, BOUNDING_RADIUS, ACTIVATION_INTERVAL);

        // What happens when this item is caught?
        this.onCaught().attach(new Action() {
            @Override
            public void execute(Object source, EventArgs args) {
                Bot bot = ((ItemCaughtEventArgs) args).getBot();

                bot.increaseHealth(HEALTH_AMOUNT);
            }
        });
    }


    // XXX Remove!
    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage loadImage(Game game) {
        return game.getImage(Settings.SPRITE_HEALTH_PATH);
    }
}
