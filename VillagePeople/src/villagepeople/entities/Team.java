
package villagepeople.entities;

import java.util.LinkedList;
import java.util.List;

/**
 * A team.
 */
public class Team {    

	/**
     * Creates a new team.
     */
    public Team(int color) {
        this.bots = new LinkedList<Bot>();
        this.color = color;
    }


    /**
     * Adds a bot to the team.
     *
     * @param bot The bot to add.
     */
    public void add(Bot bot) {
        this.bots.add(bot);
    }


    /**
     * Removes a bot from the team.
     *
     * @param bot The bot to remove.
     */
    public void remove(Bot bot) {
        this.bots.remove(bot);
    }


    /**
     * @return The list of elements in this team.
     */
    public List<Bot> getBots() {
        return this.bots;
    }

    /**
     * Get the color of that team
     * 
     * @return
     */
    public int getColor()
    {
    	return this.color;
    }
    
    /** Team's elements. */
    private List<Bot> bots;
    
    /** Team's colors */
    private int color;
    
    /** Team's possible color */
    public static final int NULL = 0, RED = 1, GREEN = 2, BLUE = 3, YELLOW = 4;
}
