package viglaglepeopgle.display;

import org.lwjgl.opengl.GL11;

/**
 * This class abstracts the necessary view port OpenGL settings.
 */
public class ViewPort {

    /**
     * Creates a new ViewPort with the given settings.
     *
     * @param x Starting x screen coordinate.
     * @param y Starting y screen coordinate.
     * @param width View port's width.
     * @param height View port's height.
     */
    public ViewPort(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    /**
     * Prepares the OpenGL state machine for rendering with this viewport.
     */
    public void prepare() {
        GL11.glViewport(x, y, width, height);
    }


    /** Starting x screen coordinate. */
    private int x;

    /** Starting y screen coordinate. */
    private int y;

    /** View port's width. */
    private int width;

    /** View port's height. */
    private int height;
}
