
package villagepeople.entities;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import villagepeople.agent.brain.AIController;
import villagepeople.agent.brain.BotControllerEngine;
import villagepeople.events.FireEventArgs;
import villagepeople.game.VillageGame;
import villagepeople.navigation.PathManager;
import villagepeople.settings.Settings;
import villagepeople.steering.ArriveBehavior;
import villagepeople.steering.SeekBehavior;
import villagepeople.steering.SteeringBehavior;
import villagepeople.steering.ArriveBehavior.Deceleration;
import villagepeople.weaponry.HandGun;
import villagepeople.weaponry.LaserGun;
import villagepeople.weaponry.Projectile;
import villagepeople.weaponry.RocketLauncher;
import villagepeople.weaponry.Weapon;
import villagepeople.weaponry.WeaponsManager;
import explicitlib.event.Event;
import explicitlib.geometry.NullVectorException;
import explicitlib.geometry.Point;
import explicitlib.geometry.Vector;

/**
 * Just a simple bot.
 */
public class Bot extends MovingEntity {

    /** Aim noise. */
    private static final double AIM_NOISE =
        Settings.getDouble("entities.bot.aimNoise");


    /**
     * Creates a new bot.
     *
     * @param game The game reference.
     * @param location Bot's initial location.
     * @param team The team this bot belongs to.
     */
    public Bot(VillageGame game, Point location, Team team) {
        super(game, location, new Vector(0, 0), 0); 
        this.team = team;
        init();
    }

