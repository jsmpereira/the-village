package villagepeople.events;

import villagepeople.entities.StaticEntity;
import explicitlib.event.EventArgs;


/**
 * Arguments for the "on collision with static entity" event.
 */
public class CollisionWithStaticEntityEventArgs extends EventArgs {

    /**
     * Creates new EventArgs with the given parameters.
     *
     * @param entity The static entity in the collision.
     */
    public CollisionWithStaticEntityEventArgs(StaticEntity entity) {
        this.entity = entity;
    }


    /**
     * @return The static entity in the collision.
     */
    public StaticEntity getEntity() {
        return this.entity;
    }

    /** The static entity in the collision. */
    private StaticEntity entity;
}
