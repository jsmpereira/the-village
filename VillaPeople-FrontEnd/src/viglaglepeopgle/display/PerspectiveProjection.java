package viglaglepeopgle.display;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.GLU;

/**
 * Perspective projection.
 */
public class PerspectiveProjection implements Projection {

    /**
     * Creates a new PerspectiveProjection with the given settings.
     *
     * @param fovy Field of view angle (in degrees).
     * @param aspectRatio Aspect ratio.
     * @param zNear Distance from the viewer to the near clipping plane.
     * @param zFar Distance from the viewer to the far clipping plane.
     */
    public PerspectiveProjection(float fovy, float aspectRatio,
            float zNear, float zFar) {
        this.fovy = fovy;
        this.aspectRatio = aspectRatio;
        this.zNear = zNear;
        this.zFar = zFar;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(fovy, aspectRatio, zNear, zFar);
    }

    /** Field of view angle (in degrees), in the y direction. */
    private float fovy;

    /** Aspect ratio that determines the field of view in the x direction. */
    private float aspectRatio;

    /** Specifies the distance from the viewer to the near clipping plane. */
    private float zNear;

    /** Specifies the distance from the viewer to the far clipping plane. */
    private float zFar;
}
