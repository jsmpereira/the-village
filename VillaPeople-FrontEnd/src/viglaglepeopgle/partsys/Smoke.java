package viglaglepeopgle.partsys;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import viglaglepeopgle.display.Observer;
import viglaglepeopgle.settings.Resources;
import viglaglepeopgle.util.Angle;
import viglaglepeopgle.util.Texture;
import viglaglepeopgle.util.TransformationUtil;
import explicitlib.geometry.NullVectorException;
import explicitlib.geometry.Vector3D;


/**
 * Simple particle system to simulate smoke.
 *
 * The particles are kept in a circular buffer optimized for performance.
 */
public class Smoke {

    /** Maximum number of particles per particle group. */
    public static final int MAX_PARTICLES = 300;

    /** Maximum size of a single particle. */
    public static final float MAX_SIZE = 0.3f;

    /** Maximum rotation increment of a single particle. */
    public static final float MAX_ROTATION_STEP = 0.2f;

    /** Maximum value of the alpha component of the particle's color. */
    public static final float MAX_ALPHA = 0.53f;

    /** Maximum value of the alpha component of the particle's color. */
    public static final float MIN_ALPHA = 0.18f;

    /** Rate of decay of the alpha component of the particle's color. */
    public static final float ALPHA_DECAY = 0.1f;

    /** Index of the x coordinate inside the particles array. */
    private static final int X_INDEX = 0;

    /** Index of the y coordinate inside the particles array. */
    private static final int Y_INDEX = 1;

    /** Index of the z coordinate inside the particles array. */
    private static final int Z_INDEX = 2;

    /** Index of the particle's size inside the particles array. */
    private static final int SIZE_INDEX = 3;

    /**
     * Index of the particle's rotation angle increment inside the particles
     * array.
     */
    private static final int ROTATION_STEP_INDEX = 4;

    /** Index of the particle's rotation angle inside the particles array. */
    private static final int ANGLE_INDEX = 5;

    /**
     * Index of the particle's alpha color component inside the particles array.
     */
    private static final int ALPHA_INDEX = 6;


    /**
     * Creates smoke.
     */
    public Smoke() {
        // Preinitialize the particles array
        this.particles = new float[MAX_PARTICLES][ALPHA_INDEX + 1];

        this.start = 0;
        this.end = 0;
        this.texture = Resources.getTexture("smoke");
    }


    /**
     * Adds a new smoke puff to the group.
     *
     * @param x The x coordinate of the puff's location.
     * @param y The y coordinate of the puff's location.
     * @param z The z coordinate of the puff's location.
     */
    public void add(float x, float y, float z) {
        float[] particle = particles[end];
        particle[X_INDEX] = x;
        particle[Y_INDEX] = y;
        particle[Z_INDEX] = z;
        particle[SIZE_INDEX] = (float) (float) (Math.random() * MAX_SIZE);
        particle[ROTATION_STEP_INDEX] = (float) (Math.random()
                * MAX_ROTATION_STEP - MAX_ROTATION_STEP / 2.0);
        particle[ALPHA_INDEX] = (float) (Math.random()
                * (MAX_ALPHA - MIN_ALPHA) + MIN_ALPHA);
        end++;

        // Circular buffer management
        if (end >= MAX_PARTICLES) {
            end = 0;
        }

        if (end <= start) {
            start++;
            assert end < start;
            
            if (start >= MAX_PARTICLES) {
                start = 0;
            }
        }
    }


    /**
     * Renders the smoke.
     *
     * @param observer
     */
    public void render(Observer observer) {
        this.normal = observer.getHeading();

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        this.texture.bind();

        assert start != end;
        if (start < end) {
            for (int i = start; i < end; i++) {
                renderParticle(this.particles[i]);
            }
        } else if (start > end) {
            assert start == end + 1 : start + " " + end;

            for (int i = 0; i < MAX_PARTICLES; i++) {
                renderParticle(this.particles[i]);
            }
        }

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.texture.unbind();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }


    /**
     * Renders a single particle.
     *
     * Note: The particle may not be rendered, if its alpha component is to
     * low. 
     *
     * @param particle the properties of the particle to render.
     * @return true if the particle was rendered, false otherwise.
     */
    private boolean renderParticle(float[] particle) {
        particle[ALPHA_INDEX] *= (1 - ALPHA_DECAY);

        // Rotate the particle
        particle[ANGLE_INDEX] += particle[ROTATION_STEP_INDEX];
        
        FloatBuffer buffer = this.getVerticesBuffer(particle);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, particle[ALPHA_INDEX]);
        
        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex3d(buffer.get(0), buffer.get(1), buffer.get(2));

            GL11.glTexCoord2f(1, 0);
            GL11.glVertex3d(buffer.get(4), buffer.get(5), buffer.get(6));

            GL11.glTexCoord2f(1, 1);
            GL11.glVertex3d(buffer.get(8), buffer.get(9), buffer.get(10));

            GL11.glTexCoord2f(0, 1);
            GL11.glVertex3d(buffer.get(12), buffer.get(13), buffer.get(14));
        GL11.glEnd();
        
        return true;
    }


    /**
     * Calculates the positions of the vertices of a single particle, after
     * applying all the transformations.
     * 
     * @param particle the properties of the particle.
     * @return The vertices of the particle, as a FloatBuffer, for faster
     * rendering.
     */
    private FloatBuffer getVerticesBuffer(float[] particle) {
        float size = particle[SIZE_INDEX];
        
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
                -size / 2.0f, -size / 2.0f, 0, 1,
                 size / 2.0f, -size / 2.0f, 0, 1,
                 size / 2.0f,  size / 2.0f, 0, 1,
                -size / 2.0f,  size / 2.0f, 0, 1,
        };

        FloatBuffer buffer = FloatBuffer.wrap(points);

        // Now, let's use OpenGL routines to transform the vertices for us :-)
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
            GL11.glLoadIdentity();

            // Finally, move to the center away from the origin
            GL11.glTranslatef(particle[X_INDEX], particle[Y_INDEX],
                    particle[Z_INDEX]);

            // Now rotate by this.rotation around the normal vector
            GL11.glRotatef((float) Angle.toDegrees(particle[ANGLE_INDEX]),
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
     * Circular buffer with all the properties of all the existing particles.
     * 
     *  Properties:
     *  - x, y, z coordinates;
     *  - square size;
     *  - angle of rotation;
     *  - increment of the angle of rotation;
     *  - alpha component of the particle's color.
     */
    private float[][] particles;

    /** Index of the first particle inside the circular buffer. */
    private int start;

    /** Index of the last particle inside the circular buffer. */
    private int end;
    private Vector3D normal;
    
    /** Particle's texture. */
    private Texture texture;
}
