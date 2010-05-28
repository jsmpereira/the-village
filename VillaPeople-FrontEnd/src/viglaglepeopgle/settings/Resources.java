
package viglaglepeopgle.settings;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GLContext;

import viglaglepeopgle.md3.InvalidMD3FileException;
import viglaglepeopgle.md3.Model;
import viglaglepeopgle.util.Texture;
import explicitlib.geometry.Vector3D;

/**
 * Manages resources (textures and models) used by the application.
 *
 * Resources are configured through the RESOURCES_XML_PATH file.
 */
public final class Resources {

    /** Path of the xml file with the configuration of all the resources. */
    private static final String RESOURCES_XML_PATH = "config/resources.xml";

    static {
        parseResources();
        
      //  initVBOs();
    }


    /**
     * Parse the config/resources.xml file and create the corresponding
     * resources.
     */
    public static void parseResources() {
        Document document = null;
        SAXBuilder sb = new SAXBuilder();

        try {
            File file = new File(RESOURCES_XML_PATH);
            document = sb.build(file);
            Element root = document.getRootElement();

            parseTextures(root.getChild("textures"));
            parseModels(root.getChild("models"));
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static int createVBOID() {
        if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
          IntBuffer buffer = BufferUtils.createIntBuffer(1);
          ARBVertexBufferObject.glGenBuffersARB(buffer);
          return buffer.get(0);
        }
        return 0;
      }
    
    public static int dukeVerticesVBOID;
    public static int dukeTrianglesVBOID;
    private static void initVBOs() {
        Model duke = getModel("duke").getModel();
        int frame = 0;
        dukeVerticesVBOID = createVBOID();
        FloatBuffer vertices = duke.getMeshes()[0].getFrames()[frame].getVertices();

        if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, dukeVerticesVBOID);
            ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertices, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
        }

        IntBuffer triangles = duke.getMeshes()[0].getTriangles();
        dukeTrianglesVBOID = createVBOID();
        if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, dukeTrianglesVBOID);
            ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, triangles, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
        }
    }


    /**
     * Returns the texture with the given id.
     *
     * @param id The key of the texture to find.
     * @return The desired texture, or null if it doesn't exist.
     */
    public static Texture getTexture(String id) {
        assert textures.containsKey(id);
        return textures.get(id);
    }


    /**
     * Returns the texture animation with the given id.
     *
     * @param id The key of the texture animation to find.
     * @return An array with all the frames of the desired animation, or null
     * if it doesn't exist.
     */
    public static Texture[] getTextureAnimation(String id) {
        assert textureAnimations.containsKey(id);
        return textureAnimations.get(id);
    }


    /**
     * Returns the model with the given id.
     *
     * @param id The key of the model to find.
     * @return The desired model, or null if it doesn't exist.
     */
    public static ModelConfiguration getModel(String id) {
        assert models.containsKey(id);
        return models.get(id);
    }


    /**
     * Parses and initializes the textures from the resource configuration file.
     *
     * @param texRoot The &lt;textures&gt; element.
     */
    @SuppressWarnings("unchecked")
    private static void parseTextures(Element texRoot) {
        textures = new HashMap<String, Texture>();
        textureAnimations = new HashMap<String, Texture[]>();

        if (texRoot == null || texRoot.getChildren().isEmpty()) {
            return;
        }

        String texDir = texRoot.getAttributeValue("dir");

        for (Element child : (List<Element>) texRoot.getChildren()) {
            String id = child.getAttributeValue("id");
            String file = child.getAttributeValue("file");
            String animated = child.getAttributeValue("animated");
            boolean isAnimated = animated != null && animated.equals("true");
            String flip = child.getAttributeValue("flip");
            boolean flipNeeded = flip != null && flip.equals("true");

            if (!isAnimated) {
                try {
                    textures.put(id, new Texture(texDir + file, flipNeeded));
                } catch (IOException e) {
                    System.err.println("Exception thrown when loading the "
                            + "texture '" + id + "': " + e);
                    e.printStackTrace();
                }
            } else {
                int length = Integer.valueOf(child.getAttributeValue("length"));
                Texture[] animTex = new Texture[length];
                for (int i = 0; i < length; i++) {
                    String realFile = file.replace("###", i + "");
                    try {
                        animTex[i] = new Texture(texDir + realFile, flipNeeded);
                    } catch (IOException e) {
                        System.err.println("Exception thrown when loading the "
                                + "texture '" + id + "': " + e);
                    }
                }

                textureAnimations.put(id, animTex);
            }
        }
    }


    /**
     * Parses and initializes the models from the resource configuration file.
     *
     * @param modelsRoot The &lt;models&gt; element.
     */
    @SuppressWarnings("unchecked")
    private static void parseModels(Element modelsRoot) {
        models = new HashMap<String, ModelConfiguration>();

        if (modelsRoot == null || modelsRoot.getChildren().isEmpty()) {
            return;
        }

        String modelsDir = modelsRoot.getAttributeValue("dir");

        for (Element child : (List<Element>) modelsRoot.getChildren()) {
            String id = child.getAttributeValue("id");
            String file = child.getAttributeValue("file");
            float scale = Float.valueOf(child.getChild("scale")
                    .getAttributeValue("factor"));

            Element elNormal = child.getChild("default-normal");
            Vector3D defaultNormal = new Vector3D(Float.valueOf(elNormal
                    .getAttributeValue("x")), Float.valueOf(elNormal
                    .getAttributeValue("y")), Float.valueOf(elNormal
                    .getAttributeValue("z")));

            // Find out which textures to apply to this model.
            Element meshTextures = child.getChild("mesh-textures");
            Texture[] modelTextures = new Texture[meshTextures.getChildren()
                    .size()];
            int index = 0;
            for (Element meshTexture : (List<Element>) meshTextures
                    .getChildren()) {
                String textId = meshTexture.getAttributeValue("id");
                modelTextures[index++] = textures.get(textId);
            }

            // Parse the animations defined for the model.
            Element animsElt = child.getChild("animations");
            Map<String, Animation> animations =
                new HashMap<String, Animation>();
            for (Element animElt : (List<Element>) animsElt.getChildren()) {
                String animId = animElt.getAttributeValue("id");
                int firstFrame = Integer.valueOf(animElt.getChildText("start"));
                int lastFrame = Integer.valueOf(animElt.getChildText("end"));
                animations.put(animId, new Animation(firstFrame, lastFrame));
            }

            try {
                Model model = Model.parse(modelsDir + file);
                models.put(id, new ModelConfiguration(model, modelTextures,
                        defaultNormal, scale, animations));
            } catch (IOException e) {
                System.err.println("Exception thrown when loading the model"
                        + " '" + id + "': " + e);
            } catch (InvalidMD3FileException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Hide default constructor.
     */
    private Resources() {
    }

    /** The static textures declared. */
    private static Map<String, Texture> textures;

    /** The animation textures declared. */
    private static Map<String, Texture[]> textureAnimations;

    /** The models declared. */
    private static Map<String, ModelConfiguration> models;
}
