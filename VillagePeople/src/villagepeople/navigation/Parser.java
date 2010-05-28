package villagepeople.navigation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import villagepeople.entities.FirstAidItem;
import villagepeople.entities.InventoryItem;
import villagepeople.entities.Wall;
import villagepeople.entities.WeaponItem;
import villagepeople.entities.WeaponItem.WeaponType;
import villagepeople.exceptions.ParseException;
import explicitlib.geometry.Point;
import explicitlib.geometry.Size;

/**
 * The map parser. Contains methods to parse a .map file and create the
 * corresponding Map object.
 */
public class Parser {

    /**
     * Parses a .map file and returns the corresponding Map instance.
     *
     * @param fileName The name of the .map file.
     * @return The newly created map.
     * @throws IOException if there is an IO error with the file.
     * @throws ParseException if there is an error parsing the file.
     */
    public Map parse(String fileName) throws IOException, ParseException {
        Map map = null;

        BufferedReader in = new BufferedReader(new FileReader(fileName));

        String line = "";

        try {
            while ((line = in.readLine()) != null) {
                line = line.trim();

                // Empty lines are ignored
                if (line.length() == 0 || line.charAt(0) == '#') {
                    continue;
                }

                // Splits the line in "command arguments"
                Pattern pattern = Pattern.compile("^([\\w-]+)\\s+(.*)$");
                Matcher matcher = pattern.matcher(line);
                matcher.find();

                String command = matcher.group(1).toLowerCase();
                String parameters = matcher.group(2).trim();


                // Call the method that will handle this specific command
                if (command.equals("cells")) {
                    map = this.parseCells(parameters);
                } else if (command.equals("wall")) {
                    this.parseWall(map, parameters);
                } else if (command.equals("first-aid")) {
                    this.parseFirstAidKit(map, parameters);
                } else if (command.equals("weapon")) {
                    this.parseWeapon(map, parameters);
                } else {
                    logger.warn("Parser.parse(): Unknown command - " + command);
                }

            }
        } catch (ParseException ex) {
            throw (ParseException) new ParseException("Error parsing line '"
                    + line + "' of file '" + fileName + '"').initCause(ex);
        } finally {
            in.close();
        }

        // Parsing done. Now build the navigational graph.
        buildNavGraph(map);

        in.close();
        return map;
    }


    /**
     * Parses the "cells" command inside a .map file and creates a Map
     * instance with the specified size.
     *
     * @param arguments Arguments to the cells command.
     * @return A new map, with the given size.
     * @throws ParseException if there is an error parsing the arguments.
     */
    private Map parseCells(String arguments) throws ParseException {

        if (arguments.isEmpty()) {
            throw new ParseException("No arguments supplied.");
        }

        Pattern pattern = Pattern.compile("^(\\d+)\\s*x\\s*(\\d+)$");
        Matcher matcher = pattern.matcher(arguments);
        Map map = null;

        if (matcher.find()) {
            int width = Integer.parseInt(matcher.group(1));
            int height = Integer.parseInt(matcher.group(2));

            map = new Map(width, height);
        } else {
            throw new ParseException("Couldn't parse the number of cells.");
        }

        return map;
    }


    /**
     * Parses the "wall" command inside a .map file, creating a new Wall
     * in the given Map.
     *
     * @param map The map reference where the wall will be added to
     * @param arguments Arguments to the wall command.
     * @throws ParseException if there is an error parsing the arguments.
     */
    private void parseWall(Map map, String arguments) throws ParseException {

        if (arguments.isEmpty()) {
            throw new ParseException("No arguments supplied.");
        }

        Pattern pattern = Pattern.compile("\\(.*?\\)");
        Matcher matcher = pattern.matcher(arguments);

        List<Point> points = new ArrayList<Point>();
        while (matcher.find()) {
            points.add(stringToPoint(matcher.group()));
        }

        // Creates a wall between every two points
        Point previous = points.remove(0);
        if (points.isEmpty()) {
            buildSimpleWall(map, previous, previous);
        } else {
            boolean skipFirst = false;
            for (Point point : points) {
                buildWallBetween(map, previous, point, skipFirst);
                previous = point;
                skipFirst = true;
            }
        }
    }


