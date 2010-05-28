package viglaglepeopgle.renderable;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import viglaglepeopgle.util.Angle;
import viglaglepeopgle.util.Texture;
import viglaglepeopgle.util.TransformationUtil;

import explicitlib.geometry.NullVectorException;
import explicitlib.geometry.Point3D;
import explicitlib.geometry.Size;
import explicitlib.geometry.Vector3D;

/**
 * A rectangle.
 */
public class Rectangle implements Renderable {

    /**
     * Creates a new rectangle with the given attributes and no texture.
     *
     * @param size Edge sizes.
     * @param center Center location.
     * @param normal Normal of the plane the rectangle is in.
     */
    public Rectangle(Size size, Point3D center, Vector3D normal) {
        this.size = size;
        this.center = center;
        this.normal = normal.normalize();
        this.rotation = 0;
        this.texture = Texture.NULL_TEXTURE;
        this.textureRepetitions = size;
    }


    /**
     * Creates a new rectangle with the given attributes.
     *
     * @param size Edge sizes.
     * @param center Center location.
     * @param normal Normal of the plane the rectangle is in.
     * @param texture the texture to apply to this rectangle.
     */
    public Rectangle(Size size, Point3D center, Vector3D normal,
            Texture texture) {
        this(size, center, normal);

        this.texture = texture;
    }


    /**
     * Creates a new rectangle with the given vertex points.
     *
     * @param p1 The first vertex.
     * @param p2 The second vertex.
     * @param p3 The third vertex.
     * @param p4 The fourth vertex.
     */
    public Rectangle(Point3D p1, Point3D p2, Point3D p3, Point3D p4) {
        Vector3D p1ToP2 = p2.minus(p1);
        Vector3D p2ToP3 = p3.minus(p2);

        // Is this a valid rectangle?
        if (p1.distanceTo(p2) != p3.distanceTo(p4)
                || p2.distanceTo(p3) != p1.distanceTo(p4)
                || p1ToP2.dot(p2ToP3) != 0) {
            throw new IllegalArgumentException("The points "
                    + p1 + ", " + p2 + ", " + p3 + ", " + p4
                    + " do not define a valid rectangle");
        }

        // Calculates the size of the rectangle
        this.size = new Size((float) p1.distanceTo(p2),
                (float) p2.distanceTo(p3));

        // The center is the middle point between points 1 and 3.
        this.center = Point3D.middle(p1, p3);

        // Now, calculate the normal using the cross product between two
        // vectors inside the rectangle.
        this.normal = p1ToP2.cross(p2ToP3).normalize();

        // Now, calculate the angle of rotation around the normal.
        Vector3D centerToP1 = p1.minus(center);
        Vector3D centerToV1 = this.getVertices()[0].minus(center);

        try {
            this.rotation = (float) centerToV1.angleWith(centerToP1);
        } catch (NullVectorException e) {
            // Not going to happen.
            e.printStackTrace();
        }

        this.texture = Texture.NULL_TEXTURE;
        this.textureRepetitions = this.size;
    }


