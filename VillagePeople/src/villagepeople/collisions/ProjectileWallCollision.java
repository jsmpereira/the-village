package villagepeople.collisions;

import villagepeople.entities.StaticEntity;
import villagepeople.events.CollisionWithStaticEntityEventArgs;
import villagepeople.weaponry.Projectile;

/**
 * Represents a collision between a projectile and some wall.
 */
public class ProjectileWallCollision extends Collision {

    /**
     * Creates a new collision between the given projectile and wall.
     *
     * @param projectile The projectile that participates in the collision.
     * @param wall The wall hit by the projectile.
     */
    public ProjectileWallCollision(Projectile projectile, StaticEntity wall) {
        super(projectile, wall);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void handle() {
        Projectile projectile = (Projectile) this.entityA;
        StaticEntity entity = (StaticEntity) this.entityB;

        projectile.onCollisionWithStaticEntity().fire(
                new CollisionWithStaticEntityEventArgs(entity));
    }
}
