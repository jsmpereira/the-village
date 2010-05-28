
package villagepeople.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import villagepeople.game.VillageGame;
import villagepeople.navigation.Map;
import villagepeople.settings.Settings;

import com.golden.gamedev.Game;
import com.golden.gamedev.object.Sprite;

import explicitlib.event.Event;
import explicitlib.geometry.Point;
import explicitlib.geometry.Size;

/**
 * An inventory item is any kind of item that lies in the map and can be picked
 * by the bots. After being picked, they become inactive for a given interval,
 * called the activation interval.
 *
 * Note that an inventory item is placed at the center of its cell in the map.
 * There can only be one item per cell.
 *
 * Examples: weapons, health packs, etc
 */
public abstract class InventoryItem extends Entity {

    /**
     * Creates a new inventory item.
     *
     * @param map The map of the game.
     * @param cell The cell where this item will be lying on.
     * @param boundingRadius item's bounding radius, for collision detection.
     * @param activationInterval Item's activation interval.
     */
    public InventoryItem(Map map, Point cell, double boundingRadius,
            long activationInterval) {
        super(cell);
        this.isActive = true;
        this.activationInterval = activationInterval;
    }


    /**
     * {@inheritDoc}
     */
    public void update(long timeElapsed) {
        if (this.isActive) {
            //this.sprite.update(timeElapsed);
        } else {
            this.timeSinceDeactivation += timeElapsed;

            // Enable the item again if sufficient time has already passed.
            if (this.timeSinceDeactivation >= this.activationInterval) {
                this.isActive = true;
            }
        }
    }


    // XXX Remove
    public void render(Graphics2D graphics) {
        if (this.isActive) {
            this.sprite.render(graphics);
        }
    }


    // XXX Remove
    public void loadSprite(VillageGame game) {
        this.sprite = new Sprite(loadImage(game));

        // Set the sprite's location
        Size size = Settings.CELL_SIZE;
        double x = (this.getLocation().getX() + 0.5) * size.getWidth()
                - sprite.getWidth() / 2;
        double y = (game.getMap().getHeight() - this.getLocation().getY() - 0.5)
                * size.getHeight() - sprite.getHeight() / 2;
        this.sprite.setLocation(x, y);
    }


    // XXX Remove
    protected abstract BufferedImage loadImage(Game game);

    // XXX Remove
    protected Sprite sprite;


    /**
     * @return The item's bounding radius.
     */
    public double getBoundingRadius() {
        return this.boundingRadius;
    }


    /**
     * @return true if the item is active, false otherwise
     */
    public boolean isActive() {
        return this.isActive;
    }


    /**
     * Activates this item.
     */
    protected void activate() {
        this.isActive = true;
        this.onActivated().fire();
    }


    /**
     * Deactivates the item.
     */
    public void deactivate() {
        this.isActive = false;
        timeSinceDeactivation = 0;
    }


    /**
     * @return The "on caught" event.
     */
    public Event onCaught() {
        return this.onCaught;
    }


    /**
     * @return The "on activated" event.
     */
    public Event onActivated() {
        return this.onActivated;
    }


    /** Item's bounding radius. */
    protected double boundingRadius;

    /** Is the item active? */
    protected boolean isActive;

    /** Activation interval. */
    protected long activationInterval;

    /** Time passed since the item was deactivated. */
    protected long timeSinceDeactivation;

    /** Event fired when this item is caught. */
    protected Event onCaught = new Event(this);

    /** Event fired when this item is activated. */
    protected Event onActivated = new Event(this);
}
