package viglaglepeopgle.renderable;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import viglaglepeopgle.util.Angle;
import viglaglepeopgle.util.Texture;
import viglaglepeopgle.util.TransformationUtil;
import explicitlib.geometry.NullVectorException;
import explicitlib.geometry.Point3D;
import explicitlib.geometry.Size;
import explicitlib.geometry.Size3D;
import explicitlib.geometry.Vector3D;

/**
 * A box.
 */
public class Box implements Renderable {

    /** Order of box's faces. */
    public enum Face {
        FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM
    }

    /**
     * Creates a new box with the given attributes.
     *
     * @param size Edge sizes.
     * @param center Center location.
     * @param normal Normal of the plane the rectangle is in.
     */
    public Box(Size3D size, Point3D center, Vector3D normal) {
        this.size = size;
        this.center = center;
        this.normal = normal.normalize();
        this.rotation = 0;
        this.textures = new Texture[] {
                Texture.NULL_TEXTURE,
                Texture.NULL_TEXTURE,
                Texture.NULL_TEXTURE,
                Texture.NULL_TEXTURE,
                Texture.NULL_TEXTURE,
                Texture.NULL_TEXTURE
        };

        this.textureRepetitions = new Size[] {
                new Size(this.size.getWidth(), this.size.getHeight()), // Front
                new Size(this.size.getWidth(), this.size.getHeight()), // Back

                new Size(this.size.getDepth(), this.size.getHeight()), // Left
                new Size(this.size.getDepth(), this.size.getHeight()), // Right

                new Size(this.size.getWidth(), this.size.getDepth()),  // Top
                new Size(this.size.getWidth(), this.size.getDepth())   // Bottom
        };
    }


