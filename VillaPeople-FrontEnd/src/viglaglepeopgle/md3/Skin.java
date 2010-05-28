package viglaglepeopgle.md3;


/**
 * An MD3 skin.
 */
public class Skin {

    /** Length of the mesh name field, in bytes. */
    public static final int NAME_LENGTH = 68;
    

    /**
     * Creates a new MD3Skin with the given file name.
     * 
     * @param name The name of the file where the texture is.
     */
    public Skin(String name) {
        this.name = name;
    }


    /**
     * @return The name of the file where the texture is. 
     */
    public String getName() {
        return this.name;
    }


    /** The name of the file where the texture is. */
    private String name;
}
