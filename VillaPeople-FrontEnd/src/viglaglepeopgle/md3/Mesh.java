package viglaglepeopgle.md3;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


/**
 * Represents a mesh inside an MD3 file.
 */
public final class Mesh {

    /**
     * Reads a mesh from the md3 file.
     *
     * @param in The file reader.
     * @return The mesh.
     * @throws IOException when some I/O error occurs when reading the file.
     */
    public static Mesh parse(LittleEndianRandomAccessFile in)
        throws IOException {
        return new Mesh(in);
    }


    /**
     * @return The header for this mesh.
     */
    public MeshHeader getHeader() {
        return this.header;
    }


    /**
     * @return the frames included in this mesh.
     */
    public Frame[] getFrames() {
        return frames;
    }


    /**
     * @return the triangles that form this mesh.
     */
    public IntBuffer getTriangles() {
        return triangles;
    }


    /**
     * @return the texture coordinates for each vertex of this mesh.
     */
    public FloatBuffer getTextureCoordinates() {
        return textureCoordinates;
    }


    /**
     * @return the skins to texture this mesh.
     */
    public Skin[] getSkins() {
        return skins;
    }


    /**
     * Private constructor: use the static parse() method to read meshes.
     *
     * @param in The file reader.
     * @throws IOException when some I/O error occurs when opening or reading
     * the file.
     */
    private Mesh(LittleEndianRandomAccessFile in)
        throws IOException {

        this.meshOffset = in.getFilePointer();
        this.header = this.readHeader(in);
        this.skins = this.readSkins(in);
        this.frames = this.readFrames(in);
        this.triangles = this.readTriangles(in);
        this.textureCoordinates = this.readTextureCoordinates(in);
    }


    /**
     * Reads the header of this mesh.
     *
     * @param in The file reader.
     * @return The header.
     * @throws IOException when some I/O error occurs when reading the file.
     */
    private MeshHeader readHeader(LittleEndianRandomAccessFile in)
    throws IOException {
        MeshHeader header = new MeshHeader();

        byte[] meshId = new byte[MeshHeader.ID_LENGTH];
        in.readFully(meshId);
        header.setMeshId(new String(meshId));

        byte[] meshName = new byte[MeshHeader.NAME_LENGTH];
        in.readFully(meshName);
        header.setMeshName(new String(meshName));

        header.setNumMeshFrames(in.readInt());
        header.setNumSkins(in.readInt());
        header.setNumVertices(in.readInt());
        header.setNumTriangles(in.readInt());
        header.setTrianglesOffset(in.readInt());
        header.setSkinsOffset(in.readInt());
        header.setTextCoordsOffset(in.readInt());
        header.setVertexOffset(in.readInt());
        header.setMeshSize(in.readInt());

        return header;
    }


    /**
     * Reads the skins section for this mesh.
     *
     * @param in The file reader.
     * @return The skins.
     * @throws IOException when some I/O error occurs when reading the file.
     */
    private Skin[] readSkins(LittleEndianRandomAccessFile in)
        throws IOException {
        in.seek(this.meshOffset + this.header.getSkinsOffset());
        Skin[] skins = new Skin[this.header.getNumSkins()];
        for (int i = 0; i < skins.length; i++) {
            byte[] skinName = new byte[Skin.NAME_LENGTH];
            in.readFully(skinName);
            skins[i] = new Skin(new String(skinName));
        }

        return skins;
    }


    /**
     * Reads the frames section for this mesh.
     *
     * @param in The file reader.
     * @return The frames.
     * @throws IOException when some I/O error occurs when reading the file.
     */
    private Frame[] readFrames(LittleEndianRandomAccessFile in)
        throws IOException {
        in.seek(this.meshOffset + this.header.getVertexOffset());

        Frame[] frames = new Frame[this.header.getNumMeshFrames()];
        for (int i = 0; i < frames.length; i++) {
            FloatBuffer vertices = ByteBuffer.allocateDirect(
                    this.header.getNumVertices() * 3 * 4)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();

            final float divisorConstant = 64.0f;
            for (int j = 0; j < this.header.getNumVertices(); j++) {
                vertices.put(in.readShort() / divisorConstant);
                vertices.put(in.readShort() / divisorConstant);
                vertices.put(in.readShort() / divisorConstant);
                in.skipBytes(2);
            }

            vertices.flip();
            frames[i] = new Frame(vertices);
        }
        return frames;
    }


    /**
     * Reads the triangles section for this mesh.
     *
     * @param in The file reader.
     * @return The triangles.
     * @throws IOException when some I/O error occurs when reading the file.
     */
    private IntBuffer readTriangles(LittleEndianRandomAccessFile in)
        throws IOException {
        in.seek(this.meshOffset + this.header.getTrianglesOffset());
        IntBuffer triangles = ByteBuffer.allocateDirect(header.getNumTriangles()
                * 3 * 4).order(ByteOrder.nativeOrder()).asIntBuffer();

        for (int i = 0; i < triangles.capacity(); i++) {
            triangles.put(in.readInt());
        }

        triangles.flip();
        return triangles;
    }


    /**
     * Reads the texture coordinates for this mesh.
     *
     * @param in The file reader.
     * @return The texture coordinates.
     * @throws IOException when some I/O error occurs when reading the file.
     */
    private FloatBuffer readTextureCoordinates(
            LittleEndianRandomAccessFile in) throws IOException {
        in.seek(this.meshOffset + this.header.getTextCoordsOffset());
        FloatBuffer coordinates = ByteBuffer.allocateDirect(
                this.header.getNumVertices() * 2 * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        for (int i = 0; i < this.header.getNumVertices(); i++) {
            coordinates.put(in.readFloat());
            coordinates.put(in.readFloat());
        }
        
        coordinates.flip();
        return coordinates;
    }


    /** Mesh header. */
    private MeshHeader header;


    /** Frames for this mesh. */
    private Frame[] frames;

    /** The triangles that form this mesh. */
    private IntBuffer triangles;

    /** Texture coordinates for each vertex. */
    private FloatBuffer textureCoordinates;

    /** Skins to be used for this mesh. */
    private Skin[] skins;

    /** Offset of this mesh into the md3 file. */
    private long meshOffset;
}
