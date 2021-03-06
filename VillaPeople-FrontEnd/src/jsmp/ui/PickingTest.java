package jsmp.ui;

/* 
 * Copyright (c) 2002-2004 LWJGL Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are 
 * met:
 * 
 * * Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of 
 *   its contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * PickingTest.java
 * This was modified from some example code on LWJGL sample and forums code.
 * Created on Sep 6, 2007, 4:00:20 PM
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.examples.spaceinvaders.Game;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.GLU;
import org.newdawn.slick.Color;

import viglaglepeopgle.renderable.Renderable;

/**
 *
 * @author tomu
 */

public class PickingTest {
	/** The normal title of the window */
	private String WINDOW_TITLE = "Picking Test";

	/** The width of the game display area */
	private int	width	= 800;

	/** The height of the game display area */
	private int	height = 600;
	
	/** The time at which the last rendering looped started from the point of view of the game logic */
	private long lastLoopTime = getTime();

	/** The time since the last record of fps */
	private long lastFpsTime = 0;

	/** The recorded fps */
	private int	fps;

	private static long timerTicksPerSecond = Sys.getTimerResolution();
	
	private boolean lastMouseDown = false;

	private MenuButton m1, m2, m3, m4;
	
	private Set<Renderable> items = new HashSet<Renderable>();
	
      /** Creates a new instance of Main */
      public PickingTest() {
		initialize();
      }

      /**
       * @param args the command line arguments
       */
      public static void main(String[] args) {
		new PickingTest().execute();
      }
	
	private void execute() {
		
        m4 = new MenuButton(200.0f, 25.0f, 650.0f, 400.0f, "", 0);
        //m4.setTexture("menu_button.png");
        items.add(m4);		
		
        m1 = new MenuButton(250.0f, 50.0f, 600.0f, 100.0f, "Start Game", 1);
        //m1.setTexture("menu_button.png");
        items.add(m1);
        
        m2 = new MenuButton(250.0f, 150.0f, 600.0f, 200.0f, "Options", 2);
        //m2.setTexture("menu_button.png");
        items.add(m2);
        
        m3 = new MenuButton(250.0f, 250.0f, 600.0f, 300.0f, "Quit Game", 3);
        //m3.setTexture("menu_button.png");
        items.add(m3);

        
		try {
			gameLoop();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			Display.destroy();
		}
	}
	
	/**
	 * Run the main game loop. This method keeps rendering the scene
	 * and requesting that the callback update its screen.
	 */
	private void gameLoop() {
		while (Game.gameRunning) {
			// Game Logic
			gameLogic();
			
			// Game Rendering
			// clear screen
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			DrawEntities();
		
			// update window contents
			Display.update();
		}
	}
	/**
	 * Notification that a frame is being rendered. Responsible for
	 * running game logic and rendering the scene.
	 */
	public void gameLogic() {
		int mx = Mouse.getX(), my = Mouse.getY();
			
		//SystemTimer.sleep(lastLoopTime+10-SystemTimer.getTime());
		Display.sync(60);
        
		// work out how long its been since the last update, this
		// will be used to calculate how far the entities should
		// move this loop
		long delta = getTime() - lastLoopTime;
		lastLoopTime = getTime();
		lastFpsTime += delta;
		fps++;
		
		// update our FPS counter if a second has passed
		if (lastFpsTime >= 1000) {
			Display.setTitle(WINDOW_TITLE + " (FPS: " + fps + "; mx: "+mx+"; my: "+my+")");
			lastFpsTime = 0;
			fps = 0;
		}
		
		// Selection Magic
		//if(Mouse.isButtonDown(0) && !lastMouseDown) {
		if ((mx > 250 || mx < 600) && (my > 50 || my < 300))
        	selection(mx, my);
		//}
		
		lastMouseDown = Mouse.isButtonDown(0);

		// if escape has been pressed, stop the game
		if (Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Game.gameRunning = false;
		}

	}
	
