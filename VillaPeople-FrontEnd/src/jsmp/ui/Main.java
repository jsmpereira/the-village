package jsmp.ui;

import org.lwjgl.LWJGLException;

import explicitlib.geometry.Point3D;
import explicitlib.geometry.Point;
import explicitlib.geometry.Size;
import explicitlib.geometry.Vector3D;

import viglaglepeopgle.display.MovingObserver;
import viglaglepeopgle.display.Observer;
import viglaglepeopgle.display.OrthogonalProjection;
import viglaglepeopgle.display.Scene;
import viglaglepeopgle.display.StaticObserver;
import viglaglepeopgle.entities.GLWall;
import viglaglepeopgle.game.Viewer;
import viglaglepeopgle.renderable.Rectangle;
import villagepeople.entities.StaticEntity;
import villagepeople.entities.Wall;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.GLU;

public class Main {

	public static void main(String[] args) {
		
		Main main = new Main();
		Viewer viewer;
		try {
			viewer = new Viewer();
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

			Scene scene = viewer.getScene();			
			
			//viewer.setProjection(new OrthogonalProjection(0, 800, 600, 0, 1, -1));

			//GL11.glMatrixMode(GL11.GL_MODELVIEW);
			//GL11.glRectf(100.0f, 150.0f, 150.0f, 100.0f);
			MenuButton m = new MenuButton(250.0f, 50.0f, 600.0f, 100.0f, "Bzinga", 1);
			
			scene.add(m);
			//scene.render();
			viewer.run();
			
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	//Vector3D direction = new Vector3D (1,1,0);
	
	/*Rectangle menu = 
		//new Rectangle(new Size(5,5), new Point3D(10,-4,1), direction);
		new Rectangle(new Point3D(0, 0, 0),
					new Point3D(20, 0, 0),
					new Point3D(20, 0, -10),
					new Point3D(0, 0,-10));
	
	Rectangle bStart =
		new Rectangle (new Point3D(1,-5,0),
					new Point3D(10, -5, 0),
					new Point3D(10, -5, 3),
					new Point3D(1, -5, 3));*/
	
	//this.explosion.setNormal(direction);
	
	/*scene.setObserver(new StaticObserver(new Point3D(15, 20,
			-10),
			new Point3D(15, 0, -10)
					.minus(new Point3D(15,
							20, -10))));*/
		
	//scene.add(menu);
	//scene.add(bStart);
	/*Wall wall = new Wall(new Point(5.0f,1.5f), new Size(10,2));
	GLWall glwall = new GLWall(wall);
	scene.add(glwall);*/
	//GL11.glTranslatef(-1.5f,0.0f,-6.0f);
}
