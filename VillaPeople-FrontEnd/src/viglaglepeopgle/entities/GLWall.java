package viglaglepeopgle.entities;

import explicitlib.geometry.Point3D;
import explicitlib.geometry.Size;
import explicitlib.geometry.Size3D;
import explicitlib.geometry.Vector3D;
import viglaglepeopgle.renderable.Box;
import viglaglepeopgle.settings.Resources;
import viglaglepeopgle.util.Texture;
import villagepeople.entities.Wall;

/**
 * GLEntity for a Wall.
 *
 * Used to display a game wall with OpenGL.
 */
public class GLWall extends GLEntity {

    /**
     * Creates a new GLWall for a wall on the game map.
     *
     * @param wall The corresponding wall.
     */
    public GLWall(Wall wall) {
        super(wall);

        Size wallSize = wall.getSize();
        Size3D boxSize = new Size3D(wallSize.getWidth(), 1,
                wallSize.getHeight());
        
        Point3D boxLocation = new Point3D(
                (int) wall.getLocation().getX() + wallSize.getWidth() / 2,
                0.5,
                -((int) wall.getLocation().getY() + wallSize.getHeight() / 2));

        Texture wallTexture = Resources.getTexture("wall");
        this.box = new Box(boxSize, boxLocation, new Vector3D(0, 0, 1),
                wallTexture);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void render() {
        box.render();
    }

    /** Internally, a wall is rendered as a box. */
    private Box box;
}
