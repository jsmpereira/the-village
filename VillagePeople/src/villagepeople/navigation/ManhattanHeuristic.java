package villagepeople.navigation;

import explicitlib.geometry.Point;
import explicitlib.graph.IGraph;
import explicitlib.graph.SimpleWeight;
import explicitlib.graph.IGraph.Node;
import explicitlib.graph.IGraph.Weight;
import explicitlib.graph.search.FindTargetCondition;
import explicitlib.graph.search.Heuristic;
import explicitlib.graph.search.TerminationCondition;

/**
 * An heuristic that estimates costs using the manhattan distance between
 * points.
 */
public class ManhattanHeuristic implements Heuristic {

    /**
     * Calculates the manhattan distance between the current node and the
     * search's target node, adds it to the cost found so far from the source
     * node, and returns it.
     *
     * @param graph The graph where the search takes place.
     * @param current The current node.
     * @param condition The termination condition of the search
     * @param costSoFar The cost found from the source node to the current node.
     * @return The resulting estimated cost.
     */
    @SuppressWarnings("unchecked")
    public Weight estimate(IGraph graph, Node current,
            TerminationCondition condition, IGraph.Weight costSoFar) {
        Node target = ((FindTargetCondition) condition).getTarget();

        Point targetPoint = ((Node<Point>) target).getKey();
        Point sourcePoint = ((Node<Point>) current).getKey();

        double distance = manhattanDistance(sourcePoint, targetPoint);
        return costSoFar.plus(new SimpleWeight(distance));
    }


    /**
     * Calculates the manhattan distance between two points.
     *
     * @param pointA The first point.
     * @param pointB The second point.
     * @return The manhattan distance between the points.
     */
    private double manhattanDistance(Point pointA, Point pointB) {
        return Math.abs(pointA.getX() - pointB.getX())
            + Math.abs(pointA.getY() - pointB.getY());
    }
}