	/**
	 * Intialise the common elements for the game
	 */
	public void initialize() {
		// initialize the window beforehand
		try {
			setDisplayMode();
			Display.setTitle(WINDOW_TITLE);
			Display.setFullscreen(false);
			Display.create();
      
			GL11.glEnable(GL11.GL_BLEND);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			// Allow cursor to appear
			Mouse.setGrabbed(false);
			
			//GL11.glDepthRange(0.0, 1.0);

			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();

			GL11.glOrtho(0, 800, 600, 0, 1, -1);
			//GL11.glOrtho(0, 8, 0, 8, -0.5, 2.5);


		} catch (LWJGLException le) {
			System.out.println("Game exiting - exception in initialization:");
			le.printStackTrace();
			Game.gameRunning = false;
			return;
		}
	}
	/**
	 * Sets the display mode for fullscreen mode
	 */
	private boolean setDisplayMode() {
	    try {
		// get modes
		DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(width, height, -1, -1, -1, -1, 60, 60);

		org.lwjgl.util.Display.setDisplayMode(dm, new String[] {
		    "width=" + width,
		    "height=" + height,
		    "freq=" + 60,
		    "bpp=" + org.lwjgl.opengl.Display.getDisplayMode().getBitsPerPixel()
		   });
		
		return true;
		
	    } catch (Exception e) {
		e.printStackTrace();
		System.out.println("Unable to enter fullscreen, continuing in windowed mode");
	    }

		return false;
	}
	
	/**
	 * Get the high resolution time in milliseconds
	 * 
	 * @return The high resolution time in milliseconds
	 */
	public static long getTime() {
		// we get the "timer ticks" from the high resolution timer
		// multiply by 1000 so our end result is in milliseconds
		// then divide by the number of ticks in a second giving
		// us a nice clear time in milliseconds
		return (Sys.getTime() * 1000) / timerTicksPerSecond;
	}

      /**
       *
       */
      private void DrawEntities() {
            // cycle round drawing all the entities we have in the game
    	      	  
    	  for (Renderable item : items) {
              item.render();
    	  }
            
            //GL11.glLoadName(2);
            //m2.render();
            /*GL11.glBegin(GL11.GL_QUADS);
            GL11.glColor3f(1.0f, 1.0f, 0.0f);
            GL11.glVertex3i(2, 0, 0);
            GL11.glVertex3i(2, 6, 0);
            GL11.glVertex3i(6, 6, 0);
            GL11.glVertex3i(6, 0, 0);
            GL11.glEnd();*/

            /*GL11.glLoadName(2);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glColor3f(0.0f, 1.0f, 1.0f);
            GL11.glVertex3i(3, 2, -1);
            GL11.glVertex3i(3, 8, -1);
            GL11.glVertex3i(8, 8, -1);
            GL11.glVertex3i(8, 2, -1);
            GL11.glEnd();

            GL11.glLoadName(3);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glColor3f(1.0f, 0.0f, 1.0f);
            GL11.glVertex3i(0, 2, -2);
            GL11.glVertex3i(0, 7, -2);
            GL11.glVertex3i(5, 7, -2);
            GL11.glVertex3i(5, 2, -2);
            GL11.glEnd();*/
      }

      /**
       * The selection magic happens here.
       * @param mouse_x
       * @param mouse_y
       */
      private void selection(int mouse_x, int mouse_y) {
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
		GLU.gluPickMatrix( (float) mouse_x, (float) mouse_y, 5.0f, 5.0f, viewport);
		
		GL11.glOrtho(0, 800, 600, 0, 1, -1);
		DrawEntities();
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
                  System.out.println("Depth: "+depth);
                  for (int i = 1; i < hits; i++) {
                        // Loop Through All The Detected Hits
				// If This Object Is Closer To Us Than The One We Have Selected
                        if (buffer[i * 4 + 1] < (int) depth) {
                              choose = buffer[i * 4 + 3]; // Select The Closer Object
                              depth = buffer[i * 4 + 1]; // Store How Far Away It Is
                        }
                  }

                  System.out.println("Chosen: " + choose);
               //   System.out.println("Depth: " + depth);
                  
                  switch (choose) {
                  	case 1: m1.over(); break;
                  	case 2: m2.over(); break;
                  	case 3: m3.over(); break;              
                  }	  
                  
            }
	}
            
}
