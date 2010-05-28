package viglaglepeopgle.entities;

import viglaglepeopgle.renderable.Box;
import villagepeople.weaponry.LaserRay;
import villagepeople.weaponry.Projectile;
import explicitlib.geometry.Point3D;
import explicitlib.geometry.Size3D;
import explicitlib.geometry.Vector3D;

/**
 * GLEntity for a laser ray.
 *
 * Used to display a laser ray in OpenGL.
 */
public class GLLaserRay extends GLProjectile {

    /**
     * Creates a new GLBullet for a given laser ray in the game.
     *
     * @param laserRay The corresponding laser ray.
     */
    public GLLaserRay(LaserRay laserRay) {
        super(laserRay);

        Size3D boxSize = new Size3D((float) laserRay.getBoundingRadius() * 10,
                (float) laserRay.getBoundingRadius(),
                (float) laserRay.getBoundingRadius());
        this.box = new Box(boxSize, new Point3D(0, 0, 0),
                new Vector3D(0, 1, 0));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void render() {
        box.render();
    }


    /**
     * @return The box that represents the laser ray on screen.
     */
    public Box getBox() {
        return this.box;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(long timeElapsed) {
        Projectile projectile = (Projectile) this.entity;

        Point3D boxLocation = new Point3D(
                projectile.getLocation().getX(),
                projectile.getShooter().getBoundingRadius() / 2.0,
                -projectile.getLocation().getY());
        box.setRotation((float) projectile.getRotation());
        box.setCenter(boxLocation);
    }


    /** Internally, a laser ray is rendered as a box. */
    private Box box;
}
