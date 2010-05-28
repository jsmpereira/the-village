package viglaglepeopgle.entities;

import viglaglepeopgle.renderable.Box;
import viglaglepeopgle.renderable.Box.Face;
import viglaglepeopgle.settings.Resources;
import viglaglepeopgle.util.Texture;
import villagepeople.entities.FirstAidItem;
import villagepeople.entities.InventoryItem;
import explicitlib.geometry.Point3D;
import explicitlib.geometry.Size3D;
import explicitlib.geometry.Vector3D;

/**
 * GLEntity for a first-aid kit.
 *
 * Used to display a first-aid kit in OpenGL.
 */
public class GLFirstAidItem extends GLEntity {

    /** Box size. */
    private static final float BOX_SIZE = 0.3f;


    /**
     * Creates a new GLFirstAidItem for a given first-aid kit in the game.
     *
     * @param item The corresponding first-aid kit.
     */
    public GLFirstAidItem(FirstAidItem item) {
        super(item);

        Size3D boxSize = new Size3D(BOX_SIZE, BOX_SIZE / 4.0f, BOX_SIZE);
        Point3D boxLocation = new Point3D(
                item.getLocation().getX() + 0.5,
                0.3,
                -(item.getLocation().getY() + 0.5));
        Texture boxTexture = Resources.getTexture("firstaid");
        this.box = new Box(boxSize, boxLocation,
                new Vector3D(0, -1, 0), boxTexture);
        this.box.setTextureRepetitions(Face.TOP, 1, 1);
        this.box.setTextureRepetitions(Face.BOTTOM, 1, 1);
        this.box.setTexture(Face.FRONT, Texture.NULL_TEXTURE);
        this.box.setTexture(Face.BACK, Texture.NULL_TEXTURE);
        this.box.setTexture(Face.LEFT, Texture.NULL_TEXTURE);
        this.box.setTexture(Face.RIGHT, Texture.NULL_TEXTURE);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void render() {
        if (((InventoryItem) this.entity).isActive()) {
            box.rotateBy(0.05);
            box.render();
        }
    }


    /** Internally, a first-aid kit is rendered as a box. */
    private Box box;
}
