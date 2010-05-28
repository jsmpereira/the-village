package villagepeople.navigation;

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import villagepeople.entities.FirstAidItem;
import villagepeople.entities.InventoryItem;
import villagepeople.entities.StaticEntity;
import villagepeople.entities.Wall;
import villagepeople.entities.WeaponItem;
import explicitlib.geometry.Line;
import explicitlib.geometry.Point;
import explicitlib.geometry.Size;
import explicitlib.geometry.Vector;
import explicitlib.graph.Graph;
import explicitlib.graph.SimpleWeight;
import explicitlib.graph.search.AStarSearch;
import explicitlib.graph.search.DijkstraSearch;
import explicitlib.graph.search.FindTargetCondition;
import explicitlib.graph.search.NoPathFoundException;
import explicitlib.graph.search.Path;

/**
 * The game map.
 *
 * VillagePeople is a grid-based game and, to reflect that, every
 * map is composed by cell's of class Map::Cell.
 */
public class Map {

    /**
     * Creates a new map of containing width x height cells.
     *
     * @param width Number of cells along the horizontal axis
     * @param height Number of cells along the vertical axis
     */
    public Map(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException();
        }

        this.grid = new Cell[height + 2][width + 2];

        for (int i = 0; i < height + 2; i++) {
            for (int j = 0; j < width + 2; j++) {
                this.grid[i][j] = new Cell(new Point(j - 1, i - 1));
            }
        }

