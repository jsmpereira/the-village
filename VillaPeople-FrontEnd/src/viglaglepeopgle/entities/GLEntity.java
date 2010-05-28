package viglaglepeopgle.entities;

import viglaglepeopgle.renderable.Renderable;
import villagepeople.entities.Entity;

/**
 * A GLEntity contains the corresponding village people entity
 * and can be rendered using OpenGL.
 *
 * Just like there is a derived entity for each kind of game entity in
 * VillagePeople, there will be derived classes of GLEntity for bots, walls,
 * weapons, etc.
 */
public abstract class GLEntity implements Renderable {

    /**
     * Creates a new GLEntity for the given village people entity.
     *
     * @param entity The village people entity to associate with this GLEntity
     * instance.
     */
    public GLEntity(Entity entity) {
        this.entity = entity;
    }


    /**
     * {@inheritDoc}
     */
    public String toString() {
        return this.getClass().getSimpleName() + "(" + this.entity + ")";
    }


    /** The corresponding village people entity. */
    protected Entity entity;
}
