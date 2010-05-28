package viglaglepeopgle.settings;

/**
 * Represents a model animation.
 */
public class Animation {

    /**
     * Creates a new animation with the given parameters.
     *
     * @param firstFrame the first frame of the animation.
     * @param lastFrame the last frame of the animation.
     */
    public Animation(int firstFrame, int lastFrame) {
        super();
        this.firstFrame = firstFrame;
        this.lastFrame = lastFrame;
    }


    /**
     * @return the first frame of the animation.
     */
    public int getFirstFrame() {
        return firstFrame;
    }


    /**
     * @return the last frame of the animation.
     */
    public int getLastFrame() {
        return lastFrame;
    }


    /** First frame of the animation. */
    private int firstFrame;

    /** Last frame of the animation. */
    private int lastFrame;
}
