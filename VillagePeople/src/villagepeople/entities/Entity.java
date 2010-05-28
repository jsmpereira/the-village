
package villagepeople.entities;

import org.apache.log4j.Logger;

import villagepeople.navigation.Parser;
import explicitlib.event.Event;
import explicitlib.geometry.Point;

/**
 * Game entity.
 *
 * Includes both static entities that can't move, like walls and moving
 * entities, like bots and projectiles.
 */
public abstract class Entity {

    /**
     * Creates a new Entity at the given location.
     *
     * @param location The location of the top-left corner of the entity.
     */
    public Entity(Point location) {
        this.location = location;
    }


    /**
     * @return The location of the top-left corner of this entity.
     */
    public Point getLocation() {
        return location;
    }


    /**
     * Sets the location of the entity.
     *
     * @param location The new location
     */
    public void setLocation(Point location) {
        this.location = location;
    }


    /**
     * @return the "On removed" event.
     */
    public Event onRemoved() {
        return this.onRemoved;
    }


    /** The location of the top-left corner of this entity on our map. */
    private Point location;

    /** Event fired when this entity is removed from the world. */
    private Event onRemoved = new Event(this);

    /** Class logger. */
    private static Logger logger = Logger.getLogger(Parser.class.getName());
}