    /**
     * Creates a new rectangle with the given vertex points and texture.
     *
     * @param p1 The first vertex.
     * @param p2 The second vertex.
     * @param p3 The third vertex.
     * @param p4 The fourth vertex.
     * @param texture the texture to apply to this rectangle.
     */
    public Rectangle(Point3D p1, Point3D p2, Point3D p3, Point3D p4,
            Texture texture) {
        this(p1, p2, p3, p4);

        this.texture = texture;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void render() {
        this.texture.bind();

        // Use getVerticesBuffer() instead of getVertices() in order to avoid
        // creating tons of Point3D that won't be necessary.
        FloatBuffer buffer = this.getVerticesBuffer();
        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex3d(buffer.get(0), buffer.get(1), buffer.get(2));

            GL11.glTexCoord2f(this.textureRepetitions.getWidth(), 0);
            GL11.glVertex3d(buffer.get(4), buffer.get(5), buffer.get(6));

            GL11.glTexCoord2f(this.textureRepetitions.getWidth(),
                    this.textureRepetitions.getHeight());
            GL11.glVertex3d(buffer.get(8), buffer.get(9), buffer.get(10));

            GL11.glTexCoord2f(0, this.textureRepetitions.getHeight());
            GL11.glVertex3d(buffer.get(12), buffer.get(13), buffer.get(14));
        GL11.glEnd();

        this.texture.unbind();
    }


    /**
     * @return Center location.
     */
    public Point3D getCenter() {
        return this.center;
    }


    /**
     * Alters the center location.
     *
     * @param center The new center location.
     */
    public void setCenter(Point3D center) {
        this.center = center;
    }


    /**
     * @return The vertices of this rectangle.
     */
    public Point3D[] getVertices() {
        FloatBuffer buffer = this.getVerticesBuffer();

        return new Point3D[] {
             new Point3D(buffer.get(0), buffer.get(1), buffer.get(2)),
             new Point3D(buffer.get(4), buffer.get(5), buffer.get(6)),
             new Point3D(buffer.get(8), buffer.get(9), buffer.get(10)),
             new Point3D(buffer.get(12), buffer.get(13), buffer.get(14))
        };
    }


    /**
     * @return The vertices of the rectangle, as a FloatBuffer, for faster
     * rendering.
     */
    private FloatBuffer getVerticesBuffer() {
        // The rectangle is created with the normal (0, 0, 1). Thus, if the
        // normal is now this.normal, a rotation had to be performed.
        // In order to find the axe of this rotation one can use the cross
        // product:
        //
        // axe = initial-normal x normal
        //
        // Also, the angle of the rotation is just the angle between both
        // normals.
        Vector3D initialNormal = new Vector3D(0, 0, 1);
        Vector3D rotationAxe = initialNormal.cross(this.normal);
        double rotationAngle = 0;
        try {
            rotationAngle = initialNormal.angleWith(this.normal);
        } catch (NullVectorException e) {
            // This is not going to happen!
            e.printStackTrace();
        }

        // Setup the original vertices of the rectangle, before any
        // transformations are applied.
        float[] points = new float[] {
                -size.getWidth() / 2.0f, -size.getHeight() / 2.0f, 0, 1,
                 size.getWidth() / 2.0f, -size.getHeight() / 2.0f, 0, 1,
                 size.getWidth() / 2.0f,  size.getHeight() / 2.0f, 0, 1,
                -size.getWidth() / 2.0f,  size.getHeight() / 2.0f, 0, 1,
        };

        FloatBuffer buffer = FloatBuffer.wrap(points);

        // Now, let's use OpenGL routines to transform the vertices for us :-)
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
            GL11.glLoadIdentity();

            // Finally, move to the center away from the origin
            GL11.glTranslated(center.getX(), center.getY(), center.getZ());

            // Now rotate by this.rotation around the normal vector
            GL11.glRotatef((float) Angle.toDegrees(this.rotation),
                    (float) normal.getX(),
                    (float) normal.getY(),
                    (float) normal.getZ());

            // First rotate rotationAngle around rotationAxe
            GL11.glRotatef((float) Angle.toDegrees((float)rotationAngle),
                    (float) rotationAxe.getX(),
                    (float) rotationAxe.getY(),
                    (float) rotationAxe.getZ());

            // Transform the corners.
            buffer = TransformationUtil.transformPoints(buffer,
                    GL11.GL_MODELVIEW_MATRIX);
        GL11.glPopMatrix();

        return buffer;
    }


    /**
     * @return The edge sizes.
     */
    public Size getSize() {
        return this.size;
    }

    /**
     * Sets the edge's sizes.
     *
     * @param size The new size of the edges of this rectangle.
     */
    public void setSize(Size size) {
        this.size = size;
    }


    /**
     * @return The rotation angle around the normal.
     */
    public float getRotation() {
        return this.rotation;
    }


    /**
     * Sets the rotation angle of this rectangle.
     *
     * @param rotation The new rotation angle.
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }


    /**
     * Increments the rotation around the normal axe.
     *
     * @param amount Amount of rotation to add.
     */
    public void rotateBy(double amount) {
        this.rotation += amount;
    }


    /**
     * @return the rectangle's normal..
     */
    public Vector3D getNormal() {
        return this.normal;
    }


    /**
     * Sets the normal of the rectangle.
     *
     * @param normal The new normal.
     */
    public void setNormal(Vector3D normal) {
        this.normal = normal;
    }


    /**
     * @return the rectangle's texture.
     */
    public Texture getTexture() {
        return this.texture;
    }


    /**
     * Sets a new texture for this rectangle.
     *
     * @param texture the new texture to use.
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }


    /**
     * @return the number of texture repetitions along the width and height
     * of this rectangle.
     */
    public Size getTextureRepetitions() {
        return this.textureRepetitions;
    }


    /**
     * Sets the number of texture repetitions along the width and height
     * of this rectangle.
     *
     * @param textureRepetitions The new number of texture repetitions.
     */
    public void setTextureRepetitions(Size textureRepetitions) {
        this.textureRepetitions = textureRepetitions;
    }


    /** Edge sizes. */
    private Size size;

    /** Center location. */
    private Point3D center;

    /** Normal of the plane the rectangle is in. */
    private Vector3D normal;

    /** Rotation angle around its normal. */
    private float rotation;

    /** Rectangle's texture. */
    private Texture texture;

    /** Texture repetitions along its width and height. */
    private Size textureRepetitions;
}
