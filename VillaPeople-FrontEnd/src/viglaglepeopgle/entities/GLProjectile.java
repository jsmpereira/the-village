package viglaglepeopgle.entities;

import viglaglepeopgle.game.Controller;
import villagepeople.weaponry.Bullet;
import villagepeople.weaponry.LaserRay;
import villagepeople.weaponry.Projectile;
import villagepeople.weaponry.Rocket;

/**
 * GLEntity for a projectile.
 *
 * Used to display a projectile with OpenGL.
 */
public abstract class GLProjectile extends GLMovingEntity {

    /**
     * Factory method to create the right GLProjectile to handle a given
     * projectile.
     *
     * @param controller The controller of the game where this rocket belongs.
     * @param projectile The projectile that was fired.
     * @return The right GLProjectile derived instance that will handle the
     * given projectile.
     */
    public static GLProjectile createFor(Controller controller,
            Projectile projectile) {
        if (projectile instanceof Rocket) {
            return new GLRocket(controller, (Rocket) projectile);
        } else if (projectile instanceof Bullet) {
            return new GLBullet((Bullet) projectile);
        } else if (projectile instanceof LaserRay) {
            return new GLLaserRay((LaserRay) projectile);
        }

        return null;
    }


    /**
     * Creates a new GLProjectile for a given projectile in the game.
     *
     * @param projectile The corresponding projectile.
     */
    protected GLProjectile(Projectile projectile) {
        super(projectile);
    }
}
