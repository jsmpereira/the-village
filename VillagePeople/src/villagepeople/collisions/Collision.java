package villagepeople.collisions;

import villagepeople.entities.Entity;

/**
 * Represents a collision between two entities.
 *
 * For each kind of collision
 * (bot-bot, bot-wall, etc), there will be a derived class capable of handling
 * the collision.
 */
public abstract class Collision {

    /**
     * Creates a new collision between two entities.
     *
     * @param entityA The first entity that collided.
     * @param entityB The second entity.
     */
    public Collision(Entity entityA, Entity entityB) {
        this.entityA = entityA;
        this.entityB = entityB;
    }


    /**
     * Handle the collision.
     */
    public abstract void handle();


    /** The first collided entity. */
    protected Entity entityA;

    /** The other collided entity. */
    protected Entity entityB;
}
