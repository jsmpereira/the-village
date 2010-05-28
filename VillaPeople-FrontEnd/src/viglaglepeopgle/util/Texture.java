package viglaglepeopgle.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.devil.IL;
import org.lwjgl.devil.ILU;
import org.lwjgl.devil.ILUT;
import org.lwjgl.opengl.GL11;


/**
 * Utility class to represent a texture on OpenGL.
 */
public class Texture {

    /** Null texture, i.e., a dummy texture that does nothing. */
    public static final Texture NULL_TEXTURE = new Texture() {
        @Override
        public void bind() {
        }


        @Override
        public void unbind() {
        }
    };


    /**
     * Private default constructor, to be used by nullTexture.
     */
    private Texture() {
    }

    /**
     * Creates a new texture from the given file, and flips it if necessary.
     *
     * @param fileName The name of the image file.
     * @param flip If set, flips the image.
     * @throws IOException IOException when there is an I/O error reading the
     * image file.
     */
    public Texture(String fileName, boolean flip) throws IOException {
        this.load(fileName, flip);
    }


    /**
     * Binds the texture.
     */
    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.handle);        
    }


    /**
     * Unbinds the texture.
     */
    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }


    /**
     * Destroys this texture, reclaiming all its resources.
     */
    public void destroy() {
        IntBuffer scratch = BufferUtils.createIntBuffer(1);
        scratch.put(0, this.handle);
        GL11.glDeleteTextures(scratch);
    }


    /**
     * Renders the texture inside a square, for testing purposes.
     */
    public void render() {
        this.bind();
        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2i(0, 0);

            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2i(this.width, 0);

            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2i(this.width, this.height);

            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2i(0, this.height);
        GL11.glEnd();
    }


    /**
     * Loads a texture from the given file, and flips it if necessary.
     *
     * Note: Adapted from
     *   http://lwjgl.org/wiki/doku.php/lwjgl/tutorials/devil/loadingtextures
     *
     * @param fileName The name of the image file.
     * @param flip If set, flips the image.
     * @throws IOException when there is an I/O error reading the image file.
     */
    private void load(String fileName, boolean flip) throws IOException {
        try {
            IL.create();
            ILU.create();
            ILUT.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        ByteBuffer imageData = null;
        int ilImageHandle;
        IntBuffer scratch = BufferUtils.createIntBuffer(1);

        // Create image in DevIL and bind it
        IL.ilGenImages(scratch);
        IL.ilBindImage(scratch.get(0));
        ilImageHandle = scratch.get(0);

        // Load the image
        if (!IL.ilLoadImage(fileName)) {
            throw new IOException("Error loading the texture from " + fileName);
        }

        // Convert image to RGBA
        IL.ilConvertImage(IL.IL_RGBA, IL.IL_BYTE);

        // flip if needed
        if (flip) {
            ILU.iluFlipImage();
        }

        // Get image attributes
        int width = IL.ilGetInteger(IL.IL_IMAGE_WIDTH);
        int height = IL.ilGetInteger(IL.IL_IMAGE_HEIGHT);
        int textureWidthSize = getNextPowerOfTwo(width);
        int textureHeightSize = getNextPowerOfTwo(height);

        // Resize image to the next power of two number, if needed
        if (textureWidthSize != width || textureHeightSize != height) {
            imageData = BufferUtils.createByteBuffer(
                    textureWidthSize * textureHeightSize * 4);
            IL.ilCopyPixels(0, 0, 0, textureWidthSize, textureHeightSize,
                    1, IL.IL_RGBA, IL.IL_BYTE, imageData);
        } else {
            imageData = IL.ilGetData();
        }

        // Create OpenGL counterpart
        GL11.glGenTextures(scratch);
        int handle = scratch.get(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        
        /*
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_LINEAR);
                
                */
                
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureWidthSize,
                textureHeightSize, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
                imageData);

        this.handle = handle;
        // Create image (either resized by copying, else directly from IL)
        if (textureWidthSize != width || textureHeightSize != height) {
            this.width = textureWidthSize;
            this.height = textureHeightSize;
        } else {
            this.width = width;
            this.height = height;
        }

        // Delete Image in DevIL
        scratch.put(0, ilImageHandle);
        IL.ilDeleteImages(scratch);

        // Revert the OpenGL state back to the default so that accidental
        // texture binding doesn't occur
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }


    /**
     * Calculates the next power of two greater than the given number.
     *
     * @param number The target number.
     * @return the next power of two greater than the given number.
     */
    private int getNextPowerOfTwo(int number) {
        int power = 1;
        while (power < number) {
            power *= 2;
        }

        return power;
    }

    /** Texture width. */
    private int width;

    /** Texture height. */
    private int height;

    /** OpenGL texture handle. */
    private int handle;
}
