package viglaglepeopgle.md3;

import java.nio.FloatBuffer;

/**
 * Represents an md3 frame.
 */
public class Frame {

    /**
     * Creates a new md3 frame with the given vertices.
     *
     * @param vertices Vertices stored inside this frame.
     */
    public Frame(FloatBuffer vertices) {
        this.vertices = vertices;
    }


    /**
     * @return the vertices stored inside this frame.
     */
    public FloatBuffer getVertices() {
        return this.vertices;
    }


    /** Vertices stored inside this frame. */
    private FloatBuffer vertices;
}
