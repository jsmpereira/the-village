package villagepeople.weaponry;

import java.util.HashMap;
import java.util.Map;

import villagepeople.entities.Bot;
import explicitlib.geometry.Vector;

/**
 * Weapons' manager.
 *
 * Each bot has a weapon's manager that acts as a container for all the weapons
 * that the bot is carrying.
 *
 * This class also implements the logic related to acquiring weapons or
 * projectiles and even some of the higher-level logic of choosing the weapon
 * to use or firing at the enemy.
 */
public class WeaponsManager {

    /**
     * Creates a new weapon's manager owned by some bot.
     *
     * @param agent The bot that owns this manager.
     */
    public WeaponsManager(Bot agent) {
        this.agent = agent;
    }


    /**
     * Fires against the given enemy.
     *
     * @param other The enemy to shoot at.
     * @return The projectile that was shot.
     */
    public Projectile fireAt(Bot other) {
        Vector toOther = other.getLocation().minus(agent.getLocation());

        if (agent.rotateFacing(toOther)) {
            this.currentWeapon = this.chooseWeapon(other);
            Projectile projectile = this.currentWeapon.fire();

            if (projectile != null) {
                this.agent.getGame().addProjectile(projectile);
            }

            return projectile;
        }

        return null;
    }

    /**
     * Fires against the given enemy.
     *
     * @return The projectile that was shot.
     */
    public Projectile fire() {
    	Projectile projectile = this.currentWeapon.fire();

    	if (projectile != null) {
    		this.agent.getGame().addProjectile(projectile);
    	}

    	return projectile;        
    }

    /**
     * Acquires a weapon.
     *
     * If the bot is already carrying one of these, acquire
     * only the available ammunition (up to the maximum limit).
     *
     * @param weapon The weapon to acquire.
     */
    public void acquire(Weapon weapon) {
        if (this.weapons.containsKey(weapon.getClass().getName())) {
            this.weapons.get(weapon.getClass().getName())
                .acquireAmmo(weapon.getRemainingAmmo());
        } else {
            this.weapons.put(weapon.getClass().getName(), weapon);
            this.currentWeapon = weapon;
        }
    }


    /**
     * @return The weapon that the owner is currently using.
     */
    public Weapon getCurrentWeapon() {
        return this.currentWeapon;
    }


    /**
     * Averages the sum of each weapon's individual strength to obtain the total
     * "weapon's strength" of this bot. This number is then used by the planning
     * layer to decide if the carrier should attack or retreat.
     *
     * @return The total weapon's strength.
     */
    public double totalWeaponStrength() {
        double totalStrength = 0;

        for (Weapon weapon : weapons.values()) {
            totalStrength += weapon.individualWeaponStrength();
        }

        return totalStrength / 3; // XXX Should this be weapons.size() ? ASK Joel
    }


    /**
     * Chooses which weapon to use agains the specified enemy.
     *
     * @param other The enemy to shoot at.
     * @return The choosen weapon.
     */
    private Weapon chooseWeapon(Bot other) {
        double distance = agent.getLocation().distanceTo(other.getLocation());
        double bestDesirability = -1;
        Weapon bestWeapon = null;

        // Choose the one with maximum desirability
        for (Weapon weapon : this.weapons.values()) {
            double desirability = weapon.desirability(distance);

            if (desirability > bestDesirability) {
                bestDesirability = desirability;
                bestWeapon = weapon;
            }
        }

        return bestWeapon;
    }    

    /**
     * Chooses which weapon to use agains the specified enemy.
     *
     * @param other The enemy to shoot at.
     * @return The choosen weapon.
     */
    public Weapon chooseWeapon(int id) {    	
    	int t = 1;
    	for(Weapon weapon : weapons.values()) {    		
    		if(t == id)
    		{
    			System.out.println("Seleccionou a arma: "+weapon.getClass().getCanonicalName());
    			this.currentWeapon = weapon;
    			return this.currentWeapon;
    		}
    		t++;
    	}    	    	
    	this.currentWeapon = weapons.values().iterator().next();
    	return this.currentWeapon;
    }  
    
    /**
     * The bot that owns this weapons' manager.
     */
    private Bot agent;

    /**
     * Weapons possessed by the owner.
     */
    private Map<String, Weapon> weapons = new HashMap<String, Weapon>();

    /**
     * The currently-active weapon.
     */
    private Weapon currentWeapon;
}
