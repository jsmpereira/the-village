package viglaglepeopgle.entities;

import villagepeople.entities.InventoryItem;

/**
 * GLEntity for an inventory item.
 *
 * Used to display an inventory item in OpenGL.
 */
public abstract class GLInventoryItem extends GLEntity {

    /**
     * Creates a new GLInventoryItem for a given item in the game.
     *
     * @param item The corresponding item.
     */
    public GLInventoryItem(InventoryItem item) {
        super(item);
    }
}