    /**
     * Parses the first-aid-kit command inside a .map file and adds the
     * corresponding item to the specified location of the given map.
     *
     * @param map The map reference where the first aid kit will be added to.
     * @param arguments Arguments to the command.
     * @throws ParseException if there is an error parsing the arguments.
     */
    private void parseFirstAidKit(Map map, String arguments)
        throws ParseException {

        if (arguments.isEmpty()) {
            throw new ParseException("No arguments supplied.");
        }

        Pattern pattern = Pattern.compile("\\(.*?\\)");
        Matcher matcher = pattern.matcher(arguments);

        if (matcher.find()) {
            Point location = stringToPoint(matcher.group());

            InventoryItem item = new FirstAidItem(map, location);

            if (item != null) {
                map.addItem(item);
            }
        } else {
            throw new ParseException("Couldn't parse the item's location");
        }
    }


    /**
     * Parses the weapon command inside a .map file and adds the
     * corresponding item to the specified location of the given map.
     *
     * @param map The map reference where the weapon will be added to.
     * @param arguments Arguments to the command.
     * @throws ParseException if there is an error parsing the arguments.
     */
    private void parseWeapon(Map map, String arguments)
        throws ParseException {

        if (arguments.isEmpty()) {
            throw new ParseException("No arguments supplied.");
        }

        WeaponType weapon = null;
        String weaponStr = arguments.split(" ")[0].toUpperCase();
        try {
            weapon = WeaponType.valueOf(weaponStr);
        } catch (IllegalArgumentException ex) {
            throw new ParseException("Unknown weapon: " + weaponStr);
        }

        Pattern pattern = Pattern.compile("\\(.*?\\)");
        Matcher matcher = pattern.matcher(arguments);

        if (matcher.find()) {
            Point location = stringToPoint(matcher.group());

            InventoryItem item = new WeaponItem(map, location, weapon);

            if (item != null) {
                map.addItem(item);
            }
        } else {
            throw new ParseException("Couldn't parse the item's location");
        }
    }


