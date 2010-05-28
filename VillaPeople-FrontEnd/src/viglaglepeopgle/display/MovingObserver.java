package viglaglepeopgle.display;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.GLU;

import viglaglepeopgle.util.TransformationUtil;
import villagepeople.entities.Player;
import explicitlib.geometry.NullVectorException;
import explicitlib.geometry.Point3D;
import explicitlib.geometry.Vector3D;

public class MovingObserver extends Observer {

	public MovingObserver(Point3D location, Point3D lookAt, Player user) {
		super(location, lookAt);
		this.user = user;
	}

	public MovingObserver(Point3D location, Vector3D heading, Player user) {
		super(location, heading);
		this.user = user;
	}
	
	/**
     * Initializes the OpenGL state machine according to the observer
     * configuration.
     */
    public void prepare() {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();        
        setHeading(new Vector3D(user.getHeadingVector().getX(),0,-user.getHeadingVector().getY()));
		setLocation(new Point3D(user.getLocation().getX()+0.2,0.75,-user.getLocation().getY()));
        Point3D lookingAt = this.location.plus(this.heading);
        GLU.gluLookAt((float) location.getX(),
                (float) location.getY(),
                (float) location.getZ(),
                (float) lookingAt.getX(),
                (float) lookingAt.getY(),
                (float) lookingAt.getZ(),
                (float) this.above.getX(),
                (float) this.above.getY(),
                (float) this.above.getZ());
    }

    /**
     * Calculates the above camera vector.
     * This method must be called after modifying heading or rotation.
     *
     * @see Rectangle.getVerticesBuffer() to understand the math involved
     * in calculating the above vector.
     */
    protected void updateAbove() {
        // Find the axe and the amount of the rotation needed to transform
        // the default heading vector (0, 0, -1) into the current one.
        // Why? Because the same rotation was applied to the above vector,
        // which was initially pointing to (0, 1, 0).
        Vector3D initialHeading = new Vector3D(0, 0, -1);
        Vector3D rotationAxe = initialHeading.cross(this.heading);
        double rotationAngle = 0;
        try {
            rotationAngle = initialHeading.angleWith(this.heading);
        } catch (NullVectorException e) {
            // This is not going to happen!
            e.printStackTrace();
        }

        // This is the default above vector, before being transformed.
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        float[] points = new float[] {
                0, 1, 0, 1
        };

        buffer.put(points);
        buffer.rewind();

        // Now, let's use OpenGL routines to transform the vector for us :-)
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
            GL11.glLoadIdentity();

            // No need to translate anything because we are not transforming
            // points, but one vector.

            // Now rotate by this.rotation around the normal vector
            GL11.glRotatef((float) (this.rotation * 180 / Math.PI),
                    (float) heading.getX(),
                    (float) heading.getY(),
                    (float) heading.getZ());

            // First rotate rotationAngle around rotationAxe
            GL11.glRotatef((float) (rotationAngle * 180 / Math.PI),
                    (float) rotationAxe.getX(),
                    (float) rotationAxe.getY(),
                    (float) rotationAxe.getZ());

            // Transform the vector.
            buffer = TransformationUtil.transformPoints(buffer,
                    GL11.GL_MODELVIEW_MATRIX);
        GL11.glPopMatrix();

        this.above = new Vector3D(buffer.get(0), buffer.get(1), buffer.get(2));
        
    }

    private Player user;
}
