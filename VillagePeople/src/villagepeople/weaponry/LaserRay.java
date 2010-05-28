package villagepeople.weaponry;

import villagepeople.entities.Bot;
import villagepeople.game.VillageGame;
import explicitlib.geometry.Point;
import explicitlib.geometry.Vector;
import villagepeople.settings.Settings;

/**
 * A projectile fired by the lasergun.
 *
 * @see LaserGun
 */
public class LaserRay extends Projectile {

    /** Laser ray speed. */
    private static final int SPEED =
        Settings.getInt("projectiles.laserRay.speed");

    /** Laser ray bounding radius. */
    private static final double BOUNDING_RADIUS =
        Settings.getDouble("projectiles.laserRay.boundingRadius");

    /** Damage inflicted by a Laser ray. */
    private static final int DAMAGE =
        Settings.getInt("projectiles.laserRay.damage");


    /**
     * {@inheritDoc}
     *
     * @param game {@inheritDoc}
     * @param shooter {@inheritDoc}
     * @param location {@inheritDoc}
     * @param rotation {@inheritDoc}
     */
    public LaserRay(VillageGame game, Bot shooter, Point location,
            double rotation) {
        super(game, shooter, location,
                new Vector(Math.cos(rotation), Math.sin(rotation))
                    .multiplyBy(SPEED), rotation, BOUNDING_RADIUS);
    }


    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getDamage() {
        return DAMAGE;
    }
}
