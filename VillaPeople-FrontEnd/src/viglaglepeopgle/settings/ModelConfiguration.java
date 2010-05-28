
package viglaglepeopgle.settings;

import java.util.Map;

import viglaglepeopgle.md3.Model;
import viglaglepeopgle.util.Texture;
import explicitlib.geometry.Vector3D;

/**
 * Configuration for a model.
 *
 * Includes attributes not included in the model file itself, like the textures
 * to apply, existing animations, scaling factor, etc.
 */
public class ModelConfiguration {

    /**
     * Configures the given model.
     *
     * @param model The model that is configured by this instance.
     * @param textures Textures to apply to this model (one per mesh).
     * @param initialNormal Default normal, or above vector of the model.
     * @param scale Scaling factor to apply to the original model.
     * @param animations Animations defined for this model.
     */
    public ModelConfiguration(Model model, Texture[] textures,
            Vector3D initialNormal, float scale,
            Map<String, Animation> animations) {
        this.model = model;
        this.textures = textures;
        this.initialNormal = initialNormal;
        this.scale = scale;
        this.animations = animations;
    }


    /**
     * @return the model that is configured by this instance.
     */
    public Model getModel() {
        return model;
    }


    /**
     * @return the textures to apply to this model (one per mesh).
     */
    public Texture[] getTextures() {
        return textures;
    }


    /**
     * @return the default normal, or above vector of the model.
     */
    public Vector3D getInitialNormal() {
        return initialNormal;
    }


    /**
     * @return the scaling factor to apply to the original model.
     */
    public float getScale() {
        return scale;
    }


    /**
     * @return the animations defined for this model.
     */
    public Map<String, Animation> getAnimations() {
        return animations;
    }

    /** The model that is configured by this instance. */
    private Model model;

    /** Textures to apply to this model (one per mesh). */
    private Texture[] textures;

    /** Default normal, or above vector of the model. */
    private Vector3D initialNormal;

    /** Scaling factor to apply to the original model. */
    private float scale;

    /** Animations defined for this model. */
    private Map<String, Animation> animations;
}
