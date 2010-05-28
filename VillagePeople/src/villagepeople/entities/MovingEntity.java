
package villagepeople.entities;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import villagepeople.events.UpdateEventArgs;
import villagepeople.game.VillageGame;
import villagepeople.navigation.Map.Cell;
import villagepeople.settings.Settings;
import explicitlib.event.Event;
import explicitlib.geometry.Line;
import explicitlib.geometry.Point;
import explicitlib.geometry.Vector;

/**
 * A moving entity is an entity that can - that's right, move. Its movement
 * tries to imitate the laws of physics as closely as possible - but not that
 * close.
 */
public abstract class MovingEntity extends Entity {

	/** Maximum speed. */
    private static final double MAX_SPEED =
        Settings.getDouble("entities.bot.maxSpeed");


    /** Maximum turn rate. */
    private static final double MAX_TURN_RATE =
        Settings.getDouble("entities.bot.maxTurnRate");


    /** Bounding radius. */
    private static final double BOUNDING_RADIUS =
        Settings.getDouble("entities.bot.boundingRadius");

    /**
     * Creates a new moving entity with the given parameters.
     *
     * @param game The game reference.
     * @param location The initial location of the entity.
     * @param velocity Entity's initial velocity.
     * @param rotation Entity's initial rotation.
     * @param maxSpeed Entity's maximum speed.
     * @param maxTurnRate Entity's maximum turn rate.
     * @param boundingRadius Entity's bounding radius.
     */
    public MovingEntity(VillageGame game, Point location, Vector velocity, double rotation) {
        super(location);
        this.game = game;        
        this.velocity = velocity;
        this.rotation = rotation;
        this.maxSpeed = MAX_SPEED;
        this.maxTurnRate = MAX_TURN_RATE;
        this.boundingRadius = BOUNDING_RADIUS;
    }
    
    /**
     * Creates a new moving entity with the given parameters.
     *
     * @param game The game reference.
     * @param location The initial location of the entity.
     * @param velocity Entity's initial velocity.
     * @param rotation Entity's initial rotation.
     * @param maxSpeed Entity's maximum speed.
     * @param maxTurnRate Entity's maximum turn rate.
     * @param boundingRadius Entity's bounding radius.
     */
    public MovingEntity(VillageGame game, Point location,
            Vector velocity, double rotation, double maxSpeed,
            double maxTurnRate, double boundingRadius) {
        super(location);
        this.game = game;
        this.velocity = velocity;
        this.rotation = rotation;
        this.maxSpeed = maxSpeed;
        this.maxTurnRate = maxTurnRate;
        this.boundingRadius = boundingRadius;
    }


    /**
     * Updates the position of this entity.
     *
     * @param timeElapsed Time elapsed since this method was last called.
     */
    public void update(long timeElapsed) {
        this.setLocation(this.getLocation().plus(
                this.velocity.multiplyBy(timeElapsed / 1000.0)));

        this.onUpdate().fire(new UpdateEventArgs(timeElapsed));
    }


    /**
     * Converts a given point from the coordinate system local to the entity,
     * to the world's coordinate system.
     *
     * @param point The point to convert.
     * @return The same point, at the world's coordinate system.
     */
    public Point toWorldCoordinates(Point point) {
        return point.rotate(this.rotation).plus(getLocation());
    }


    /**
     * Checks if the entity can move directly to the given target point.
     *
     * @param target The destination point.
     * @return true if the entity can move in a straight line, false otherwise.
     */
    public boolean canMoveTo(Point target) {
        return canMoveBetween(getLocation(), target);
    }


