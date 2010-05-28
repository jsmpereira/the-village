package viglaglepeopgle.md3;


/**
 * Header of an MD3 mesh.
 */
public final class MeshHeader {

    /** Length of the mesh ID field, in bytes. */
    public static final int ID_LENGTH = 4;


    /** Length of the mesh name field, in bytes. */
    public static final int NAME_LENGTH = 68;


    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "Mesh Header: "
            + "\n  Mesh ID: " + this.meshId 
            + "\n  Mesh name: " + this.meshName
            + "\n  Number of keyframes: " + this.numMeshFrames
            + "\n  Number of skins: " + this.numSkins
            + "\n  Number of vertices: " + this.numVertices
            + "\n  Number of triangles: " + this.numTriangles
            + "\n  Triangles offset: " + this.trianglesOffset
            + "\n  Skins offset: " + this.skinsOffset
            + "\n  Texture coordinates offset: " + this.textCoordsOffset
            + "\n  Vertex offset: " + this.vertexOffset
            + "\n  Mesh size: " + this.meshSize;
        
    }


    /**
     * @return the mesh's Id.
     */
    public String getMeshId() {
        return meshId;
    }

    
    /**
     * @param meshId the new mesh Id.
     */
    public void setMeshId(String meshId) {
        this.meshId = meshId;
    }

    
    /**
     * @return the name of this mesh.
     */
    public String getMeshName() {
        return meshName;
    }

    
    /**
     * @param meshName the new name of the mesh.
     */
    public void setMeshName(String meshName) {
        this.meshName = meshName;
    }

    
    /**
     * @return the number of keyframes contained inside the mesh.
     */
    public int getNumMeshFrames() {
        return numMeshFrames;
    }

    
    /**
     * @param numMeshFrames the new number of keyframes.
     */
    public void setNumMeshFrames(int numMeshFrames) {
        this.numMeshFrames = numMeshFrames;
    }

    
    /**
     * @return the number of skins for this particular mesh.
     */
    public int getNumSkins() {
        return numSkins;
    }

    
    /**
     * @param numSkins the new number of skins.
     */
    public void setNumSkins(int numSkins) {
        this.numSkins = numSkins;
    }

    
    /**
     * @return the number of vertices that form this mesh.
     */
    public int getNumVertices() {
        return numVertices;
    }

    
    /**
     * @param numVertices the new number of vertices.
     */
    public void setNumVertices(int numVertices) {
        this.numVertices = numVertices;
    }

    
    /**
     * @return the number of triangles that form this mesh.
     */
    public int getNumTriangles() {
        return numTriangles;
    }

    
    /**
     * @param numTriangles the new number of triangles.
     */
    public void setNumTriangles(int numTriangles) {
        this.numTriangles = numTriangles;
    }

    
    /**
     * @return the offset for the triangles sub-section.
     */
    public int getTrianglesOffset() {
        return trianglesOffset;
    }

    
    /**
     * @param trianglesOffset the new offset for the triangles section.
     */
    public void setTrianglesOffset(int trianglesOffset) {
        this.trianglesOffset = trianglesOffset;
    }

    
    /**
     * @return the offset for the skins sub-section.
     */
    public int getSkinsOffset() {
        return skinsOffset;
    }

    
    /**
     * @param skinsOffset the new offset for the skins sub-section.
     */
    public void setSkinsOffset(int skinsOffset) {
        this.skinsOffset = skinsOffset;
    }

    
    /**
     * @return the offset for the texture coordinates section.
     */
    public int getTextCoordsOffset() {
        return textCoordsOffset;
    }

    
    /**
     * @param textCoordsOffset the new offset for the texture coordinates
     *        section.
     */
    public void setTextCoordsOffset(int textCoordsOffset) {
        this.textCoordsOffset = textCoordsOffset;
    }

    
    /**
     * @return the offset for the vertex section.
     */
    public int getVertexOffset() {
        return vertexOffset;
    }

    
    /**
     * @param vertexOffset the new offset for the vertex section.
     */
    public void setVertexOffset(int vertexOffset) {
        this.vertexOffset = vertexOffset;
    }

    
    /**
     * @return the mesh size, including the header.
     */
    public int getMeshSize() {
        return meshSize;
    }

    
    /**
     * @param meshSize the new size of the mesh.
     */
    public void setMeshSize(int meshSize) {
        this.meshSize = meshSize;
    }
    

    /** Mesh ID. */
    private String meshId;
    
    /** Mesh name. */
    private String meshName;
    
    /** Number of keyframes contained inside the mesh. */
    private int numMeshFrames;
    
    /** Number of skins for this particular mesh. */
    private int numSkins;
    
    /** Number of vertices that form this mesh. */
    private int numVertices;
    
    /** Number of triangles that form this mesh. */
    private int numTriangles;
    
    /** Offset for the triangles sub-section. */
    private int trianglesOffset;
    
    /** Offset for the skins sub-section. */
    private int skinsOffset;
    
    /** Offset for the texture coordinates sub-section. */
    private int textCoordsOffset;
    
    /** Offset for the vertex sub-section. */
    private int vertexOffset;
    
    /** Mesh size, including the header. */
    private int meshSize;
}
