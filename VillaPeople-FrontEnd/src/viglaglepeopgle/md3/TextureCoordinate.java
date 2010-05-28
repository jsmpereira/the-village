package viglaglepeopgle.md3;

/**
 * Represents a texture coordinate in a MD3 file.
 */
public class TextureCoordinate {

    /**
     * Creates a new md3 texture coordinate with the given coordinates.
     *
     * @param u Texture's U coordinate.
     * @param v Texture's V coordinate.
     */
    public TextureCoordinate(float u, float v) {
        this.u = u;
        this.v = v;
    }


    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "MD3TextureCoordinate(" + u + ", " + v + ")";
    }


    /**
     * @return the U coordinate.
     */
    public float getU() {
        return u;
    }


    /**
     * @return the V coordinate.
     */
    public float getV() {
        return v;
    }


    /** Texture's U coordinate. */
    private float u;

    /** Texture's V coordinate. */
    private float v;
}
