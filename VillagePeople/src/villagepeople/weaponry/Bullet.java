package villagepeople.weaponry;

import villagepeople.entities.Bot;
import villagepeople.game.VillageGame;
import explicitlib.geometry.Point;
import explicitlib.geometry.Vector;
import villagepeople.settings.Settings;

/**
 * A projectile fired by the handgun.
 */
public class Bullet extends Projectile {

    /** Bullet speed. */
    private static final int SPEED =
        Settings.getInt("projectiles.bullet.speed");

    /** Bullet bounding radius. */
    private static final double BOUNDING_RADIUS =
        Settings.getDouble("projectiles.bullet.boundingRadius");

    /** Damage inflicted by a bullet. */
    private static final int DAMAGE =
        Settings.getInt("projectiles.bullet.damage");


    /**
     * {@inheritDoc}
     *
     * @param game {@inheritDoc}
     * @param shooter {@inheritDoc}
     * @param location {@inheritDoc}
     * @param rotation {@inheritDoc}
     */
    public Bullet(VillageGame game,
            Bot shooter, Point location, double rotation) {
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
