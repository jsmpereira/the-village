package villagepeople.navigation;

import java.util.LinkedList;

import villagepeople.entities.Bot;
import explicitlib.geometry.Point;
import explicitlib.graph.search.NoPathFoundException;
import explicitlib.graph.search.Path;

/**
 * A path manager simplifies the creation of paths, abstracting the Bot from
 * fiddling with the navigational graph API's.
 */
public class PathManager {

    /**
     * Creates a new path manager, for the given agent.
     *
     * @param agent The agent that will own this path manager.
     */
    public PathManager(Bot agent) {
        this.agent = agent;
        this.map = agent.getGame().getMap();
    }


    /**
     * Calculates a path from the agent's current location to some other target
     * location. Assumes that such a path exists!
     *
     * @param target The destination point
     * @return The path
     * @throws NoPathFoundException when no path was found
     */
    public Path<Point> getPathTo(Point target) throws NoPathFoundException {

        if (agent.canMoveTo(target)) {
            Path<Point> path = new Path<Point>();
            path.addFirst(agent.getLocation());
            path.addLast(target);

            return path;
        }
        // Find the nodes closest to the source and target position
        Point closestSource = map.cellAt(agent.getLocation()).getCenter();
        Point closestTarget = map.cellAt(target).getCenter();

        // Computes the path
        Path<Point> path = map.findPath(closestSource, closestTarget);

        // Don't forget to include the target location
        path.addLast(target);

        // Add the current location to the front of the path
        path.addFirst(agent.getLocation());

        // Smooth the path
        path = this.smoothPath(path);

        return path;
    }


    /**
     * Calculates a path from the agent's current location to the nearest health
     * item.
     *
     * @return The path
     * @throws NoPathFoundException when no path was found
     */
    public Path<Point> getPathToHealthItem() throws NoPathFoundException {
        // Find the node closest to the source position
        Point closestSource = map.cellAt(agent.getLocation()).getCenter();

        // Computes the path
        Path<Point> path = map.findPathToHealthItem(closestSource);

        // Add the current location to the front of the path
        path.addFirst(agent.getLocation());

        // Smooth the path
        path = this.smoothPath(path);

        return path;
    }


    /**
     * Calculates a path from the agent's current location to the nearest
     * weapon.
     *
     * @return The path
     * @throws NoPathFoundException when no path was found
     */
    public Path<Point> getPathToWeapon() throws NoPathFoundException {
        // Find the node closest to the source position
        Point closestSource = map.cellAt(agent.getLocation()).getCenter();

        // Computes the path
        Path<Point> path = map.findPathToWeapon(closestSource);

        // Add the current location to the front of the path
        path.addFirst(agent.getLocation());

        // Smooth the path
        path = this.smoothPath(path);

        return path;
    }


    /**
     * Smooths the path, removing unneeded way-points.
     *
     * @param path The path to smooth
     * @return The smoothed path
     */
    private Path<Point> smoothPath(Path<Point> path) {

        LinkedList<Point> result = new LinkedList<Point>();

        // Add the first way-point, because that's where the path begins
        Point last = path.getWayPoints().getFirst();
        Point previous = last;
        result.add(last);

        for (Point point : path.getWayPoints()) {
            // Adds the way-point, if the path between the previous one
            // and the next is obstructed
            if (!agent.canMoveBetween(last, point)) {
                result.add(previous);
                last = previous;
            }

            previous = point;
        }

        // Always add the last way-point
        result.add(previous);

        return new Path<Point>(result);
    }

    /** The agent that owns this path manager. */
    private Bot agent;

    /** The game map. */
    private Map map;
}
