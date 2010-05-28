package viglaglepeopgle.renderable;

import viglaglepeopgle.util.Texture;
import explicitlib.geometry.Point3D;
import explicitlib.geometry.Size;
import explicitlib.geometry.Vector3D;

/**
 * Rectangle with texture animations.
 */
public class AnimatedRectangle implements Renderable {

    /**
     * Creates a new animated rectangle with the given geometric attributes and
     * texture frames.
     *
     * @param size Edge sizes.
     * @param center Center location.
     * @param normal Normal of the plane the rectangle is in.
     * @param frames Animation frames.
     */
    public AnimatedRectangle(Size size, Point3D center, Vector3D normal,
            Texture[] frames) {
        this.rectangle = new Rectangle(size, center, normal);
        this.frames = frames;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void render() {
        if (this.frame < this.frames.length) {
            this.rectangle.setTexture(frames[(int) this.frame]);
        }

        this.rectangle.render();
    }


    /**
     * Advances the animation.
     *
     * @param elapsedTime The time elapsed since the last step.
     */
    public void step(long elapsedTime) {
        float amount = elapsedTime / 1000.0f * this.fps;
        this.frame += amount;
    }


    /**
     * Sets the normal of the rectangle.
     *
     * @param normal The new normal.
     */
    public void setNormal(Vector3D normal) {
        this.rectangle.setNormal(normal);
    }


    /**
     * Sets the number of texture repetitions along the width and height
     * of this rectangle.
     *
     * @param textureRepetitions The new number of texture repetitions.
     */
    public void setTextureRepetitions(Size textureRepetitions) {
        this.rectangle.setTextureRepetitions(textureRepetitions);
    }


    /** The rectangle itself. */
    private Rectangle rectangle;

    /** Animation frames. */
    private Texture[] frames;

    /** Current frame. */
    private float frame;

    /** Number of frames-per-second to render. */
    private int fps = 15;
}
