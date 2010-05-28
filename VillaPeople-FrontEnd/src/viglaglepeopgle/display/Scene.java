package viglaglepeopgle.display;

import java.util.HashSet;
import java.util.Set;

import viglaglepeopgle.inputcontrollers.GLSceneController;
import viglaglepeopgle.renderable.Renderable;

/**
 * The scene contains all the items that are going to be rendered, the
 * properties of the observer, etc.
 */
public class Scene {

	
    /**
     * Creates a new empty scene.
     *
     */
    public Scene() {
    	
    }
    
    /**
     * Creates a new, empty scene, with the given observer.
     *
     * @param observer The scene's observer.
     */
    public Scene(Observer observer) {
        this.observer = observer;
    }

    /**
     * Renders the scene.
     */
    public void render() {
        // Initialize all the observer configurations
       //observer.prepare();
        
        if(this.glSceneController!=null)
        	this.glSceneController.handleInputController();
        
        for (Renderable item : items) {
            item.render();
        }
    }


    /**
     * Adds a new item to the scene.
     *
     * @param item The item to add.
     */
    public void add(Renderable item) {
        this.items.add(item);
    }


    /**
     * Removes an item from the scene.
     *
     * @param item The item to remove.
     */
    public void remove(Renderable item) {
        boolean removed = this.items.remove(item);
        //assert removed : item;
    }


    /**
     * @return The scene's observer.
     */
    public Observer getObserver() {
        return this.observer;
    }

    /**
     * Set observer
     * @param observer
     */
    public void setObserver(Observer observer) {
    	this.observer = observer;
    }
    
    public void setGLSceneController(GLSceneController glSceneControl)
    {
    	this.glSceneController = glSceneControl;
    }
    
    /** Scene's items to be rendered. */
    private Set<Renderable> items = new HashSet<Renderable>();

    /** The scene's observer. */
    private Observer observer;    
    
    private GLSceneController glSceneController;
}
