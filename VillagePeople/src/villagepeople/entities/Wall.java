
package villagepeople.entities;

import explicitlib.geometry.Point;
import explicitlib.geometry.Size;

/**
 * A wall.
 */
public class Wall extends StaticEntity {

    /**
     * Creates a new wall. Note that it still needs to be manually added to the
     * map, after creation.
     *
     * @param location {@inheritDoc}
     * @param size {@inheritDoc}
     */
    public Wall(Point location, Size size) {
        super(location, size);
    }
}
