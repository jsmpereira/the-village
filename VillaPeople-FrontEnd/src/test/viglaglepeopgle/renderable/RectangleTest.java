package test.viglaglepeopgle.renderable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.hamcrest.core.Is.*;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

import explicitlib.geometry.Point3D;
import explicitlib.geometry.Size;
import explicitlib.geometry.Vector3D;

import viglaglepeopgle.renderable.Rectangle;


public class RectangleTest {

    @Before
    public void setUp() {
        try {
            Display.setDisplayMode(new DisplayMode(100, 100));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
    
    @After
    public void tearDown() {
        Display.destroy();
    }
    
    @Test
    public void verticesConstructorTest() {
        Point3D[] vertices = new Point3D[] {
                new Point3D(-5, -5, 0),
                new Point3D(5, -5, 0),
                new Point3D(5, 5, 0),
                new Point3D(-5, 5, 0)
        };
        Rectangle square = new Rectangle(vertices[0], vertices[1], vertices[2], vertices[3]);
        comparePoints(square.getVertices(), vertices);
        
        vertices = new Point3D[] {
                new Point3D(5 / -Math.sqrt(2), -5, 5 / Math.sqrt(2)),
                new Point3D(5 / Math.sqrt(2), -5, 5 / -Math.sqrt(2)),
                new Point3D(5 / Math.sqrt(2), 5, 5 / -Math.sqrt(2)),
                new Point3D(5 / -Math.sqrt(2), 5, 5 / Math.sqrt(2))
        };
        square = new Rectangle(vertices[0], vertices[1], vertices[2], vertices[3]);        
        comparePoints(square.getVertices(), vertices);
        
        vertices = new Point3D[] {
                new Point3D(5, -5, 0),
                new Point3D(5, 5, 0),
                new Point3D(-5, 5, 0),
                new Point3D(-5, -5, 0) 
        };
        square = new Rectangle(vertices[0], vertices[1], vertices[2], vertices[3]);        
        comparePoints(square.getVertices(), vertices);
    }
    
    @Test
    public void getCornersTest() {
        Rectangle square = new Rectangle(new Size(10, 10), new Point3D(0, 0, 0), new Vector3D(0, 0, 1));
        Point3D[] vertices = new Point3D[] {
                new Point3D(-5, -5, 0),
                new Point3D(5, -5, 0),
                new Point3D(5, 5, 0),
                new Point3D(-5, 5, 0)
        };
        
        comparePoints(square.getVertices(), vertices);
        
        square = new Rectangle(new Size(10, 10), new Point3D(10, 0, 0), new Vector3D(0, 0, 1));
        vertices = new Point3D[] {
                new Point3D(5, -5, 0),
                new Point3D(15, -5, 0),
                new Point3D(15, 5, 0),
                new Point3D(5, 5, 0)
        };
        
        comparePoints(square.getVertices(), vertices);
        
        square = new Rectangle(new Size(10, 10), new Point3D(10, 10, 0), new Vector3D(0, 1, 0));
        vertices = new Point3D[] {
                new Point3D(5, 10, 5),
                new Point3D(15, 10, 5),
                new Point3D(15, 10, -5),
                new Point3D(5, 10, -5)
        };
        
        comparePoints(square.getVertices(), vertices);
        
        square = new Rectangle(new Size(10, 10), new Point3D(0, 0, 0), new Vector3D(1, 0, 1));
        vertices = new Point3D[] {
                new Point3D(5 / -Math.sqrt(2), -5, 5 / Math.sqrt(2)),
                new Point3D(5 / Math.sqrt(2), -5, 5 / -Math.sqrt(2)),
                new Point3D(5 / Math.sqrt(2), 5, 5 / -Math.sqrt(2)),
                new Point3D(5 / -Math.sqrt(2), 5, 5 / Math.sqrt(2))
        };
        
        comparePoints(square.getVertices(), vertices);
        
        square = new Rectangle(new Size(10, 10), new Point3D(0, 0, 0), new Vector3D(0, 0, 1));
        square.rotateBy((float) Math.PI / 2);
        vertices = new Point3D[] {
                new Point3D(5, -5, 0),
                new Point3D(5, 5, 0),
                new Point3D(-5, 5, 0),
                new Point3D(-5, -5, 0) 
        };
        
        comparePoints(square.getVertices(), vertices);
        
        square = new Rectangle(new Size(10, 10), new Point3D(0, 0, 0), new Vector3D(1, 0, 1));
        square.rotateBy((float) Math.PI / 2);
        vertices = new Point3D[] {
                new Point3D(5 / Math.sqrt(2), -5, 5 / -Math.sqrt(2)),
                new Point3D(5 / Math.sqrt(2), 5, 5 / -Math.sqrt(2)),
                new Point3D(5 / -Math.sqrt(2), 5, 5 / Math.sqrt(2)),
                new Point3D(5 / -Math.sqrt(2), -5, 5 / Math.sqrt(2))
        };
        
        comparePoints(square.getVertices(), vertices);
    }
    
    private void comparePoints(Point3D[] first, Point3D[] second) {
        assertThat(first.length, is(second.length));
        for (int i = 0; i < first.length; i++) {
            assertThat(first[i].getX(), is(closeTo(second[i].getX(), 0.00001)));
            assertThat(first[i].getY(), is(closeTo(second[i].getY(), 0.00001)));
            assertThat(first[i].getZ(), is(closeTo(second[i].getZ(), 0.00001)));
        }
    }
}
