package viglaglepeopgle.md3;

/**
 * Header of an MD3 file.
 */
public class Header {

    /** Length of the ID field, in bytes. */
    public static final int ID_LENGTH = 4;

    /** Expected ID. */
    public static final String EXPECTED_ID = "IDP3";

    /** Expected version. */
    public static final int EXPECTED_VERSION = 15;

    /** Length of the file name field, in bytes. */
    public static final int FILE_NAME_LENGTH = 68;


    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "Header: "
            + "\n  ID: " + this.id 
            + "\n  Version: " + this.version
            + "\n  File name: " + this.fileName
            + "\n  Number of frames: " + this.numFrames
            + "\n  Number of tags: " + this.numTags
            + "\n  Number of meshes: " + this.numMeshes
            + "\n  Max skins: " + this.maxSkins
            + "\n  Header size: " + this.headerSize
            + "\n  Tag offset: " + this.tagOffset
            + "\n  Mesh offset: " + this.meshOffset
            + "\n  File size: " + this.fileSize;
        
    }


    /**
     * @return the id (must be "IDP3").
     */
    public String getId() {
        return id;
    }


    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * @return the version of the md3 file (must be 15).
     */
    public int getVersion() {
        return version;
    }


    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }


    /**
     * @return the name of the model file.
     */
    public String getFileName() {
        return fileName;
    }


    /**
     * @param fileName the file name to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    /**
     * @return the number of animation keyframes
     */
    public int getNumFrames() {
        return numFrames;
    }


    /**
     * @param numFrames the number of animation keyframes to set
     */
    public void setNumFrames(int numFrames) {
        this.numFrames = numFrames;
    }


    /**
     * @return the number of tags included in the file.
     */
    public int getNumTags() {
        return numTags;
    }


    /**
     * @param numTags the new number of tags included in the file.
     */
    public void setNumTags(int numTags) {
        this.numTags = numTags;
    }


    /**
     * @return the number of meshes included in the file.
     */
    public int getNumMeshes() {
        return numMeshes;
    }


    /**
     * @param numMeshes the new number of meshes included in the file.
     */
    public void setNumMeshes(int numMeshes) {
        this.numMeshes = numMeshes;
    }


    /**
     * @return maximum number of skins for the model.
     */
    public int getMaxSkins() {
        return maxSkins;
    }


    /**
     * @param maxSkins the new maximum number of skins for the model.
     */
    public void setMaxSkins(int maxSkins) {
        this.maxSkins = maxSkins;
    }


    /**
     * @return the size of this header inside the file.
     */
    public int getHeaderSize() {
        return headerSize;
    }


    /**
     * @param headerSize the new size of the header.
     */
    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }


    /**
     * @return the offset for the "tags" section.
     */
    public int getTagOffset() {
        return tagOffset;
    }


    /**
     * @param tagOffset the new offset for the "tags" section.
     */
    public void setTagOffset(int tagOffset) {
        this.tagOffset = tagOffset;
    }


    /**
     * @return the offset for the "meshes" section.
     */
    public int getMeshOffset() {
        return meshOffset;
    }


    /**
     * @param meshOffset the offset for the "meshes" section.
     */
    public void setMeshOffset(int meshOffset) {
        this.meshOffset = meshOffset;
    }


    /**
     * @return the size of the model file.
     */
    public int getFileSize() {
        return fileSize;
    }


    /**
     * @param fileSize the size of the model file to set.
     */
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }


    /** Must be "IDP3". */
    private String id;

    /** Must be 3. */
    private int version;

    /** Name of this file. */
    private String fileName;

    /** Number of animation keyframes. */
    private int numFrames;

    /** Number of tags. */
    private int numTags;

    /** Number of meshes included in this model. */
    private int numMeshes;

    /** Maximum number of skins for the model. */
    private int maxSkins;

    /** Size of this header. */
    private int headerSize;

    /** Offset for the "tags" section. */
    private int tagOffset;

    /** Offset for the "meshes" section. */
    private int meshOffset;

    /** Total size of this file. */
    private int fileSize;
}
