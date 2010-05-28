package viglaglepeopgle.display;

import org.lwjgl.opengl.GL11;

/**
 * Orthogonal projection.
 */
public class OrthogonalProjection implements Projection {

    /**
     * Creates a new PerspectiveProjection with the given settings.
     *
     * @param fovy Field of view angle (in degrees).
     * @param aspectRatio Aspect ratio.
     * @param zNear Distance from the viewer to the near clipping plane.
     * @param zFar Distance from the viewer to the far clipping plane.
     */
    public OrthogonalProjection(float left, float right, float bottom,
            float top, float near, float far) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        this.near = near;
        this.far = far;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(left, right, bottom, top, near, far);
    }


    /** Field of view angle (in degrees), in the y direction. */
    private float left;

    /** Aspect ratio that determines the field of view in the x direction. */
    private float right;

    /** Specifies the distance from the viewer to the near clipping plane. */
    private float bottom;

    /** Specifies the distance from the viewer to the far clipping plane. */
    private float top;

    /** Specifies the distance from the viewer to the far clipping plane. */
    private float near;

    /** Specifies the distance from the viewer to the far clipping plane. */
    private float far;
}
