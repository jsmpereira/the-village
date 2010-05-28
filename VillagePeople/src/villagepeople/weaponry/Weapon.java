package villagepeople.weaponry;

import villagepeople.entities.Bot;
import villagepeople.fuzzy.FuzzyEvaluator;
import explicitlib.geometry.Vector;

/**
 * Weapon's base class. Each weapon has a fuzzy rules system that calculates the
 * desirability of using that weapon against a specified target, based on the
 * distance to that target (projectiles from different weapons have different
 * speeds) and the remaining ammunition available.
 */
public abstract class Weapon {

    /**
     * Creates a new weapon owned by some agent.
     *
     * @param agent The bot that owns this weapon
     */
    public Weapon(Bot agent) {
        this.agent = agent;
        this.remainingAmmo = this.getInitialAmmo();
        this.fuzzyEvaluator = new FuzzyEvaluator();
    }


    /**
     * Fires the weapon. This fires a projectile heading in the direction the
     * weapon is pointed at, with a small noise added.
     *
     * @return The projectile that was just fired.
     */
    public Projectile fire() {
        if (this.isReady() && this.remainingAmmo > 0) {
            this.remainingAmmo--;
            this.lastShotTime = System.currentTimeMillis();
            Projectile projectile = this.createProjectile();

            // Set's the bullet's location so that it doesn't overlap with the
            // shooter
            Vector displacement = agent.getHeadingVector().multiplyBy(
                    agent.getBoundingRadius() + projectile.getBoundingRadius());
            projectile.setLocation(projectile.getLocation().plus(displacement));
            return projectile;
        }
        return null;
    }


    /**
     * Increments the remaining ammunition available.
     *
     * @param ammoCount The ammunition acquired.
     */
    public void acquireAmmo(int ammoCount) {
        this.remainingAmmo = Math.min(this.remainingAmmo + ammoCount,
                this.getMaxAmmo());
    }


    /**
     * @return the remaining ammunition for the weapon.
     */
    public int getRemainingAmmo() {
        return this.remainingAmmo;
    }


    /**
     * Calculates the desirability to use this weapon, based on the distance to
     * the target and the number of remaining ammo.
     *
     * @param distanceToTarget The distance to the target
     * @return The desirability to shoot the enemy with this weapon
     */
    public abstract double desirability(double distanceToTarget);


    /**
     * Creates and returns a projectile for this weapon.
     *
     * @return An instance of the projectile class used by this weapon.
     */
    protected abstract Projectile createProjectile();


    /**
     * Adds some noise when the weapon is about to shoot, so that the bot
     * doesn't always hit his opponent.
     *
     * @return The change in radians that will be added to the projectile's
     *         angle.
     */
    protected double noise() {
        return this.agent.getAimNoise() * (2 * Math.random() - 1);
    }


    /**
     * Checks if the weapon is ready to shoot.
     *
     * @return true if the weapon is ready, false otherwise
     */
    protected boolean isReady() {
        final int millisecondsInASecond = 1000;
        return System.currentTimeMillis() - this.lastShotTime
            > (millisecondsInASecond / this.getFireRate());
    }


    /**
     * @return The initial ammunition for this weapon.
     */
    protected abstract int getInitialAmmo();


    /**
     * @return The max ammunition for this weapon that the bot can carry.
     */
    protected abstract int getMaxAmmo();


    /**
     * @return The number of shots this weapon can make per second.
     */
    protected abstract long getFireRate();


    /**
     * Calculates the strength of the weapon, as a number between 0 (weak) and
     * 1 (strong). This number is then used by the planning layer to decide
     * if the carrier should attack or retreat.
     *
     * @return The strength of the specific weapon
     */
    protected double individualWeaponStrength() {
        return (double) this.getRemainingAmmo() / (double) this.getMaxAmmo();
    }

    /**
     * The bot that owns this weapons' manager.
     */
    protected Bot agent;

    /**
     * Remaining ammunition available.
     */
    protected int remainingAmmo;

    /**
     * The time when the last shot was fired.
     */
    protected long lastShotTime;

    /**
     * The fuzzy evaluator is used to calculate the desirability
     * to use this weapon.
     */
    protected FuzzyEvaluator fuzzyEvaluator;
}
