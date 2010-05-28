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
import explicitlib.geometry.Size3D;
import explicitlib.geometry.Vector3D;

import viglaglepeopgle.renderable.Box;
import viglaglepeopgle.renderable.Rectangle;


public class BoxTest {

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
    public void getCornersTest() {
        Box box = new Box(new Size3D(10, 10, 10), new Point3D(0, 0, 0), new Vector3D(0, 0, 1));
        Point3D[] vertices = new Point3D[] {
                new Point3D(-5, -5, 5),
                new Point3D(5, -5, 5),
                new Point3D(5, 5, 5),
                new Point3D(-5, 5, 5),
                new Point3D(-5, -5, -5),
                new Point3D(5, -5, -5),
                new Point3D(5, 5, -5),
                new Point3D(-5, 5, -5)
        };
        
        comparePoints(box.getVertices(), vertices);
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
