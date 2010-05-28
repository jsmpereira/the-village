package jsmp.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.GLU;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.Log;

/**
 * A simple utility test to use the internal slick API without 
 * the slick framework.
 * 
 * @author kevin
 */
public class Menu {
	/** The texture that's been loaded */
	private Texture texture;
	/** The ogg sound effect */
	private Audio oggEffect;
	/** The wav sound effect */
	private Audio wavEffect;
	/** The aif source effect */
	private Audio aifEffect;
	/** The ogg stream thats been loaded */
	private Audio oggStream;
	/** The mod stream thats been loaded */
	private Audio modStream;
	/** The font to draw to the screen */
	private Font font;
	
	private int height = 800;
	private int width = 600;
	private MenuButton m1, m2;
	
	/**
	 * Start the test 
	 */
	public void start() {
		initGL(height, width);
		init();
		
		while (true) {
			update();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			render();
			Display.update();
			Display.sync(100);

			if (Display.isCloseRequested()) {
				System.exit(0);
			}
		}
	}
	
	/**
	 * Initialize the GL display
	 * 
	 * @param width The width of the display
	 * @param height The height of the display
	 */
	private void initGL(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width,height));
			Display.create();
			Display.setVSyncEnabled(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);        
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);                    
        
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);                
        GL11.glClearDepth(1);                                       
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        GL11.glViewport(0,0,width,height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glInitNames();
		GL11.glPushName(0);
		//GL11.glPushMatrix();
	}
	
	/**
	 * Initialise resources
	 */
	public void init() {
		// turn off all but errors
		Log.setVerbose(false);
		
		m1 = new MenuButton(250.0f, 50.0f, 600.0f, 100.0f, "Start Game", 1);
		m2 = new MenuButton(250.0f, 150.0f, 600.0f, 200.0f, "Options", 2);

		//java.awt.Font awtFont = new java.awt.Font("Verdana", java.awt.Font.BOLD, 16);
		//font = new TrueTypeFont(awtFont, false);
		
		// texture load, the second argument is a name assigned to the texture to
		// allow for caching in the texture loader. The 3rd argument indicates whether
		// the image should be flipped on loading
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("textures/firstaid.png"));
		
			/*System.out.println("Texture loaded: "+texture);
			System.out.println(">> Image width: "+texture.getImageWidth());
			System.out.println(">> Image height: "+texture.getImageWidth());
			System.out.println(">> Texture width: "+texture.getTextureWidth());
			System.out.println(">> Texture height: "+texture.getTextureHeight());
			System.out.println(">> Texture ID: "+texture.getTextureID());*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		m1.setTexture("firstaid.png");

		/*try {
			// you can play oggs by loading the complete thing into 
			// a sound
			oggEffect = AudioLoader.getAudio("OGG", new FileInputStream("testdata/restart.ogg"));
			
			// or setting up a stream to read from. Note that the argument becomes
			// a URL here so it can be reopened when the stream is complete. Probably
			// should have reset the stream by thats not how the original stuff worked
			oggStream = AudioLoader.getStreamingAudio("OGG", new File("testdata/bongos.ogg").toURL());
			
			// can load mods (XM, MOD) using ibxm which is then played through OpenAL. MODs
			// are always streamed based on the way IBXM works
			modStream = AudioLoader.getStreamingAudio("MOD", new File("testdata/SMB-X.XM").toURL());

			// playing as music uses that reserved source to play the sound. The first
			// two arguments are pitch and gain, the boolean is whether to loop the content
			modStream.playAsMusic(1.0f, 1.0f, true);
			
			// you can play aifs by loading the complete thing into 
			// a sound
			aifEffect = AudioLoader.getAudio("AIF", new FileInputStream("testdata/burp.aif"));

			// you can play wavs by loading the complete thing into 
			// a sound
			wavEffect = AudioLoader.getAudio("WAV", new FileInputStream("testdata/cbrown01.wav"));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * Game loop update
	 */
	public void update() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_Q) {
					// play as a one off sound effect
					oggEffect.playAsSoundEffect(1.0f, 1.0f, false);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_W) {
					// replace the music thats curretly playing with 
					// the ogg
					oggStream.playAsMusic(1.0f, 1.0f, true);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_E) {
					// replace the music thats curretly playing with 
					// the mod
					modStream.playAsMusic(1.0f, 1.0f, true);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_R) {
					// play as a one off sound effect
					aifEffect.playAsSoundEffect(1.0f, 1.0f, false);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_T) {
					// play as a one off sound effect
					wavEffect.playAsSoundEffect(1.0f, 1.0f, false);
				}
			}
		}
		
		// polling is required to allow streaming to get a chance to
		// queue buffers.
		//SoundStore.get().poll(0);
		//Mouse.setGrabbed(true);
		Mouse.setCursorPosition(0, 0);
		while (Mouse.next())
		{			
			int x = Mouse.getEventDX();
			int y = Mouse.getEventDY();
			System.out.println("X: "+x+" - Y: "+y);
			
			/*if(x < 0 && x > -200) {
								
			}
			else if((x > 250 && x < 600) && (y > 500 && y < 550)) {
				System.out.println("Over Start Menu");
			}else if ((x > 250 && x < 600) && (y > 400 && y < 450)) {
				System.out.println("Over Options Menu");
			}*/
			int key = Mouse.getEventButton();
			if(key == 0)
			{				
				//System.out.println("Pressed left mouse button");
				this.checkMouseOver(x, y);
			}

		}
	}
	
	void checkMouseOver(int xmouse, int ymouse){
		// The selection buffer
		IntBuffer selBuffer = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder()).asIntBuffer();
		int buffer[] = new int[256];
		
		IntBuffer vpBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
		// The size of the viewport. [0] Is <x>, [1] Is <y>, [2] Is <width>, [3] Is <height>
            int[] viewport = new int[4];
		
		// The number of "hits" (objects within the pick area).
		int hits;

		// Get the viewport info
            GL11.glGetInteger(GL11.GL_VIEWPORT, vpBuffer);
            vpBuffer.get(viewport);
		
		// Set the buffer that OpenGL uses for selection to our buffer
		GL11.glSelectBuffer(selBuffer);
		
		// Change to selection mode
		GL11.glRenderMode(GL11.GL_SELECT);
		
		// Initialize the name stack (used for identifying which object was selected)
		GL11.glInitNames();
		GL11.glPushName(0);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		
		/*  create 5x5 pixel picking region near cursor location */
		GLU.gluPickMatrix( (float) xmouse, (float) ymouse, 5.0f, 5.0f, viewport);
		
		GL11.glOrtho(viewport[0], viewport[2], viewport[3], viewport[1], -1, 1);
		render();
		GL11.glPopMatrix();

		// Exit selection mode and return to render mode, returns number selected
		hits = GL11.glRenderMode(GL11.GL_RENDER);
		System.out.println("Number: " + hits);
		
		selBuffer.get(buffer);
            // Objects Were Drawn Where The Mouse Was
            if (hits > 0) {
                  // If There Were More Than 0 Hits
                  int choose = buffer[3]; // Make Our Selection The First Object
                  int depth = buffer[1]; // Store How Far Away It Is
                  for (int i = 1; i < hits; i++) {
                        // Loop Through All The Detected Hits
				// If This Object Is Closer To Us Than The One We Have Selected
                        if (buffer[i * 4 + 1] < (int) depth) {
                              choose = buffer[i * 4 + 3]; // Select The Closer Object
                              depth = buffer[i * 4 + 1]; // Store How Far Away It Is
                        }
                  }

                  System.out.println("Chosen: " + choose);
            }
		
	}
	
	
	void checkMouseOver2(int xmouse, int ymouse) {
		
		int[] selectBuff = new int[64];
		int hits;
		int[] viewport = new int[4];
		
		IntBuffer temp = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
        temp.order();
        GL11.glGetInteger(GL11.GL_VIEWPORT, temp);
        temp.get(viewport);
        temp = ByteBuffer.allocateDirect(2048).asIntBuffer();
        GL11.glSelectBuffer(temp); // Tell OpenGL To Use Our Array For Selection
        temp.get(selectBuff);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		
		GL11.glRenderMode(GL11.GL_SELECT);
		
		GL11.glLoadIdentity();
		GLU.gluPickMatrix(xmouse, ymouse, 2, 2, viewport);
		
		GLU.gluPerspective(45.0f, (float) (viewport[2] - viewport[0]) / (float) (viewport[3] - viewport[1]), 1.0f, 425.0f);
		
		this.render();
		
		hits = GL11.glRenderMode(GL11.GL_RENDER);
		
		this.processMenu(selectBuff);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		System.out.println("BZINGA!!");
		
	}

	public void processMenu(int[] selectBuff){
		
		int id, count;

		count = selectBuff[0];
		id = selectBuff[3];
		
		switch(id){
		case 1:
			System.out.println("START !!!!");
		
		}
		
	}
	
	/**
	 * Game loop render
	 */
	public void render() {
		Color.white.bind();
		texture.bind(); // or GL11.glBind(texture.getTextureID());
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0,0);
			GL11.glVertex2f(100,100);
			GL11.glTexCoord2f(1,0);
			GL11.glVertex2f(100+texture.getTextureWidth(),100);
			GL11.glTexCoord2f(1,1);
			GL11.glVertex2f(100+texture.getTextureWidth(),100+texture.getTextureHeight());
			GL11.glTexCoord2f(0,1);
			GL11.glVertex2f(100,100+texture.getTextureHeight());
		GL11.glEnd();

		m1.render();
		//m2.render();
			
		//font.drawString(150, 300, "HELLO LWJGL WORLD", Color.yellow);
		
		
	}
	
	/**
	 * Entry point to the tests
	 * 
	 * @param argv The arguments to the test
	 */
	public static void main(String[] argv) {
		Menu utils = new Menu();
		utils.start();
	}
}