    public void init() {
    	this.aimNoise = AIM_NOISE;
        this.health = 100;
        
        team.add(this);

        // Initialize weapons
        this.weaponsManager = new WeaponsManager(this);
        this.weaponsManager.acquire(new LaserGun(this));
        this.weaponsManager.acquire(new RocketLauncher(this));
        this.weaponsManager.acquire(new HandGun(this));

        // Initialize miscellaneous managers
        this.currentSteeringBehavior = null;
        this.pathManager = new PathManager(this);

        // Initialize the brain
        controllerEngine = new AIController();
        this.controllerEngine.startDriven(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(long timeElapsed) {
        // Get the current steering behavior's desired velocity
        Vector desiredVelocity = this.currentSteeringBehavior != null
                ? this.currentSteeringBehavior.calculateVelocity(timeElapsed)
                : new Vector(0, 0);

        try {
        	this.velocity = desiredVelocity.truncate(this.maxSpeed);
        } catch (AssertionError ex) {
        	System.err.println("Time elapsed: " + timeElapsed);
        	System.err.println("Desired velocity: " + desiredVelocity);
        	System.err.println("Current steering behavior: " + this.currentSteeringBehavior);
        	throw ex;
        }

        // Update the brain
        this.controllerEngine.update(timeElapsed);

        // Update position
        super.update(timeElapsed);
    }


    /**
     * Increases the health by a given amount, up to the maximum health allowed
     * by the game.
     *
     * @param amount The amount of health to add.
     */
    public void increaseHealth(int amount) {
        this.health = Math.min(this.health + amount, 100);
    }


    /**
     * Decreases the health by a given amount, down to 0.
     *
     * @param damage The amount of health to decrease.
     */
    public void inflictDamage(int damage) {
        if (!this.isDead()) {
            this.health = Math.max(this.health - damage, 0);
            if (health <= 0) {
                this.die();
            }
        }
    }


    /**
     * returns the closest bot to attack.
     *
     * @return
     */
    // XXX Joel: Mete isto algures no agent.brain
    public Bot getAttackTarget() {
        double best = Double.MAX_VALUE;
        Bot target = null;
        for (Bot other : this.getBotsInFOV()) {
            if (!other.isSameTeam(this)) {
                double distance = this.getLocation().distanceTo(
                        other.getLocation());
                if (distance < best) {
                    best = distance;
                    target = other;
                }
            }
        }
        return target;
    }


    /**
     * Returns the bots that a this bot can see.
     *
     * @return The list of bots in the field-of-view.
     */
    public List<Bot> getBotsInFOV() {
        List<Bot> botsInFOV = new LinkedList<Bot>();

        // Now, iterate over all the other bots
        for (Bot other : this.game.getBots()) {
            if (this.isInFOV(other)) {
                botsInFOV.add(other);
            }
        }

        return botsInFOV;
    }


    /**
     * Checks if this bot can see another.
     *
     * @param other The other bot.
     * @return true if this bot can see the other, false otherwise.
     */
    public boolean isInFOV(Bot other) {
        // I can't see myself.
        if (this == other) {
            return false;
        }

        // The vector that points in the direction ahead of the bot
        Vector heading = this.getHeadingVector();

        // The vector that points to the other bot
        Vector toOther = other.getLocation().minus(this.getLocation());

        // If the angle between the heading vector and the vector that points
        // towards the other bot is less than 90º and there is no obstacle
        // between this bot and the other, add the other bot to the FOV
        try {
            return heading.angleWith(toOther) < Math.PI / 2
                    && !game.getMap().isPathObstructed(this.getLocation(),
                            other.getLocation());
        } catch (NullVectorException e) {
            // Not supposed to happen
            logger.error("There are two bots on top of each other.");
        }

        return false;
    }


    /**
     * Checks if the bot can strafe to its right.
     *
     * @return true if the bot can step to his right, false otherwise.
     */
    public boolean canStepRight() {
        Point right = toWorldCoordinates(new Point(0, -1));

        return canMoveTo(right);
    }


    /**
     * Checks if the bot can strafe to its left.
     *
     * @return true if the bot can step to his left, false otherwise.
     */
    public boolean canStepLeft() {
        Point left = toWorldCoordinates(new Point(0, 1));

        return canMoveTo(left);
    }


    /**
     * Checks if the bot is dead.
     *
     * @return true if it is dead, false otherwise.
     */
    public boolean isDead() {
        return killed;
    }


    /**
     * Acquires a new weapon.
     *
     * @param weapon The weapon acquired.
     */
    public void acquireWeapon(Weapon weapon) {
        this.weaponsManager.acquire(weapon);
    }


    /**
     * Seeks to the given position.
     *
     * @param target The target position.
     */
    public void seek(Point target) {
        this.enableSteering(new SeekBehavior(this, target));
    }


    /**
     * Arrives at the given position.
     *
     * @param target The target position.
     * @param deceleration The type of deceleration to use on arriving.
     */
    public void arrive(Point target, Deceleration deceleration) {
        this.enableSteering(new ArriveBehavior(this, target, deceleration));
    }


    /**
     * Stops the bot.
     */
    public void stop() {
        this.currentSteeringBehavior = null;
    }


    /**
     * Shoots at some other bot.
     *
     * @param other The bot to shoot.
     */
    public void fireAt(Bot other) {
        //assert isInFOV(other);

        Projectile projectile = this.weaponsManager.fireAt(other);
        if (projectile != null) {
            this.onFire().fire(new FireEventArgs(other, projectile));
        }
    }

    /**
     * Shoots.
     *
     */
    public void fire() {
        //assert isInFOV(other);

        Projectile projectile = this.weaponsManager.fire();
        if (projectile != null) {
            this.onFire().fire(new FireEventArgs(null, projectile));
        }
    }   
    
    public void chooseWeapon(int id) {
    	this.weaponsManager.chooseWeapon(id);
    }

    /**
     * Rotates the bot until it is heading in the given direction. Actually, it
     * will only rotate a maximum of maxTurnRate degrees.
     *
     * @param direction The direction that the bot should be heading to.
     * @return true if the bot is heading in the given direction, false if even
     *         after rotating maxTurnRate the bot is not yet heading at that
     *         direction.
     */
    public boolean rotateFacing(Vector direction) {
        if (direction.getX() == 0 && direction.getY() == 0) {
            return true;
        }

        Vector heading = this.getHeadingVector();

        try {
            double angleBetween = heading.angleWith(direction);
            double rotateBy = (angleBetween < this.maxTurnRate) ? angleBetween
                    : this.maxTurnRate;

            // Rotate in clockwise or anti-clockwise direction?
            int sign = heading.wedge(direction) > 0 ? 1 : -1;
            this.setRotation(this.getRotation() + rotateBy * sign);

            return rotateBy == angleBetween;
        } catch (NullVectorException ex) {
            // Not supposed to happen
            ex.printStackTrace();
        }

        return true;
    }


    /**
     * Enables the given steering behavior.
     *
     * @param behavior The steering behavior to enable.
     */
    private void enableSteering(SteeringBehavior behavior) {
        this.currentSteeringBehavior = behavior;
    }


    /**
     * @return Bot's aim noise (i.e., max deviation added to the aim angle).
     */
    public double getAimNoise() {
        return this.aimNoise;
    }


    /**
     * @return Bot's health.
     */
    public int getHealth() {
        return health;
    }


    /**
     * @return Bot's path manager.
     */
    public PathManager getPathManager() {
        return this.pathManager;
    }
    
    /**
     * 
     * @return
     */
    public BotControllerEngine getControllerEngine() {
		return controllerEngine;
	}


	/**
     * @return Bot's team.
     */
    public Team getTeam() {
        return this.team;
    }


    /**
     * Checks if some other bot is from my team.
     *
     * @param other The other bot to check.
     * @return true if both bots share the same team, false otherwise.
     */
    public boolean isSameTeam(Bot other) {
        return (getTeam() == other.getTeam());
    }


    /**
     * @return A measure of the total strength of the weapons that this bot
     *         possesses.
     */
    public double getTotalWeaponStrength() {

        return this.weaponsManager.totalWeaponStrength();
    }


    /**
     * @return the "On collision with bot" event.
     */
    public Event onCollisionWithBot() {
        return this.onCollisionWithBot;
    }


    /**
     * @return the "On hit" event.
     */
    public Event onHit() {
        return this.onHit;
    }


    /**
     * @return the "On catch item" event.
     */
    public Event onCatchItem() {
        return this.onCatchItem;
    }


    /**
     * @return the "On killed" event.
     */
    public Event onKilled() {
        return this.onKilled;
    }


    /**
     * @return the "On fire" event.
     */
    public Event onFire() {
        return this.onFire;
    }


    /**
     * Kill the bot.
     */
    private void die() {
        this.killed = true;
        game.removeBot(this);
        this.onKilled().fire();
    }


    /** Bot's team. */
    private Team team;

    /** Bot's health. */
    private int health;

    /** Am I dead? */
    private boolean killed = false;

    /** Bot's aim noise (i.e., max deviation added to the aim angle). */
    private double aimNoise;

    /** Steering behavior in use. */
    private SteeringBehavior currentSteeringBehavior;

    /** Bot's path manager. */
    private PathManager pathManager;

    /** Bot's weapons' manager. */
    private WeaponsManager weaponsManager;

    /** The Bot is under AI control */
    protected BotControllerEngine controllerEngine;

    /*
     * Events
     */

    /** Event fired when this bot collides with another. */
    private Event onCollisionWithBot = new Event(this);

    /** Event fired when this bot is hit. */
    private Event onHit = new Event(this);

    /** Event fired when this bot catches some item. */
    private Event onCatchItem = new Event(this);

    /** Event fired when this bot is killed. */
    private Event onKilled = new Event(this);

    /** Event fired when this bot fires a weapon. */
    private Event onFire = new Event(this);

    /** Class logger. */
    private static Logger logger = Logger.getLogger(Bot.class.getName());
}
