package viglaglepeopgle.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;


public class TransformationUtil {

    /** OpenGL matrices size. */
    private static final int MATRIX_SIZE = 16;

    /**
     * Applies a transformation to a given set of points, and returns the
     * transformed points. It assumes that the transformation matrix is
     * already loaded at the correct OpenGL matrix.
     *
     * Note: May modify the points parameter.
     *
     * @param points The points to transform.
     * @param matrix The OpenGL matrix that contains the transformation
     * (e.g., GL11.GL_MODELVIEW_MATRIX).
     * @return The transformed points.
     */
    public static FloatBuffer transformPoints(FloatBuffer points, int matrix) {
        if (points.remaining() % 4 != 0) {
            throw new IllegalArgumentException(points + " must have a number "
                    + "of elements that is a multiple of 4");
        }

        // Save the transformation matrix if necessary
        FloatBuffer transformation = null;
        if (points.remaining() > MATRIX_SIZE) {
            transformation = FloatBuffer.allocate(MATRIX_SIZE);
            GL11.glGetFloat(matrix, transformation);
        }

        // If the number of points is not divisible by 4, allocate dummy
        // points just for the multiplication.
        if (points.remaining() % MATRIX_SIZE != 0) {
            int newSize = (points.remaining() / MATRIX_SIZE) * MATRIX_SIZE
                + MATRIX_SIZE;

            assert newSize % MATRIX_SIZE == 0;
            FloatBuffer newBuffer = BufferUtils.createFloatBuffer(newSize);
            newBuffer.put(points);
            newBuffer.rewind();
            points = newBuffer;
        }

        while (points.hasRemaining()) {
            // Transform the points
            GL11.glMultMatrix(points);

            // Save the transformed points.
            GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, points);

            // Advance the buffer by MATRIX_SIZE positions.
            points.position(points.position() + MATRIX_SIZE);

            // More passes needed?
            if (points.hasRemaining()) {
                // Load the transformation matrix that was destroyed by the
                // last multiplication
                GL11.glLoadMatrix(transformation);
            }
        }

        points.rewind();
        return points;
    }
}