    /**
     * Checks if the entity can move from one point to another, in a straight
     * line.
     *
     * @param from The source point of the path.
     * @param dest The destination of the path.
     * @return true if the entity can move in a straight line, false otherwise.
     */
    public boolean canMoveBetween(Point from, Point dest) {
        if (from.equals(dest)) {
            return true;
        }

        // The entity can move if its bounding circle will never collide with
        // any wall or some other kind of static entity.

        Vector path = dest.minus(from).normalize();
        Vector side = path.perp();

        Point front = from.plus(path.multiplyBy(boundingRadius));

        Line sideLine = new Line(from, side);
        List<Point> points = sideLine.intersectionCircle(from, boundingRadius);
        points.add(front);

        double distX = dest.getX() - from.getX();
        double distY = dest.getY() - from.getY();

        for (Point source : points) {
            Point target = source.displace(distX, distY);

            if (game.getMap().isPathObstructed(source, target)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Checks if some other entity is in front of this entity.
     *
     * @param other The other entity.
     * @return true it it is in front of this entity, false otherwise.
     */
    public boolean isInFront(Entity other) {
        Vector toOther = other.getLocation().minus(this.getLocation());
        return this.getHeadingVector().dot(toOther) > 0;
    }


    /**
     * Checks if some other entity is behind of this entity.
     *
     * @param other The other entity.
     * @return true it it is behind this entity, false otherwise.
     */
    public boolean isBehind(Entity other) {
        return !isInFront(other);
    }


    /**
     * Returns all the cells that this entity must pass by to go to a given
     * destination point.
     *
     * @param dest The destination point.
     * @return A set with all the traversed cells.
     */
    public Set<Cell> pathTransversedCells(Point dest) {
        Set<Cell> cells = new TreeSet<Cell>();

        // The cells that the entity will occupy are the cells that its
        // bounding circle will occupy.

        Vector path = dest.minus(getLocation()).normalize();
        Vector side = path.perp();

        Point front = getLocation().plus(path.multiplyBy(boundingRadius));

        Line sideLine = new Line(getLocation(), side);
        List<Point> points = sideLine.intersectionCircle(getLocation(),
                boundingRadius);
        points.add(front);

        double distX = dest.getX() - getLocation().getX();
        double distY = dest.getY() - getLocation().getY();

        for (Point source : points) {
            Point target = source.displace(distX, distY);

            cells.addAll(game.getMap().pathTransversedCells(source, target));
        }

        return cells;
    }


    /**
     * @return The cell that the entity is currently occupying.
     */
    public Set<Point> getOccupyingCells() {
        Set<Point> points = new TreeSet<Point>();

        Point front = getLocation().plus(new Vector(boundingRadius, 0));
        Point left = getLocation().plus(new Vector(0, boundingRadius));
        Point right = getLocation().plus(new Vector(0, -boundingRadius));
        Point back = getLocation().plus(new Vector(-boundingRadius, 0));

        points.add(game.getMap().cellAt(front).getLocation());
        points.add(game.getMap().cellAt(left).getLocation());
        points.add(game.getMap().cellAt(right).getLocation());
        points.add(game.getMap().cellAt(back).getLocation());

        Point currentCell = game.getMap().cellAt(getLocation()).getLocation();
        Point northeast = currentCell.displace(1, 1);
        Point northwest = currentCell.displace(0, 1);
        Point southeast = currentCell.displace(1, 0);
        Point southwest = currentCell.displace(0, 0);

        if (getLocation().distanceTo(northeast) < this.boundingRadius) {
            points.add(northeast);
        }

        if (getLocation().distanceTo(northwest) < this.boundingRadius) {
            points.add(currentCell.displace(-1, 1));
        }

        if (getLocation().distanceTo(southeast) < this.boundingRadius) {
            points.add(currentCell.displace(1, -1));
        }

        if (getLocation().distanceTo(southwest) < this.boundingRadius) {
            points.add(currentCell.displace(-1, -1));
        }

        return points;
    }


    /**
     * @return Entity's heading vector.
     */
    public Vector getHeadingVector() {
        return new Vector(Math.cos(rotation), Math.sin(rotation));
    }


    /**
     * @return Entity's current velocity.
     */
    public Vector getVelocity() {
        return velocity;
    }


    /**
     * Sets the entity's velocity to a new value.
     *
     * @param velocity The new velocity.
     */
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }


    /**
     * @return Entity's current rotation angle.
     */
    public double getRotation() {
        return rotation;
    }


    /**
     * Alters the entity's rotation angle to a new value.
     *
     * @param rotation The new rotation angle.
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }


    /**
     * @return Entity's maximum allowed speed.
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }


    /**
     * @return Entity's bounding radius.
     */
    public double getBoundingRadius() {
        return this.boundingRadius;
    }


    /**
     * @return The game reference.
     */
    public VillageGame getGame() {
        return this.game;
    }


    /**
     * {@inheritDoc}
     *
     * The current location is saved because it may be restored if a collision
     * occurs.
     */
    @Override
    public void setLocation(Point location) {
        assert !Double.isNaN(location.getX() + location.getY());

        // Save previous location
        this.previousLocation = this.getLocation();
        super.setLocation(location);
    }


    /**
     * Restores the previous location of the entity.
     */
    public void restoreLocation() {
        this.setLocation(this.getPreviousLocation());
        this.previousLocation = this.getLocation();
    }


    /**
     * @return Entity's previous location.
     */
    public Point getPreviousLocation() {
        return this.previousLocation;
    }


    /**
     * @return the "on collision with StaticEntity" event.
     */
    public Event onCollisionWithStaticEntity() {
        return this.onCollisionWithStaticEntity;
    }


    /**
     * @return the "on update" event.
     */
    public Event onUpdate() {
        return this.onUpdate;
    }


    /** Game reference. */
    protected VillageGame game;

    /** Current velocity. */
    protected Vector velocity;

    /** Current rotation angle. */
    protected double rotation;

    /** The maximum allowed speed at which this entity may move. */
    protected double maxSpeed;

    /** The maximum rate (radians/second) at which this entity can rotate. */
    protected double maxTurnRate;

    /** Entity's bounding radius. */
    protected double boundingRadius;

    /** Entity's previous location. */
    protected Point previousLocation;

    /** Event fired when this moving entity collides with some static entity. */
    private Event onCollisionWithStaticEntity = new Event(this);

    /** Event fired after this moving entity updates itself. */
    private Event onUpdate = new Event(this);
}
