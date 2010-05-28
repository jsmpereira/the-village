package villagepeople.navigation;

import villagepeople.entities.InventoryItem;
import villagepeople.entities.WeaponItem;
import explicitlib.geometry.Point;
import explicitlib.graph.search.TerminationCondition;
import explicitlib.graph.search.AStarSearch.State;

/**
 * Condition to terminate a search when a weapon is found.
 */
public class FindWeaponCondition implements TerminationCondition {

    /**
     * Creates a new FindWeaponCondition to be used in a search inside the
     * given map.
     *
     * @param map The map where the search will take place
     */
    public FindWeaponCondition(Map map) {
        this.map = map;
    }


    /**
     * Return true when the current cell contains a weapon.
     *
     * @param state The current state of the search
     * @return true if an weapon was found, false otherwise.
     */
    public boolean isSatisfied(State state) {
        InventoryItem item = map.cellAt((Point) state.getNode().getKey())
                .getItem();

        if (item instanceof WeaponItem) {
            return ((WeaponItem) item).isActive();
        }

        return false;
    }


    /**
     * The map of the game, where the search will take place.
     */
    private Map map;
}
