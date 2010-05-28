package villagepeople.navigation;

import villagepeople.entities.FirstAidItem;
import villagepeople.entities.InventoryItem;
import explicitlib.geometry.Point;
import explicitlib.graph.search.TerminationCondition;
import explicitlib.graph.search.AStarSearch.State;

/**
 * Condition to terminate a search when a first aid kit is found.
 */
public class FindHealthCondition implements TerminationCondition {

    /**
     * Creates a new FindHealthCondition to be used in a search inside the
     * given map.
     *
     * @param map The map where the search will take place
     */
    public FindHealthCondition(Map map) {
        this.map = map;
    }


    /**
     * Return true when the current cell contains an health item.
     *
     * @param state The current state of the search
     * @return true if an health item was found, false otherwise.
     */
    public boolean isSatisfied(State state) {
        Map.Cell cell = map.cellAt((Point) state.getNode().getKey());
        InventoryItem item = cell.getItem();

        if (item instanceof FirstAidItem) {
            return ((FirstAidItem) item).isActive();
        }

        return false;
    }


    /**
     * The map of the game, where the search will take place.
     */
    private Map map;
}