        // Surround the arena with walls
        this.addEntity(new Wall(new Point(-1, -1), new Size(width + 2, 1)));
        this.addEntity(new Wall(new Point(-1, height), new Size(width + 2, 1)));
        this.addEntity(new Wall(new Point(-1, 0), new Size(1, height)));
        this.addEntity(new Wall(new Point(width, 0), new Size(1, height)));
    }


    /**
     * Updates the map after one iteration.
     *
     * @param dtime Time spent since last update.
     */
    public void update(long dtime) {
        for (InventoryItem item : this.items) {
            item.update(dtime);
        }
    }


    /**
     * Adds a static entity to the map, but only if the area occupied by the
     * entity contains no other entities.
     *
     * @param entity The entity to add.
     * @return True if the entity was added, false otherwise (possibly because
     * the area was not clear)
     */
    public boolean addEntity(StaticEntity entity) {
        if (isAreaClear(entity.getLocation(), entity.getSize())) {
            for (int i = 0; i < entity.getSize().getHeight(); i++) {
                for (int j = 0; j < entity.getSize().getWidth(); j++) {
                    Cell cell = cellAt((int) (j + entity.getLocation().getX()),
                            (int) (i + entity.getLocation().getY()));
                    cell.setEntity(entity);
                }
            }
            this.staticEntities.add(entity);
            return true;
        }
        return false;
    }


    /**
     * @return The list of static entities lying inside the map.
     */
    public List<StaticEntity> getStaticEntities() {
        return this.staticEntities;
    }

    /**
     * Adds an inventory item to the map.
     *
     * @param item The item to add
     * @return true if the item was successfully added, false otherwise.
     */
    public boolean addItem(InventoryItem item) {
        // Only add to this.items if able to add to the cell
        if (this.cellAt(item.getLocation()).setItem(item)) {
            this.items.add(item);
            return true;
        }
        return false;
    }


    /**
     * Removes an inventory item from the map.
     *
     * @param item The item to remove
     */
    public void removeItem(FirstAidItem item) {
        cellAt(item.getLocation()).removeItem();
        this.items.remove(item);
    }


    /**
     * @return The list of inventory items
     */
    public List<InventoryItem> getItems() {
        return this.items;
    }


    /**
     * @return The list of active first-aid kits on the map
     */
    public List<FirstAidItem> getHealthItems() {
        List<FirstAidItem> items = new LinkedList<FirstAidItem>();
        for (InventoryItem item : getItems()) {
            if (item instanceof FirstAidItem && item.isActive()) {
                items.add((FirstAidItem) item);
            }
        }
        return items;
    }


    /**
     * @return The list of active weapons on the map
     */
    public List<WeaponItem> getWeaponItems() {
        List<WeaponItem> weapons = new LinkedList<WeaponItem>();
        for (InventoryItem item : getItems()) {
            if (item instanceof WeaponItem && item.isActive()) {
                weapons.add((WeaponItem) item);
            }
        }
        return weapons;
    }


    /**
     * Returns the item at the given location, if any.
     *
     * @param location The location to search for the item.
     * @return The item, or null if none exists.
     */
    public InventoryItem getItemAt(Point location) {
        return cellAt(location).getItem();
    }


    /**
     * Returns a list of cells around a given point.
     *
     * @param seed The point to search at.
     * @return The list of neighbours found.
     */
    public List<Point> getNeighbours(Point seed) {
        List<Point> neighbours = new LinkedList<Point>();

        int x = (int) seed.getX();
        int y = (int) seed.getY();

        neighbours.add(new Point(x - 1, y));
        neighbours.add(new Point(x + 1, y));
        neighbours.add(new Point(x, y - 1));
        neighbours.add(new Point(x, y + 1));
        neighbours.add(new Point(x - 1, y - 1));
        neighbours.add(new Point(x + 1, y - 1));
        neighbours.add(new Point(x - 1, y + 1));
        neighbours.add(new Point(x + 1, y + 1));

        return neighbours;
    }


    /**
     * Returns a list with free cells around a given point.
     *
     * @param seed The point to search at.
     * @return The list of free neighbours found.
     */
    public List<Point> getFreeNeighbours(Point seed) {
        List<Point> neighbours = getNeighbours(seed);
        List<Point> freeNeighbours = new LinkedList<Point>();

        for (Point neighbour : neighbours) {
            if (this.cellAt(neighbour).isFree()) {
                // If this is a corner we must check if one of the surrounding
                // cells are also free.
                Point difference = neighbour.minus(seed).toPoint();
                if (difference.getX() != 0 && difference.getY() != 0) {
                    Point surroundingSameX =
                       new Point(neighbour.getX(), seed.getY());
                    Point surroundingSameY =
                        new Point(seed.getX(), neighbour.getY());

                    if (this.cellAt(surroundingSameX).isFree()
                            && this.cellAt(surroundingSameY).isFree()) {
                        freeNeighbours.add(neighbour);
                    }
                } else {
                    freeNeighbours.add(neighbour);
                }
            }
        }

        return freeNeighbours;
    }


    /**
     * Returns a list of occupied cells around a given point.
     *
     * @param seed The point to search at.
     * @return The list of free neighbours found.
     */
    public List<Point> getOccupiedNeighbours(Point seed) {
        List<Point> neighbours = getNeighbours(seed);
        List<Point> occupiedNeighbours = new LinkedList<Point>();

        for (Point neighbour : neighbours) {
            if (!this.cellAt(neighbour).isFree()) {
                occupiedNeighbours.add(neighbour);
            }
        }

        return occupiedNeighbours;
    }


    /**
     * Returns all the cells traversed by a path between two points.
     *
     * @param source The source point of the path
     * @param dest The destination point
     * @return A set with all the traversed cells.
     */
    public Set<Cell> pathTransversedCells(Point source, Point dest) {

        if (!this.isInside(source) || !this.isInside(dest)) {
            throw new IndexOutOfBoundsException(source + " " + dest);
        }

        Set<Cell> cells = new HashSet<Cell>();

        // Path's direction vector
        Vector direction = dest.minus(source).normalize();

        Cell sourceCell = cellAt(source);
        Cell destCell = cellAt(dest);

        // Add the first cell to the list
        cells.add(cellAt(source));

        Cell currentCell = sourceCell;
        Point previous = source;
        while (currentCell != null && !currentCell.equals(destCell)) {
            // Move the current point forward along the direction vector
            Point current = previous.plus(direction);
            currentCell = isInside(current) ? cellAt(current) : null;

            assert (Math.abs((int) current.getX() - (int) previous.getX()) < 2)
                : "Source: " + source
                    + "\nDestination: " + dest
                    + "\nCurrent: " + current
                    + "\nPrevious: " + previous
                    + "\nDirection: " + direction;
            assert (Math.abs((int) current.getY() - (int) previous.getY()) < 2)
                : "Source: " + source
                    + "\nDestination: " + dest
                    + "\nCurrent: " + current
                    + "\nPrevious: " + previous
                    + "\nDirection: " + direction;

            // If cells are on top of each other...
            if ((int) previous.getX() == (int) current.getX()) {
                if ((int) previous.getY() != (int) current.getY()) {
                    if (currentCell != null) {
                        cells.add(currentCell);
                    }
                }
            } else if ((int) previous.getY() == (int) current.getY()) {
                // ... else, if cells are side by side...
                if (currentCell != null) {
                    cells.add(currentCell);
                }
            } else {
                // ... otherwise, cells are diagonal to each other and we have
                // to find if the path crosses some other cell in the middle
                // First, let's discover which corner they have in common
                Point corner = null;
                if (current.getX() > previous.getX()) {
                    if (current.getY() > previous.getY()) {
                        corner = new Point((int) current.getX(),
                                (int) current.getY());
                    } else {
                        corner = new Point((int) current.getX(),
                                (int) previous.getY());
                    }
                } else {
                    if (current.getY() > previous.getY()) {
                        corner = new Point((int) previous.getX(),
                                (int) current.getY());
                    } else {
                        corner = new Point((int) previous.getX(),
                                (int) previous.getY());
                    }
                }

                // Find the point inside the line that is nearest the corner.
                // The cell where that point is located, is the cell in the
                // middle we are looking for.
                Line line = new Line(previous, direction);
                Point nearest = line.nearestPointTo(corner);

                if (!nearest.equals(corner)) {
                    Cell nearestCell = cellAt(nearest);
                    cells.add(nearestCell);

                    // Already found the last cell? Don't add currentCell then.
                    if (nearestCell.equals(destCell)) {
                        break;
                    }
                }

                if (currentCell != null) {
                    cells.add(currentCell);
                }
            }

            previous = current;
        }

        return cells;
    }


    /**
     * Checks if a path between two points is obstructed by some static entity.
     *
     * @param source The source point of the path
     * @param dest The destination point
     * @return True if the path is obstructed, false otherwise
     */
    public boolean isPathObstructed(Point source, Point dest) {

        if (!this.isInside(source) || !this.isInside(dest)) {
            return false;
        }

        Set<Cell> transversedCells = this.pathTransversedCells(source, dest);
        for (Cell cell : transversedCells) {
            if (!cell.isFree()) {
                return true;
            }
        }

        return false;
    }


    /**
     * Calculates a path from one location to another.
     * Assumes that such a path exists!
     *
     * @param source The source location
     * @param target The destination point
     * @return The path
     * @throws NoPathFoundException when no path was found
     */
    public Path<Point> findPath(Point source, Point target)
    throws NoPathFoundException {
        Path<Point> path = new AStarSearch<Point>(
                this.navGraph,
                source,
                new FindTargetCondition(this.navGraph, target),
                new ManhattanHeuristic()).search(new SimpleWeight(0));

        return path;
    }


    /**
     * Calculates shortest a path from some location to the nearest first-aid
     * kit.
     *
     * @param source The source location
     * @return The path
     * @throws NoPathFoundException when no path was found
     */
    public Path<Point> findPathToHealthItem(Point source)
    throws NoPathFoundException {
        return new DijkstraSearch<Point>(
                this.navGraph,
                source,
                new FindHealthCondition(this)).search(new SimpleWeight(0));
    }


    /**
     * Calculates shortest a path from some location to the nearest weapon.
     *
     * @param source The source location
     * @return The path
     * @throws NoPathFoundException when no path was found
     */
    public Path<Point> findPathToWeapon(Point source)
    throws NoPathFoundException {
        return new DijkstraSearch<Point>(
                this.navGraph,
                source,
                new FindWeaponCondition(this)).search(new SimpleWeight(0));
    }


    /**
     * Creates the navigation graph for this map.
     *
     * @param seed The point where the graph creation begins.
     */
    public void buildNavGraph(Point seed) {
        Point seedCenter = seed.displace(0.5, 0.5);

        if (!navGraph.containsNode(seedCenter)) {
            navGraph.add(new Graph.Node<Point>(seedCenter));
        }

        // For each free neighbor (i.e., a cell unoccupied by any building of
        // any kind)...
        List<Point> neighbours = getFreeNeighbours(seed);
        for (Point neighbour : neighbours) {
            Point cellCenter = neighbour.displace(0.5, 0.5);

            // If the neighbor is not in the graph...
            if (!navGraph.containsNode(cellCenter)) {
                // Add the neighbor and the edge connecting to it.
                navGraph.add(new Graph.Node<Point>(cellCenter));
                Graph.Edge edge = new Graph.Edge(navGraph.get(seedCenter),
                        navGraph.get(cellCenter),
                        new SimpleWeight(seedCenter.distanceTo(cellCenter)));
                navGraph.addEdge(edge);

                // Recursive call on the neighbor
                buildNavGraph(neighbour);
            } else if (!navGraph.containsAnyEdge(cellCenter, seedCenter)) {
                // However, even if the neighbor is already on the graph, we
                // might still have to create the edge.
                Graph.Edge edge = new Graph.Edge(navGraph.get(seedCenter),
                        navGraph.get(cellCenter),
                        new SimpleWeight(seedCenter.distanceTo(cellCenter)));
                navGraph.addEdge(edge);
            }
        }
    }


    /**
     * Returns the cell at position (x, y).
     *
     * @param x The x coordinate of the cell
     * @param y The y coordinate of the cell
     * @return The cell at position (x, y)
     */
    public Cell cellAt(int x, int y) {
        return this.grid[y + 1][x + 1];
    }


    /**
     * Returns the cell at the position given by the point.
     *
     * @param point The point with the coordinates of the cell.
     * @return The cell at the desired position
     */
    public Cell cellAt(Point point) {
        return this.grid[(int) point.getY() + 1][(int) point.getX() + 1];
    }


    /**
     * @return The number of cells along the horizontal axis.
     */
    public int getWidth() {
        return this.grid[0].length - 2;
    }


    /**
     * @return The number of cells along the vertical axis.
     */
    public int getHeight() {
        return this.grid.length - 2;
    }


    /**
     * @return The size of the map
     */
    public Size getSize() {
        return new Size(this.getWidth(), this.getHeight());
    }

    // FIXME Remove Graphics2D dependency from Map
    /**
     * Draws the map.
     *
     * @param graphics Graphics2D instance to draw.
     */
    public void draw(Graphics2D graphics) {
        for (InventoryItem item : this.items) {
            if (item.isActive()) {
                item.render(graphics);
            }
        }
    }


    /**
     * @return A simple textual representation of the map.
     */
    @Override
    public String toString() {
        String result = "";
        for (int i = this.getHeight() - 1; i >= 0; i--) {
            for (int j = 0; j < this.getWidth(); j++) {
                if (!this.cellAt(j, i).isFree()) {
                    result += "X ";
                } else {
                    result += ". ";
                }
            }
            result += "\n";
        }
        return result;
    }


    /**
     * Checks if an entire rectangle-shaped area is free of walls, buildings and
     * other static entities in general.
     *
     * @param location The top-left corner of the area.
     * @param size The size of the area.
     * @return true if the area is clear, false otherwise.
     */
    private boolean isAreaClear(Point location, Size size) {
        for (int i = 0; i < size.getHeight(); i++) {
            for (int j = 0; j < size.getWidth(); j++) {
                Cell cell = cellAt((int) (j + location.getX()),
                        (int) (i + location.getY()));

                if (!cell.isFree()) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Checks if a point lies inside the grid.
     *
     * @param point The point to check.
     * @return true if the point lies inside the grid, false otherwise.
     */
    private boolean isInside(Point point) {
        return point.getX() >= 0 && point.getX() < this.getWidth()
            && point.getY() >= 0 && point.getY() < this.getHeight();
    }


    /**
     * A Map::Cell represents a cell of the grid map. A cell can be in two
     * states: free or occupied by a static entity (wall, tree, building, etc).
     * A cell is considered free even if a moving agent is currently positioned
     * inside it - for all we care, cells are occupied by static entities only.
     *
     * A cell can also have exactly one inventory item (weapon or first-aid
     * kit).
     */
    public static class Cell implements Comparable<Cell> {

        /**
         * Creates a new cell at the given location.
         *
         * @param location Cell's location
         */
        public Cell(Point location) {
            this.location = location;
        }


        /**
         * @return The entity occupying this cell
         */
        public StaticEntity getEntity() {
            return this.entity;
        }


        /**
         * Sets the entity located at this cell.
         *
         * @param entity The entity occupying this cell.
         */
        public void setEntity(StaticEntity entity) {
            if (this.item == null) {
                this.entity = entity;
            } else {
                throw new RuntimeException("cell already contains an item!");
            }
        }


        /**
         * @return The inventory item in this cell
         */
        public InventoryItem getItem() {
            return this.item;
        }


        /**
         * Sets the inventory item in this cell, but only if:
         *
         * - The cell is empty;
         * - The cell doesn't contain any item.
         *
         * @param item The item that is occupying the cell
         * @return true if the item was set, false otherwise.
         */
        public boolean setItem(InventoryItem item) {
            if (this.isFree() && this.item == null) {
                this.item = item;
                return true;
            }
            return false;
        }


        /**
         * Removes the inventory item in this cell.
         */
        public void removeItem() {
            this.item = null;
        }


        /**
         * Checks if this cell is free.
         *
         * @return True if the cell is free, false otherwise
         */
        public boolean isFree() {
            return entity == null;
        }


        /**
         * @return The cell's location
         */
        public Point getLocation() {
            return this.location;
        }


        /**
         * @return The cell's center
         */
        public Point getCenter() {
            return this.location.displace(0.5, 0.5);
        }

        /**
         * Checks if two cells are equal.
         *
         * @param other The other cell to compare to
         * @return true if the cells are the same, false otherwise
         */
        @Override
        public boolean equals(Object other) {
            // Since there cannot be two cells with the same location, use
            // default equals().
            return super.equals(other);
        }


        /**
         * @return The hashCode of this Cell.
         */
        @Override
        public int hashCode() {
            return super.hashCode();
        }


        /**
         * Compares two cells.
         *
         * @param other The other cell to compare to.
         * @return < 0 if smaller, 0 if equal or > 0 if bigger
         */
        public int compareTo(Cell other) {
            return this.location.compareTo(other.location);
        }


        /**
         * @return A string representation of this cell.
         */
        @Override
        public String toString() {
            return "Cell[" + this.location + "]";
        }


        /** This cell's entity. If null, the cell is not occupied. */
        private StaticEntity entity;

        /** The inventory item occupying this cell, if any. */
        private InventoryItem item;

        /** This cell's location. */
        private Point location;
    }

    /** The map grid - a 2D array of Map::Cells. */
    private Cell[][] grid;

    /** The navigational graph of this map. */
    private Graph<Graph.Node<Point>, Graph.Edge> navGraph =
        new Graph<Graph.Node<Point>, Graph.Edge>();

    /** The items lying around in the floor waiting for someone to pick them. */
    private List<InventoryItem> items = new LinkedList<InventoryItem>();

    /** Static entities lying inside the map. */
    private List<StaticEntity> staticEntities = new LinkedList<StaticEntity>();

    /*
    // XXX APAGAR
    public void drawPoint(Point point, Graphics2D graphics) {
        int x1 = (int) (point.getX() * 32);
        int y1 = (int) ((this.getHeight() - point.getY()) * 32);

        graphics.fillOval(x1 - 4, y1 - 4, 8, 8);
    }


    public void drawLine(Point source, Point dest, Graphics2D graphics) {
        int x1 = (int) (source.getX() * 32);
        int y1 = (int) ((this.getHeight() - source.getY()) * 32);

        int x2 = (int) (dest.getX() * 32);
        int y2 = (int) ((this.getHeight() - dest.getY()) * 32);

        graphics.drawLine(x1, y1, x2, y2);
    }


    private void drawNavGraph(Graphics2D graphics) {
        for (NavNode entry : navGraph.nodes()) {
            drawPoint(entry.getKey(), graphics);

            for (Graph.Node<Point> neighbour : navGraph.nodeNeighbours(entry)) {
                drawLine(entry.getKey(), neighbour.getKey(), graphics);
            }
        }
    }


    public void drawPath(Graphics2D graphics, List<Point> path) {
        Point previous = path.get(0);
        for (Point point : path) {
            drawLine(previous, point, graphics);
            previous = point;
        }
    }


    private void drawCells(Graphics2D graphics) {
        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getHeight(); j++) {
                graphics.drawRect(i * 32, j * 32, 32, 32);
            }
        }
    }


    public void fillCell(Point location, Graphics2D graphics) {
        graphics.setColor(Color.red);
        graphics.fillRect((int) location.getX() * 32, (int) (this.getHeight() - location.getY() - 1) * 32, 32, 32);
        graphics.setColor(Color.black);
    }
*/

    

        // drawNavGraph(graphics);
        /*
         * Point source = new Point(16.61006785232117, 13.396345419197429);
         * Point dest = new Point(14.177622255641971, 15.152252142003618); //
         * drawCells(graphics); Set<Cell> transversedCells =
         * this.pathTransversedCells(source, dest); for (Cell cell :
         * transversedCells) { fillCell(cell.getLocation(), graphics); }
         * drawLine(source, dest, graphics);
         */

    

}
