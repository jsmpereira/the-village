package viglaglepeopgle.md3;


/**
 * Represents a TAG inside an MD3 file.
 */
public class Tag {

    /** Length of the tag name field, in bytes. */
    public static final int NAME_LENGTH = 64;


    /**
     * @return the name of the tag.
     */
    public String getName() {
        return name;
    }

    
    /**
     * @param name the new name of the tag.
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /**
     * @return the position of this tag.
     */
    public float[] getPosition() {
        return position;
    }

    
    /**
     * @param position the new position of the tag.
     */
    public void setPosition(float[] position) {
        this.position = position;
    }

    
    /**
     * @return the tag's rotation matrix.
     */
    public float[][] getRotationMatrix() {
        return rotationMatrix;
    }

    
    /**
     * @param rotationMatrix the new tag's rotation matrix.
     */
    public void setRotationMatrix(float[][] rotationMatrix) {
        this.rotationMatrix = rotationMatrix;
    }


    /** Tag identifier. */
    private String name;
    
    /** Position of the tag. */
    private float[] position;
    
    /** Tag's rotation matrix. */
    private float[][] rotationMatrix;
}
