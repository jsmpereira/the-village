package villagepeople.weaponry;

import villagepeople.entities.Bot;
import villagepeople.entities.StaticEntity;
import villagepeople.game.VillageGame;
import villagepeople.settings.Settings;
import explicitlib.geometry.Point;
import explicitlib.geometry.Vector;

/**
 * Rockets are fired by rocket-launchers.
 *
 * Rockets are a bit different from the regular projectiles, in that when they
 * collide with something, they explode and inflict damage upon any bot that is
 * nearby. Also, note that this damage is proportional to the distance between
 * the bot and the center of the explosion.
 *
 * @see RocketLauncher
 */
public class Rocket extends Projectile {

    /** Rocket speed. */
    private static final int SPEED =
        Settings.getInt("projectiles.rocket.speed");

    /** Rocket bounding radius. */
    private static final double BOUNDING_RADIUS =
        Settings.getDouble("projectiles.rocket.boundingRadius");

    /** Damage inflicted by a Rocket. */
    private static final int DAMAGE =
        Settings.getInt("projectiles.rocket.damage");

    /** Maximum radius of the explosion. */
    private static final double MAX_EXPLOSION_RADIUS =
        Settings.getDouble("projectiles.rocket.maxExplosionRadius");

    /** Initial radius of the explosion. */
    private static final double MIN_EXPLOSION_RADIUS =
        Settings.getDouble("projectiles.rocket.minExplosionRadius");


    /**
     * {@inheritDoc}
     *
     * @param game {@inheritDoc}
     * @param shooter {@inheritDoc}
     * @param location {@inheritDoc}
     * @param rotation {@inheritDoc}
     */
    public Rocket(VillageGame game, Bot shooter, Point location,
            double rotation) {
        super(game, shooter, location,
                new Vector(Math.cos(rotation), Math.sin(rotation))
                    .multiplyBy(SPEED), rotation, BOUNDING_RADIUS);

        this.exploding = false;
    }


    /**
     * {@inheritDoc}
     *
     * Stops the projectile, starts the explosion's animation and inflicts
     * damage upon nearby bots.
     *
     * @param bot {@inheritDoc}
     */
    @Override
    public void hitBot(Bot bot) {
        if (!exploding) {
            this.velocity = new Vector(0, 0);

            this.explosionRadius = MIN_EXPLOSION_RADIUS;
            this.exploding = true;

            // Inflict the damage upon the bots in range
            this.inflictDamage();
        }
    }


    /**
     * {@inheritDoc}
     *
     * Stops the projectile, starts the explosion's animation and inflicts
     * damage upon nearby bots.
     *
     * @param entity {@inheritDoc}
     */
    @Override
    public void hitStaticEntity(StaticEntity entity) {
        if (!exploding) {
            this.velocity = new Vector(0, 0);

            this.explosionRadius = MIN_EXPLOSION_RADIUS;
            this.exploding = true;

            // Inflict the damage upon the bots in range
            this.inflictDamage();
        }
    }


    /**
     * Is the rocket exploding?
     *
     * @return true if the rocket has hit something and is exploding, false
     * otherwise.
     */
    public boolean isExploding() {
        return this.exploding;
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


    /**
     * {@inheritDoc}
     *
     * If this rocket is exploding, updates the radius of the explosion.
     *
     * @param elapsedTime {@inheritDoc}
     */
    @Override
    public void update(long elapsedTime) {
        if (this.exploding) {
            if (!backwards) {
                explosionRadius += elapsedTime / 150.0;
            } else {
                explosionRadius -= elapsedTime / 300.0;
            }

            if (explosionRadius >= MAX_EXPLOSION_RADIUS) {
                backwards = true;
            }

            if (this.explosionRadius < MIN_EXPLOSION_RADIUS) {
                game.removeProjectile(this);
            }
        }

        super.update(elapsedTime);
    }


    /**
     * Gets the bots in range, calculates the damage to inflict upon each one
     * (based on the distance from the center of the explosion), and apply it.
     */
    private void inflictDamage() {
        for (Bot bot : game.getBotsInRange(this.getLocation(),
                MAX_EXPLOSION_RADIUS)) {
            double distance = this.getLocation().distanceTo(bot.getLocation());
            int damage = (int) ((1 - distance / MAX_EXPLOSION_RADIUS)
                    * this.getDamage());

            bot.inflictDamage(damage);
        }
    }


    /**
     * Is the rocket exploding?
     */
    private boolean exploding;

    /**
     * Is the rocket explosion's radius decreasing?
     */
    private boolean backwards = false;

    /**
     * The radius of the explosion, in game coordinates.
     */
    private double explosionRadius;
}