    /**
     * Creates a new box with the given attributes.
     *
     * @param size Edge sizes.
     * @param center Center location.
     * @param normal Normal of the plane the rectangle is in.
     * @param texture Box's texture.
     */
    public Box(Size3D size, Point3D center, Vector3D normal, Texture texture) {
        this(size, center, normal);

        this.textures = new Texture[] {
                texture,
                texture,
                texture,
                texture,
                texture,
                texture,
        };
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void render() {
        // Use getVerticesBuffer() instead of getVertices() in order to avoid
        // creating tons of Point3D that won't be necessary.
        FloatBuffer buffer = this.getVerticesBuffer();

        // Store current color
        float[] prevColor = new float[16];
        GL11.glGetFloat(GL11.GL_CURRENT_COLOR, FloatBuffer.wrap(prevColor));
        
        GL11.glColor3f(this.color[0], this.color[1], this.color[2]);
        
        // Front face
        this.textures[Face.FRONT.ordinal()].bind();
        Size repeat = this.textureRepetitions[Face.FRONT.ordinal()];
        GL11.glBegin(GL11.GL_QUADS);        	
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex3d(buffer.get(0), buffer.get(1), buffer.get(2));

            GL11.glTexCoord2f(repeat.getWidth(), 0);
            GL11.glVertex3d(buffer.get(4), buffer.get(5), buffer.get(6));

            GL11.glTexCoord2f(repeat.getWidth(), repeat.getHeight());
            GL11.glVertex3d(buffer.get(8), buffer.get(9), buffer.get(10));            
            
            GL11.glTexCoord2f(0, repeat.getHeight());
            GL11.glVertex3d(buffer.get(12), buffer.get(13), buffer.get(14));
        GL11.glEnd();

        // Back face
        this.textures[Face.BACK.ordinal()].bind();
        repeat = this.textureRepetitions[Face.BACK.ordinal()];
        GL11.glBegin(GL11.GL_QUADS);        	
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex3d(buffer.get(16), buffer.get(17), buffer.get(18));

            GL11.glTexCoord2f(repeat.getWidth(), 0);
            GL11.glVertex3d(buffer.get(20), buffer.get(21), buffer.get(22));

            GL11.glTexCoord2f(repeat.getWidth(), repeat.getHeight());
            GL11.glVertex3d(buffer.get(24), buffer.get(25), buffer.get(26));

            GL11.glTexCoord2f(0, repeat.getHeight());
            GL11.glVertex3d(buffer.get(28), buffer.get(29), buffer.get(30));
        GL11.glEnd();

        // Left face
        this.textures[Face.LEFT.ordinal()].bind();
        repeat = this.textureRepetitions[Face.LEFT.ordinal()];
        GL11.glBegin(GL11.GL_QUADS);        	
        	GL11.glTexCoord2f(0, 0);
            GL11.glVertex3d(buffer.get(16), buffer.get(17), buffer.get(18));

            GL11.glTexCoord2f(repeat.getWidth(), 0);
            GL11.glVertex3d(buffer.get(0), buffer.get(1), buffer.get(2));

            GL11.glTexCoord2f(repeat.getWidth(), repeat.getHeight());
            GL11.glVertex3d(buffer.get(12), buffer.get(13), buffer.get(14));

            GL11.glTexCoord2f(0, repeat.getHeight());
            GL11.glVertex3d(buffer.get(28), buffer.get(29), buffer.get(30));
        GL11.glEnd();

        // Right face
        this.textures[Face.RIGHT.ordinal()].bind();
        repeat = this.textureRepetitions[Face.RIGHT.ordinal()];
        GL11.glBegin(GL11.GL_QUADS);        	
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex3d(buffer.get(4), buffer.get(5), buffer.get(6));

            GL11.glTexCoord2f(repeat.getWidth(), 0);
            GL11.glVertex3d(buffer.get(20), buffer.get(21), buffer.get(22));

            GL11.glTexCoord2f(repeat.getWidth(), repeat.getHeight());
            GL11.glVertex3d(buffer.get(24), buffer.get(25), buffer.get(26));

            GL11.glTexCoord2f(0, repeat.getHeight());
            GL11.glVertex3d(buffer.get(8), buffer.get(9), buffer.get(10));
        GL11.glEnd();

        // Top face
        this.textures[Face.TOP.ordinal()].bind();
        repeat = this.textureRepetitions[Face.TOP.ordinal()];
        GL11.glBegin(GL11.GL_QUADS);        	
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex3d(buffer.get(12), buffer.get(13), buffer.get(14));

            GL11.glTexCoord2f(repeat.getWidth(), 0);
            GL11.glVertex3d(buffer.get(8), buffer.get(9), buffer.get(10));

            GL11.glTexCoord2f(repeat.getWidth(), repeat.getHeight());
            GL11.glVertex3d(buffer.get(24), buffer.get(25), buffer.get(26));

            GL11.glTexCoord2f(0, repeat.getHeight());
            GL11.glVertex3d(buffer.get(28), buffer.get(29), buffer.get(30));
        GL11.glEnd();

        // Bottom face
        this.textures[Face.BOTTOM.ordinal()].bind();
        repeat = this.textureRepetitions[Face.BOTTOM.ordinal()];
        GL11.glBegin(GL11.GL_QUADS);        	
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex3d(buffer.get(4), buffer.get(5), buffer.get(6));

            GL11.glTexCoord2f(repeat.getWidth(), 0);
            GL11.glVertex3d(buffer.get(0), buffer.get(1), buffer.get(2));

            GL11.glTexCoord2f(repeat.getWidth(), repeat.getHeight());
            GL11.glVertex3d(buffer.get(16), buffer.get(17), buffer.get(18));

            GL11.glTexCoord2f(0, repeat.getHeight());
            GL11.glVertex3d(buffer.get(20), buffer.get(21), buffer.get(22));        
            
        GL11.glEnd();

        this.textures[Face.BOTTOM.ordinal()].unbind();
        GL11.glColor3f(this.color[0], this.color[1], this.color[2]);
        
        GL11.glColor3f(prevColor[0], prevColor[1], prevColor[2]);
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
     * @return The vertices of this box.
     */
    public Point3D[] getVertices() {
        FloatBuffer buffer = this.getVerticesBuffer();

        return new Point3D[] {
             new Point3D(buffer.get(0), buffer.get(1), buffer.get(2)),
             new Point3D(buffer.get(4), buffer.get(5), buffer.get(6)),
             new Point3D(buffer.get(8), buffer.get(9), buffer.get(10)),
             new Point3D(buffer.get(12), buffer.get(13), buffer.get(14)),
             new Point3D(buffer.get(16), buffer.get(17), buffer.get(18)),
             new Point3D(buffer.get(20), buffer.get(21), buffer.get(22)),
             new Point3D(buffer.get(24), buffer.get(25), buffer.get(26)),
             new Point3D(buffer.get(28), buffer.get(29), buffer.get(30))
        };
    }


    /**
     * @return The rotation angle around the normal.
     */
    public float getRotation() {
        return this.rotation;
    }


    /**
     * Sets the rotation angle of this box.
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
     * @return Normal of the plane the box is in.
     */
    public Vector3D getNormal() {
        return this.normal;
    }


    /**
     * Sets the normal of the box.
     *
     * @param normal The new normal.
     */
    public void setNormal(Vector3D normal) {
        this.normal = normal;
    }


    /**
     * @return The vertices of the rectangle, as a FloatBuffer, for faster
     * rendering.
     */
    private FloatBuffer getVerticesBuffer() {
        // The box is created with the normal (0, 0, 1). Thus, if the
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

        // Setup the original vertices of the box, before any
        // transformations are applied.
        float halfWidth = size.getWidth() / 2.0f;
        float halfHeight = size.getHeight() / 2.0f;
        float halfDepth = size.getDepth() / 2.0f;
        float[] points = new float[] {
                // Front face
                -halfWidth, -halfHeight,  halfDepth, 1,
                 halfWidth, -halfHeight,  halfDepth, 1,
                 halfWidth,  halfHeight,  halfDepth, 1,
                -halfWidth,  halfHeight,  halfDepth, 1,

                // Back face
                -halfWidth, -halfHeight, -halfDepth, 1,
                 halfWidth, -halfHeight, -halfDepth, 1,
                 halfWidth,  halfHeight, -halfDepth, 1,
                -halfWidth,  halfHeight, -halfDepth, 1,
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

            // First, rotate rotationAngle around rotationAxe
            GL11.glRotatef((float) Angle.toDegrees((float)rotationAngle),
                    (float) rotationAxe.getX(),
                    (float) rotationAxe.getY(),
                    (float) rotationAxe.getZ());

            // Transform the vertices.
            buffer = TransformationUtil.transformPoints(buffer,
                    GL11.GL_MODELVIEW_MATRIX);
        GL11.glPopMatrix();

        return buffer;
    }


    /**
     * Returns the number of texture repetitions for a given face.
     *
     * @param face The corresponding face.
     * @return the number of texture repetitions for that face.
     */
    public Size getTextureRepetitions(Face face) {
        return this.textureRepetitions[face.ordinal()];
    }


    /**
     * Sets the number of texture repetitions for a given face in the box.
     *
     * @param face the face whose repetitions are about to be modified.
     * @param repeatX the number of repetitions along the face's width.
     * @param repeatY the number of repetitions along the face's height.
     */
    public void setTextureRepetitions(Face face, float repeatX, float repeatY) {
        this.textureRepetitions[face.ordinal()] = new Size(repeatX, repeatY);
    }


    /**
     * Returns the number of texture repetitions for a given face.
     *
     * @param face The corresponding face.
     * @return the number of texture repetitions for that face.
     */
    public Texture getTexture(Face face) {
        return this.textures[face.ordinal()];
    }


    /**
     * Sets the texture for a given face in the box.
     *
     * @param face the face whose texture is about to be modified.
     * @param texture the new texture for that face.
     */
    public void setTexture(Face face, Texture texture) {
        this.textures[face.ordinal()] = texture;
    }

    
    /**
     * Sets the box's color.
     *
     * @param color The new color.
     */
    public void setColor(float[] color) {
    	this.color = color;
    }

    /** Edge sizes. */
    private Size3D size;

    /** Center location. */
    private Point3D center;

    /** Normal of the plane the box is in. */
    private Vector3D normal;

    /** Rotation angle around its normal. */
    private float rotation;

    /** Box's textures (front, back, left, right, top and bottom). */
    private Texture[] textures;

    /** Texture repetitions for all the faces of this box. */
    private Size[] textureRepetitions;
    
    /** Box color. */
    private float[] color = new float[] { 1, 1, 1 };
}
