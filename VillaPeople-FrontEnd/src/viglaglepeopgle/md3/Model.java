package viglaglepeopgle.md3;

import java.io.File;
import java.io.IOException;

/**
 * An .MD3 model.
 */
public final class Model {

    /**
     * Parses an .md3 file and returns the corresponding model.
     *
     * @param fileName The name of the md3 file to parse.
     * @return The model contained inside the file.
     * @throws IOException when some I/O error occurs when opening or reading
     * the file.
     * @throws InvalidMD3FileException if the file is not a valid md3 file.
     */
    public static Model parse(String fileName)
        throws IOException, InvalidMD3FileException {
        return new Model(fileName);
    }


    /**
     * @return the MD3 file header.
     */
    public Header getHeader() {
        return header;
    }


    /**
     * @return the tags included in the model.
     */
    public Tag[] getTags() {
        return tags;
    }


    /**
     * @return the meshes included in this model.
     */
    public Mesh[] getMeshes() {
        return meshes;
    }


    /**
     * Private constructor: use the static parse() method to read md3 files.
     *
     * @param fileName The name of the md3 file to parse.
     * @throws IOException when some I/O error occurs when opening or reading
     * the file.
     * @throws InvalidMD3FileException if the file is not a valid md3 file.
     */
    private Model(String fileName)
        throws IOException, InvalidMD3FileException {
        LittleEndianRandomAccessFile in =
            new LittleEndianRandomAccessFile(fileName, "r");

        this.fileName = fileName;
        this.header = this.readHeader(in);

        if (new File(fileName).length() != this.header.getFileSize()) {
            throw new InvalidMD3FileException(fileName, "Wrong file size");
        }

        this.tags = this.readTags(in);
        this.meshes = this.readMeshes(in);
    }


    /**
     * Reads the header of the md3 file.
     *
     * @param in The file reader.
     * @return The header.
     * @throws IOException when some I/O error occurs when reading the file.
     * @throws InvalidMD3FileException if the file is not a valid md3 file.
     */
    private Header readHeader(LittleEndianRandomAccessFile in)
    throws IOException, InvalidMD3FileException {
        Header header = new Header();

        byte[] id = new byte[Header.ID_LENGTH];
        in.readFully(id);
        header.setId(new String(id));
        header.setVersion(in.readInt());


        if (!header.getId().equals(Header.EXPECTED_ID)
                || header.getVersion() != Header.EXPECTED_VERSION) {
            throw new InvalidMD3FileException(this.fileName,
                    "ID = " + header.getId()
                    + ", version = " + header.getVersion());
        }

        byte[] fileName = new byte[Header.FILE_NAME_LENGTH];
        in.readFully(fileName);
        header.setFileName(new String(fileName));

        header.setNumFrames(in.readInt());
        header.setNumTags(in.readInt());
        header.setNumMeshes(in.readInt());
        header.setMaxSkins(in.readInt());
        header.setHeaderSize(in.readInt());
        header.setTagOffset(in.readInt());
        header.setMeshOffset(in.readInt());
        header.setFileSize(in.readInt());

        return header;
    }


    /**
     * Reads the tags section of the md3 file.
     *
     * @param in The file reader.
     * @return An array with all the tags inside the file.
     * @throws IOException when some I/O error occurs when reading the file.
     */
    private Tag[] readTags(LittleEndianRandomAccessFile in)
    throws IOException {
        Tag[] tags = new Tag[header.getNumTags() * header.getNumFrames()];
        in.seek(this.header.getTagOffset());

        for (int i = 0; i < tags.length; i++) {
            tags[i] = new Tag();

            byte[] tagName = new byte[Tag.NAME_LENGTH];
            in.readFully(tagName);
            tags[i].setName(new String(tagName));

            tags[i].setPosition(new float[] {
               in.readFloat(),
               in.readFloat(),
               in.readFloat()
            });

            tags[i].setRotationMatrix(new float[][] {
                    new float[] {
                            in.readFloat(),
                            in.readFloat(),
                            in.readFloat()
                    },
                    new float[] {
                            in.readFloat(),
                            in.readFloat(),
                            in.readFloat()
                    },
                    new float[] {
                            in.readFloat(),
                            in.readFloat(),
                            in.readFloat()
                    },
            });
        }
        return tags;
    }


    /**
     * Reads the meshes section of the md3 file.
     *
     * @param in The file reader.
     * @return An array with all the meshes inside the file.
     * @throws IOException when some I/O error occurs when reading the file.
     */
    private Mesh[] readMeshes(LittleEndianRandomAccessFile in)
        throws IOException {
        Mesh[] meshes = new Mesh[header.getNumMeshes()];
        long offsetSoFar = this.header.getMeshOffset();

        for (int i = 0; i < meshes.length; i++) {
            in.seek(offsetSoFar);
            meshes[i] = Mesh.parse(in);
            offsetSoFar += meshes[i].getHeader().getMeshSize();
        }

        return meshes;
    }


    /** File name. */
    private String fileName;


    /** MD3 file header. */
    private Header header;


    /** Tags included in the file. */
    private Tag[] tags;


    /** Meshes included in the file. */
    private Mesh[] meshes;
}