    /**
     * Invokes the creation of the navigational graph for the given map.
     * This method must be called only after the parsing.
     *
     * @param map The map the create the graph for.
     */
    private void buildNavGraph(Map map) {
        // Select a seed point
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.cellAt(x, y).isFree()) {
                    map.buildNavGraph(new Point(x, y));
                    return;
                }
            }
        }
    }


    /**
     * Creates a Point from its string representation: "(x, y)".
     *
     * @param ptStr The string representation of the point
     * @return The corresponding point.
     * @throws ParseException if there is an error parsing the string.
     */
    private Point stringToPoint(String ptStr) throws ParseException {
        Point point = null;

        Pattern pattern =
            Pattern.compile("\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
        Matcher matcher = pattern.matcher(ptStr);

        if (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));

            point = new Point(x, y);
        } else {
            throw new ParseException("Invalid syntax for point '"
                    + ptStr + "'");
        }

        return point;
    }


    /**
     * Builds a wall between points source and dest.
     *
     * @param map The map where the walls will be created.
     * @param source The source point.
     * @param dest The destination point.
     * @param skipFirst Don't try to build a wall in the first cell
     */
    private void buildWallBetween(Map map, Point source, Point dest,
            boolean skipFirst) {
        Point fromCenter = source.displace(0.5, 0.5);
        Point toCenter = dest.displace(0.5, 0.5);

        // Find the cells this wall goes through
        List<Point> cells = getWallCells(fromCenter, toCenter);

        if (skipFirst) {
            cells.remove(0);
        }

        // And build the necessary walls.
        // Note that horizontal and vertical neighbour cells will be merged
        // in the same wall.
        Point wallStart = null;
        Point wallEnd = null;
        for (Point cell : cells) {
            if (wallStart == null) {
                wallStart = cell;
            }

            // If this cell is at a new row and column, we have to create the
            // wall that spans from wallStart to wallEnd
            if (cell.getX() != wallStart.getX()
                    && cell.getY() != wallStart.getY()) {
                buildSimpleWall(map, wallStart, wallEnd);

                // The next wall will begin at this cell.
                wallStart = cell;
            }

            wallEnd = cell;
        }

        // Build the last wall
        buildSimpleWall(map, wallStart, wallEnd);
    }


    /**
     * Builds a simple wall (horizontal or vertical only) that spans from cells
     * source to dest.
     *
     * @param map The map where the wall is going to be added.
     * @param source One of the endpoints of the wall.
     * @param dest The other endpoint of the wall.
     */
    private void buildSimpleWall(Map map, Point source, Point dest) {
        int distX = (int) Math.abs(dest.getX() - source.getX());
        int distY = (int) Math.abs(dest.getY() - source.getY());

        // The corner will be the one with lowest x coordinate, or, in case
        // they are equal, the one with lowest y.
        Point corner = null;
        if (source.getX() < dest.getX()) {
            corner = source;
        } else if (source.getX() == dest.getX()) {
            corner = (source.getY() < dest.getY()) ? source : dest;
        } else {
            corner = dest;
        }

        Size size = new Size(distX + 1, distY + 1);
        Wall wall = new Wall(corner, size);

        // Adds the wall to the map
        if (!map.addEntity(wall)) {
            logger.warn("Parser.buildSimpleWall(): Failed to add wall: "
                    + wall);
        }
    }


    /**
     * Finds the cells that are occupied by a wall with endpoints source and
     * dest.
     *
     * @param source One of the endpoints of the wall.
     * @param dest The other endpoint of the wall.
     * @return The list of cells that the wall occupies, in order from source to
     *         dest.
     */
    private List<Point> getWallCells(Point source, Point dest) {
        // Base case: points are equal. Return one of them.
        if (source.equals(dest)) {
            List<Point> result = new ArrayList<Point>();
            result.add(source);
            return result;
        }

        // Base case: points are adjacent. Return [source, dest].
        if (adjacentCells(source, dest)) {
            List<Point> result = new ArrayList<Point>();
            result.add(source);
            result.add(dest);
            return result;
        }

        // Find the middle point between source and dest
        Point middle = Point.middle(source, dest);

        // Find the cells at that point
        List<Point> cellsAtMiddle = cellsAt(middle);

        // Which of the cells is nearer source and which is nearer dest?
        Point nearSource = getNearestCell(cellsAtMiddle, source);
        Point nearDest = getNearestCell(cellsAtMiddle, dest);

        // Invoke the function again with endpoints [source, nearSource] and
        // then with [nearDest, dest] and concatenate the results.
        List<Point> result = new ArrayList<Point>();
        result.addAll(getWallCells(source, nearSource));
        result.addAll(getWallCells(nearDest, dest));

        return result;
    }


    /**
     * Finds the cell that is nearest to some point.
     *
     * @param cells The list of cells to check
     * @param point The point to check.
     * @return The coordinates of the cell nearest to point.
     */
    private Point getNearestCell(List<Point> cells, Point point) {
        Point nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Point cell : cells) {
            double distance = point.distanceTo(cell);
            if (distance < minDistance) {
                nearest = cell;
                minDistance = distance;
            }
        }

        return nearest;
    }


    /**
     * Returns the list of cells present on a given point. Usually, the point
     * occupies only a single cell. However, if the point lies in a border
     * between two cells, they will both be returned. Likewise, if the point
     * occupies a corner, the four cells that share the corner will be returned.
     *
     * @param point The point to check.
     * @return A list of the cells present occupying the point.
     */
    private List<Point> cellsAt(Point point) {
        List<Point> cells = new ArrayList<Point>();

        double right = (int) point.getX() + 0.5;
        double left = (int) point.getX() - 0.5;
        double top = (int) point.getY() + 0.5;
        double bottom = (int) point.getY() - 0.5;

        // Does the point lie on a vertical border?
        if (point.getX() == (int) point.getX()) {

            // Does the point lie on a horizontal border?
            if (point.getY() == (int) point.getY()) {
                // It's a corner!
                cells.add(new Point(right, top));
                cells.add(new Point(left, top));
                cells.add(new Point(right, bottom));
                cells.add(new Point(left, bottom));
            } else {
                // Nope, it really is a vertical border.
                cells.add(new Point(right, top));
                cells.add(new Point(left, top));
            }
        } else if (point.getY() == (int) point.getY()) {
            // It's an horizontal border
            cells.add(new Point(right, top));
            cells.add(new Point(right, bottom));
        } else {
            // Or, it's in the middle of some cell.
            cells.add(new Point(right, top));
        }

        return cells;
    }


    /**
     * Are the cells that points source and dest occupy adjacent (neighbours)?
     *
     * @param source The first point.
     * @param dest The second point.
     * @return True if they are adjacent, false otherwise.
     */
    private boolean adjacentCells(Point source, Point dest) {
        Point fromCell = new Point((int) source.getX(), (int) source.getY());
        Point toCell = new Point((int) dest.getX(), (int) dest.getY());

        if (fromCell.equals(toCell)) {
            return false;
        }

        return (Math.abs(dest.getX() - source.getX()) <= 1)
            && (Math.abs(dest.getY() - source.getY()) <= 1);
    }


    /**
     * Class logger.
     */
    private static Logger logger = Logger.getLogger(Parser.class.getName());
}
