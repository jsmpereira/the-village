package viglaglepeopgle.util;

/**
 * Contains utility functions related to angles.
 */
public final class Angle {

    /**
     * Converts from degrees to radians.
     *
     * @param degrees Angle in degrees to convert.
     * @return The corresponding angle in radians.
     */
    public static float toRadians(float degrees) {
        return (float)(degrees / 180 * Math.PI);
    }


    /**
     * Converts from radians to degrees.
     *
     * @param radians Angle in radians to convert.
     * @return The corresponding angle in degrees.
     */
    public static float toDegrees(float radians) {
        return (float)(radians * 180 / Math.PI);
    }


    /**
     * Hide the default constructor.
     */
    private Angle() {
    }
}
