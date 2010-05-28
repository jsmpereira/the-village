
package villagepeople.entities;

import java.awt.image.BufferedImage;

import villagepeople.events.ItemCaughtEventArgs;
import villagepeople.navigation.Map;
import villagepeople.settings.Settings;
import villagepeople.weaponry.HandGun;
import villagepeople.weaponry.LaserGun;
import villagepeople.weaponry.RocketLauncher;

import com.golden.gamedev.Game;

import explicitlib.event.Action;
import explicitlib.event.EventArgs;
import explicitlib.geometry.Point;

/**
 * Weapons lying around inside the map.
 */
public class WeaponItem extends InventoryItem {

    /**
     * Types of existing weapons.
     */
    public enum WeaponType {
        /** Handgun. */
        HAND_GUN,

        /** Rocket launcher. */
        ROCKET_LAUNCHER,

        /** Larser gun. */
        LASER_GUN

        // Insert new weapons here
    }


    /** First-aid item's bounding radius. */
    private static final double BOUNDING_RADIUS =
        Settings.getDouble("entities.weaponItem.boundingRadius");


    /** First-aid item's bounding radius. */
    private static final int ACTIVATION_INTERVAL =
        Settings.getInt("entities.weaponItem.activationInterval");


    /**
     * Creates a new weapon item to be added to the given map, at the given
     * position.
     *
     * @param map The map of the game.
     * @param cell The cell where this weapon will be lying on.
     * @param weaponType The type of the weapon.
     */
    public WeaponItem(Map map, Point cell, final WeaponType weaponType) {
        super(map, cell, BOUNDING_RADIUS, ACTIVATION_INTERVAL);

        this.weaponType = weaponType;

        // What happens when this item is caught?
        this.onCaught().attach(new Action() {
            @Override
            public void execute(Object source, EventArgs args) {
                Bot bot = ((ItemCaughtEventArgs) args).getBot();

                if (weaponType == WeaponType.HAND_GUN) {
                    bot.acquireWeapon(new HandGun(bot));
                } else if (weaponType == WeaponType.LASER_GUN) {
                    bot.acquireWeapon(new LaserGun(bot));
                } else if (weaponType == WeaponType.ROCKET_LAUNCHER) {
                    bot.acquireWeapon(new RocketLauncher(bot));
                }
            }
        });
    }


    // XXX Remove
    @Override
    protected BufferedImage loadImage(Game game) {
        if (this.weaponType == WeaponType.HAND_GUN) {
            return game.getImage(Settings.SPRITE_HANDGUN_PATH);
        } else if (this.weaponType == WeaponType.LASER_GUN) {
            return game.getImage(Settings.SPRITE_LASER_GUN_PATH);
        } else {
            return game.getImage(Settings.SPRITE_ROCKET_LAUNCHER_PATH);
        }
    }


    /**
     * @return The type of the weapon.
     */
    public WeaponType getWeaponType() {
        return weaponType;
    }


    /** The type of the weapon. */
    private WeaponType weaponType;
}
