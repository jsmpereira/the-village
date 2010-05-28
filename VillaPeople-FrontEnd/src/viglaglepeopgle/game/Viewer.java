package viglaglepeopgle.game;

import org.apache.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Timer;

import viglaglepeopgle.display.Observer;
import viglaglepeopgle.display.OrthogonalProjection;
import viglaglepeopgle.display.PerspectiveProjection;
import viglaglepeopgle.display.Projection;
import viglaglepeopgle.display.Scene;
import viglaglepeopgle.display.ViewPort;
import villagepeople.navigation.Parser;
import explicitlib.event.Event;
import explicitlib.geometry.Point3D;
import explicitlib.geometry.Vector3D;

/**
 * OpenGL front-end for the VillagePeople game.
 */
public class Viewer {

    /** Window title. */
    private static final String WINDOW_TITLE = "VillagePeople";

    /** Desired window width. */
    //private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_WIDTH = 800;

    /** Desired window height. */
    //private static final int WINDOW_HEIGHT = 768;
    private static final int WINDOW_HEIGHT = 600;
    
    /** Desired frame rate. */
    private static final int FRAMERATE = 60;

    /**
     * Creates a new front-end for the given village people game instance.
     *
     * @throws LWJGLException If display initialization fails.
     */
    public Viewer() throws LWJGLException {
        init(false);

        //this.viewPort = new ViewPort(32, 64, 960, 640);
        this.viewPort = new ViewPort(0, 0, 800, 600);
        //this.projection = new OrthogonalProjection(0, 800, 600, 0, 1, -1);
        this.projection = new PerspectiveProjection(60, 800 / (float) 600, 0.1f, 1000);
        this.scene = new Scene();        
    }

    /**
     * Set Viewer projection
     * 
     * @author José Santos
     */
    
    public void setProjection(Projection projection) {
    	this.projection = projection;
    }

    /**
     * Game's main loop.
     */
    public void run() {
        boolean finished = false;
        while (!finished) {
            Timer.tick();
            this.onUpdate().fire();
            Display.update();

            // Check for close requests
            if (Display.isCloseRequested()) {
                finished = true;
            } else if (Display.isActive()) {
                render();
                Display.sync(FRAMERATE);
            } else {
                // Only bother rendering if the window is visible or dirty
                if (Display.isVisible() || Display.isDirty()) {
                    render();
                }
            }
            
            frameCounter++;
            if (frameCounter % 100 == 0) {
                float timeNow = runningTimer.getTime();
                System.err.println((timeNow - lastTime) + ": " + frameCounter / (timeNow));
                lastTime = timeNow;
               // frameCounter = 0;
            }
        }
    }

    public float lastTime;

    /**
     * @return The "on update" event.
     */
    public Event onUpdate() {
        return this.onUpdate;
    }


    /**
     * @return The scene with all the objects to render.
     */
    public Scene getScene() {
        return this.scene;
    }


    /**
     * Renders the game scene.
     */
    private void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        projection.prepare();
        viewPort.prepare();
        scene.render();
    }


    /**
     * Initialize the display.
     *
     * @param fullscreen When true, initializes the display in full screen mode.
     * @throws LWJGLException If display initialization fails.
     */
    private void init(boolean fullscreen) throws LWJGLException {
        Display.setTitle(WINDOW_TITLE);
        Display.setFullscreen(fullscreen);

        // Enable VSYNC
        Display.setVSyncEnabled(true);

        Display.setDisplayMode(findDesiredDisplayMode());
        Display.create();

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // Enable depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        // Enable alpha blending
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        Keyboard.enableRepeatEvents(true);
    }

    /**
     * Finds the display mode with the desired characteristics.
     *
     * @return The desired display mode.
     * @throws LWJGLException If it's not possible to list the available display
     * modes.
     */
    private DisplayMode findDesiredDisplayMode() throws LWJGLException {
        // Find desired display mode
        DisplayMode chosenMode = null;
        DisplayMode[] modes = Display.getAvailableDisplayModes();
        for (DisplayMode mode : modes) {
             if (mode.getWidth() == WINDOW_WIDTH
                     && mode.getHeight() == WINDOW_HEIGHT) {
                  chosenMode = mode;
                  break;
             }
        }

        if (chosenMode == null) {
            logger.fatal("Unable to find the appropriate display mode.");
            System.exit(1);
        }

        return chosenMode;
    }

    /** The OpenGL view port in use. */
    private ViewPort viewPort;

    /** The OpenGL projection in use. */
    private Projection projection;

    /** The scene with all the objects to render. */
    private Scene scene;

    /** On update event. */
    private Event onUpdate = new Event(this);

    /** Frame counter. */
    private int frameCounter;
    
    /** Running time. */
    private Timer runningTimer = new Timer();

    /** Class logger. */
    private static Logger logger = Logger.getLogger(Parser.class.getName());
}
