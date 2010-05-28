package viglaglepeopgle.renderable;

import java.util.Map;

import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import viglaglepeopgle.md3.Frame;
import viglaglepeopgle.md3.Mesh;
import viglaglepeopgle.md3.Model;
import viglaglepeopgle.settings.Animation;
import viglaglepeopgle.settings.ModelConfiguration;
import viglaglepeopgle.settings.Resources;
import viglaglepeopgle.util.Angle;
import viglaglepeopgle.util.Texture;
import explicitlib.geometry.NullVectorException;
import explicitlib.geometry.Point3D;
import explicitlib.geometry.Size3D;
import explicitlib.geometry.Vector3D;

/**
 * This renderer is able to render MD3 models.
 */
public class ModelRenderer implements Renderable {

    /**
     * Creates a model renderer that will render the given model.
     *
     * @param model The MD3 model to render.
     * @param location Model location.
     * @param scale Scaling factor to apply to the original model.
     * @param initialNormal Default normal, or above vector of the model.
     * @param textures Model textures (one per mesh).
     * @param animations Animations defined for this model.
     */
    public ModelRenderer(Model model, Point3D location, float scale,
            Vector3D initialNormal, Texture[] textures,
            Map<String, Animation> animations) {
        this.model = model;
        this.location = location;
        this.scale = scale;
        this.textures = textures;
        this.initialNormal = initialNormal;
        this.normal = new Vector3D(0, 1, 0);
        this.animations = animations;

        if (model.getMeshes().length != this.textures.length) {
            throw new IllegalArgumentException("Number of meshes must be equal"
                    + " to the number of textures");
        }

        this.currentAnimation = new Animation(0,
                model.getHeader().getNumFrames() - 1);
        this.updateInitialRotationParameters();            
    }


    /**
     * Creates a new model renderer from the given model configuration.
     *
     * @param config the configuration parameters of the model.
     * @param location model location.
     */
    public ModelRenderer(ModelConfiguration config, Point3D location) {
        this(config.getModel(), location, config.getScale(),
                config.getInitialNormal(), config.getTextures(),
                config.getAnimations());
       
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void render() {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();

            // Finally, move to the center away from the origin
            GL11.glTranslated(location.getX(), location.getY(),
                    location.getZ());

            // Now rotate by this.rotation around the normal vector
            GL11.glRotatef((float) Angle.toDegrees(this.rotation),
                    (float) normal.getX(),
                    (float) normal.getY(),
                    (float) normal.getZ());

            // Perform the initial rotation.
            GL11.glRotatef(initialRotationAngle, initialRotationAxe[0],
                    initialRotationAxe[1], initialRotationAxe[2]);

            // First, scale the model.
            GL11.glScalef(scale, scale, scale);

            for (int i = 0; i < model.getMeshes().length; i++) {
                Mesh mesh = model.getMeshes()[i];
                Frame currentFrame = mesh.getFrames()[(int) this.frame];

                this.textures[i].bind();

                GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
                GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

                GL11.glVertexPointer(3, 0, currentFrame.getVertices());
                GL11.glTexCoordPointer(2, 0, mesh.getTextureCoordinates());
                GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getTriangles());

                this.textures[i].unbind();
            }            
        GL11.glPopMatrix();        
    }


    /**
     * Advances the animation.
     *
     * @param elapsedTime The time elapsed since the last step.
     */
    public void step(long elapsedTime) {
        float previousFrame = this.frame;
        int firstFrame = this.currentAnimation.getFirstFrame();
        int lastFrame = this.currentAnimation.getLastFrame();
        int animationLength = lastFrame - firstFrame;

        float amount = elapsedTime / 1000.0f * this.fps;
        float nextFrame = this.frame + amount;

        // Go back to the beginning of the animation
        while (nextFrame > lastFrame) {
            nextFrame -= animationLength;
        }

        this.frame = nextFrame;

        assert frame >= firstFrame && frame <= lastFrame
            : "Previous frame: " + previousFrame
                + "\nFrame: " + frame
                + "\nAmount: " + amount
                + "\nFirst frame: " + firstFrame
                + "\nLast frame: " + lastFrame;
    }


    /**
     * @return the location of the model.
     */
    public Point3D getLocation() {
        return this.location;
    }


    /**
     * Alters the location of the model.
     *
     * @param location The new model location.
     */
    public void setLocation(Point3D location) {
        this.location = location;
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
     * Sets the current animation of the model.
     *
     * @param id The key of the animation to run.
     */
    public void setAnimation(String id) {
        Animation nextAnimation = this.animations.get(id);

        if (nextAnimation != this.currentAnimation) {
            this.frame = nextAnimation.getFirstFrame();
            this.currentAnimation = nextAnimation;
        }
    }


    /**
     * @return the number of frames-per-second to render.
     */
    public int getFps() {
        return this.fps;
    }


    /**
     * Sets the number of frames-per-second to render.
     * @param fps The new number of frames-per-second to render.
     */
    public void setFps(int fps) {
        this.fps = fps;
    }


    /**
     * Updates the parameters of the initial rotation that needs to be performed
     * in order to transform the original local coordinate system of the model,
     * to the coordinate system used in this application.
     */
    private void updateInitialRotationParameters() {
        // The box is created with the normal this.initialNormal. Thus, if the
        // normal is now this.normal, a rotation had to be performed.
        // In order to find the axe of this rotation one can use the cross
        // product:
        //
        // axe = initial-normal x normal
        //
        // Also, the angle of the rotation is just the angle between both
        // normals.
        Vector3D rotationAxe = initialNormal.cross(this.normal);
        double rotationAngle = 0;
        try {
            rotationAngle = initialNormal.angleWith(this.normal);
        } catch (NullVectorException e) {
            // This is not going to happen!
            e.printStackTrace();
        }

        this.initialRotationAxe = new float[] {
                (float) rotationAxe.getX(),
                (float) rotationAxe.getY(),
                (float) rotationAxe.getZ()
        };

        this.initialRotationAngle = (float) Angle.toDegrees((float)rotationAngle);
    }


    /** The MD3 model that this instance renders. */
    private Model model;

    /** Rotation angle around its normal. */
    private float rotation;

    /** Model location. */
    private Point3D location;

    /** Scaling factor to apply to the original model. */
    private float scale;

    /**
     * The axe of the rotation that needs to be performed in order to transform
     * the original local coordinate system of the model, to the coordinate
     * system used in this application, where the floor is the XZ plane and the
     * top direction is along the positive Y axis.
     */
    private float[] initialRotationAxe;

    /** The angle of the rotation performed around initialRotationAxe. */
    private float initialRotationAngle;

    /** Default normal, or above vector of the model. */
    private Vector3D initialNormal;

    /** Current normal, or above vector. */
    private Vector3D normal;

    /** Model textures (one per mesh). */
    private Texture[] textures;

    /** Animations defined for this model. */
    private Map<String, Animation> animations;

    /** The currently running animation. */
    private Animation currentAnimation;

    /** Frame to render. */
    private float frame;

    /** Number of frames-per-second to render. */
    private int fps = 30;
}
