package viglaglepeopgle.entities;

import explicitlib.event.Action;
import explicitlib.event.EventArgs;
import villagepeople.entities.MovingEntity;
import villagepeople.events.UpdateEventArgs;

/**
 * GLEntity for village people moving entities.
 */
public abstract class GLMovingEntity extends GLEntity {

    /**
     * Creates a new GLMovingEntity for the given village people moving entity.
     *
     * @param entity {@inheritDoc}
     */
    public GLMovingEntity(MovingEntity entity) {
        super(entity);

        // Update this glentity when the corresponding entity updates itself.
        entity.onUpdate().attach(new Action() {
            @Override
            public void execute(Object source, EventArgs args) {
                long timeElapsed = ((UpdateEventArgs) args).getTimeElapsed();
                GLMovingEntity.this.update(timeElapsed);
            }
        });
    }


    /**
     * Called to update this renderable entity, after the corresponding game
     * entity updated itself.
     *
     * @param timeElapsed the time elapsed since the last update.
     */
    public abstract void update(long timeElapsed);
}
