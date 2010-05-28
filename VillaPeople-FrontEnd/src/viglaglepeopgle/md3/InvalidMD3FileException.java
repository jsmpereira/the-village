package viglaglepeopgle.md3;

/**
 * Exception thrown when reading an invalid md3 file.
 */
public class InvalidMD3FileException extends Exception {

    /**
     * Creates a new Invalid md3 file exception.
     *
     * @param fileName The name of the md3 file.
     * @param reason The reason that explains why the file isn't valid.
     */
    public InvalidMD3FileException(String fileName, String reason) {
        super(fileName + " is not a valid md3 file: " + reason);
    }
}
