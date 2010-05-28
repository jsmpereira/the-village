package viglaglepeopgle.entities;

import viglaglepeopgle.renderable.Box;
import villagepeople.weaponry.Bullet;
import villagepeople.weaponry.Projectile;
import explicitlib.geometry.Point3D;
import explicitlib.geometry.Size3D;
import explicitlib.geometry.Vector3D;

/**
 * GLEntity for a bullet.
 *
 * Used to display a bullet in OpenGL.
 */
public class GLBullet extends GLProjectile {

    /**
     * Creates a new GLBullet for a given bullet in the game.
     *
     * @param bullet The corresponding bullet.
     */
    public GLBullet(Bullet bullet) {
        super(bullet);

        Size3D boxSize = new Size3D((float) bullet.getBoundingRadius(),
                (float) bullet.getBoundingRadius(),
                (float) bullet.getBoundingRadius());
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
     * @return The box that represents the bullet on screen.
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


    /** Internally, a bullet is rendered as a box. */
    private Box box;
}
