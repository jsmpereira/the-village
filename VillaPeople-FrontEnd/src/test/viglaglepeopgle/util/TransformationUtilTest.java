package test.viglaglepeopgle.util;

import java.nio.FloatBuffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;

import viglaglepeopgle.util.TransformationUtil;


public class TransformationUtilTest {
    
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
    public void testTransformPoints() {
        // Simple test with 4 points and a translation
        float[] points = new float[] {
                -5, -5, 0, 1,
                 5, -5, 0, 1,
                 5,  5, 0, 1,
                -5,  5, 0, 1
        };
        
        float[] result = new float[] {
                95,  95, 100, 1,
               105,  95, 100, 1,
               105, 105, 100, 1,
                95, 105, 100, 1
        };
        
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslated(100, 100, 100);
            
            FloatBuffer buffer = FloatBuffer.allocate(points.length);
            buffer.put(points);
            buffer.rewind();
            
            TransformationUtil.transformPoints(buffer, GL11.GL_MODELVIEW_MATRIX);
            
            comparePoints(result, buffer);
            
            // More complex test with 8 points and a translation
            points = new float[] {
                    -5, -5, 0, 1,
                     5, -5, 0, 1,
                     5,  5, 0, 1,
                    -5,  5, 0, 1,
                    
                    -5, -5, 10, 1,
                     5, -5, 10, 1,
                     5,  5, 10, 1,
                    -5,  5, 10, 1
            };
            
            result = new float[] {
                    95,  95, 100, 1,
                   105,  95, 100, 1,
                   105, 105, 100, 1,
                    95, 105, 100, 1,
                    
                    95,  95, 110, 1,
                   105,  95, 110, 1,
                   105, 105, 110, 1,
                    95, 105, 110, 1
            };
            
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPushMatrix();
                GL11.glLoadIdentity();
                GL11.glTranslated(100, 100, 100);
                
                buffer = FloatBuffer.allocate(points.length);
                buffer.put(points);
                buffer.rewind();
                
                TransformationUtil.transformPoints(buffer, GL11.GL_MODELVIEW_MATRIX);
                
                comparePoints(result, buffer);
            
            GL11.glPopMatrix();
        
            // Same shit but with only 7 points
            points = new float[] {
                    -5, -5, 0, 1,
                     5, -5, 0, 1,
                     5,  5, 0, 1,
                    -5,  5, 0, 1,
                    
                    -5, -5, 10, 1,
                     5, -5, 10, 1,
                     5,  5, 10, 1,
            };
            
            result = new float[] {
                    95,  95, 100, 1,
                   105,  95, 100, 1,
                   105, 105, 100, 1,
                    95, 105, 100, 1,
                    
                    95,  95, 110, 1,
                   105,  95, 110, 1,
                   105, 105, 110, 1,
            };
            
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPushMatrix();
                GL11.glLoadIdentity();
                GL11.glTranslated(100, 100, 100);
                
                buffer = FloatBuffer.allocate(points.length);
                buffer.put(points);
                buffer.rewind();
                
                buffer = TransformationUtil.transformPoints(buffer, GL11.GL_MODELVIEW_MATRIX);
                comparePoints(result, buffer);
            
        GL11.glPopMatrix();
    }

    private void comparePoints(float[] points, FloatBuffer buffer) {
        assertTrue(points.length <= buffer.remaining());
        for (int i = 0; i < points.length; i++) {
            assertThat((double) points[i], is(closeTo(buffer.get(i), 0.000001)));
        }
    }
}
