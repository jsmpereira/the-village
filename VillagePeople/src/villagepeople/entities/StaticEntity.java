
package villagepeople.entities;

import explicitlib.geometry.Point;
import explicitlib.geometry.Size;

/**
 * A static entity is a physical and inanimate entity that lies somewhere on the
 * game map. Trees, walls and buildings are three examples of static entities.
 *
 * The most important thing to remember about static entities, is that they
 * occupy one or more contiguous map cells. Thus, a cell can either be entirely
 * occupied by a static entity (and an agent can't move over there), or
 * completely free. No cell can be partially occupied and partially free. This
 * simplifies geometry calculations, needed by modules like pathfinding and
 * navigation.
 */
public abstract class StaticEntity extends Entity {

    /**
     * Creates a new StaticEntity with the given size and location.
     *
     * @param location The location of the top-left corner of the entity.
     * @param size The size of the entity.
     */
    public StaticEntity(Point location, Size size) {
        super(location);
        this.size = size;
    }


    /**
     * @return The size of this entity i.e., the number of cells it occupies
     *         horizontally and vertically.
     */
    public Size getSize() {
        return size;
    }

    /**
     * The size of the entity, i.e, the number of cells it occupies to the right
     * and above the location point.
     */
    private Size size;
}
