package viglaglepeopgle.display;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import viglaglepeopgle.util.TransformationUtil;
import explicitlib.event.Event;
import explicitlib.geometry.Point3D;
import explicitlib.geometry.Vector3D;

/**
 * The observer encapsulates the settings of the scene's observer, like
 * its position, where is it pointed at, etc.
 *
 * Note that this is not related to the Observer design pattern!
 */
public abstract class Observer {

    /**
     * Creates a new observer with the given attributes.
     *
     * @param location Observer's location.
     * @param heading Observer's heading vector.
     */
    public Observer(Point3D location, Vector3D heading) {
        this.location = location;
        this.heading = heading;
        this.rotation = 0;
        this.updateAbove();
    }

    /**
     * Creates a new observer with the given attributes.
     *
     * @param location Observer's location.
     * @param lookAt The point the observer is directed at.
     */
    public Observer(Point3D location, Point3D lookAt) {
        this(location, lookAt.minus(location));        
    }


    /**
     * Walks a given amount in the heading direction. If the amount is negative,
     * the observer will walk backwards.
     *
     * @param amount Amount of distance to walk.
     */
    public void walk(double amount) {
        this.location = this.location.plus(this.heading.multiplyBy(amount));
    }


    /**
     * Rotates the heading vector, allowing the observer to look to his left
     * and right, but not up and down (i.e., doesn't change the above vector).
     *
     * @param amount The angle to rotate.
     */
    public void rotateHeadingBy(double amount) {
        // The heading vector, before being transformed.
        FloatBuffer headingBuffer = FloatBuffer.wrap(new float[] {
                (float) heading.getX(),
                (float) heading.getY(),
                (float) heading.getZ(),
                1
        });

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
            GL11.glLoadIdentity();

            // Rotate by the given amount around the above vector.
            GL11.glRotatef((float) (amount * 180 / Math.PI),
                    (float) above.getX(),
                    (float) above.getY(),
                    (float) above.getZ());

            // Transform the vector.
            headingBuffer = TransformationUtil.transformPoints(headingBuffer,
                    GL11.GL_MODELVIEW_MATRIX);
        GL11.glPopMatrix();

        // Update the heading to its new value.
        this.heading = new Vector3D(headingBuffer.get(0), headingBuffer.get(1),
                headingBuffer.get(2));
    }

    /**
     * @return Observer's location.
     */
    public Point3D getLocation() {
        return this.location;
    }

    /**
     * Alters the location of this observer.
     *
     * @param location The new location.
     */
    public void setLocation(Point3D location) {
        this.location = location;
    }


    /**
     * @return Observer's heading vector.
     */
    public Vector3D getHeading() {
        return this.heading;
    }

    /**
     * Alters the heading vector of this observer.
     *
     * @param heading The new heading vector.
     */
    public void setHeading(Vector3D heading) {
        this.heading = heading;
        this.updateAbove();
    }

    /**
     * @return Observer's rotation along the axe defined by the heading vector.
     */
    public double getRotation() {
        return this.rotation;
    }

    /**
     * Alters the rotation angle along the axe defined by the heading vector
     * of this observer.
     *
     * @param rotation The new rotation angle.
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
        this.updateAbove();
    }

    public abstract void prepare();
    protected abstract void updateAbove();
    
    /** Observer's location. */
    protected Point3D location;

    /** Observer's heading vector. */
    protected Vector3D heading;

    /** Observer's rotation along the axe defined by the heading vector. */
    protected double rotation;

    /** Observer's above vector (calculated from the above properties). */
    protected Vector3D above;
}
