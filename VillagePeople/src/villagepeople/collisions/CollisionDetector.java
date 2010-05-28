package villagepeople.collisions;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import villagepeople.entities.Bot;
import villagepeople.entities.InventoryItem;
import villagepeople.game.VillageGame;
import explicitlib.geometry.Line;
import explicitlib.geometry.Point;
import explicitlib.geometry.Vector;
import villagepeople.navigation.Map;
import villagepeople.weaponry.Projectile;

/**
 * Detects all kinds of collisions.
 */
public class CollisionDetector {

    /**
     * Creates a new collision detector.
     *
     * @param game The game where this detector is going to be put to use.
     */
    public CollisionDetector(VillageGame game) {
        this.game = game;
    }


    /**
     * Detect every possible collision:
     * - Bot/Wall;
     * - Bot/Bot;
     * - Bot/Projectile;
     * - Bot/Inventory item;
     * - Projectile/Wall.
     *
     * @return The list of collisions found
     */
    public List<Collision> detectCollisions() {
        List<Collision> result = detectBotWallCollisions();
        result.addAll(detectBotBotCollisions());
        result.addAll(detectProjectileWallCollisions());
        result.addAll(detectBotProjectileCollisions());
        result.addAll(detectBotItemCollisions());

        return result;
    }


    /**
     * Detect collisions between bots.
     *
     * @return A list of bot/bot collisions
     */
    private List<Collision> detectBotBotCollisions() {
        List<Collision> result = new LinkedList<Collision>();
        List<Bot> bots = this.game.getBots();

        for (int i = 0; i < bots.size() - 1; i++) {
            for (int j = i + 1; j < bots.size(); j++) {
                Bot bot = bots.get(i);
                Bot other = bots.get(j);

                Vector toOther = other.getLocation().minus(bot.getLocation());
                double distance = toOther.magnitude();

                // If there is an overlapping, then there is a collision
                double overlapAmount = bot.getBoundingRadius()
                    + other.getBoundingRadius() - distance;
                if (overlapAmount > 0) {
                    result.add(new BotBotCollision(bot, other, toOther,
                            distance, overlapAmount));
                }
            }
        }

        return result;
    }


    /**
     * Detect collisions between bots and inventory items.
     *
     * @return A list of bot/inventory-item collisions
     */
    private List<Collision> detectBotItemCollisions() {
        List<Collision> result = new LinkedList<Collision>();

        for (Bot bot : this.game.getBots()) {
            Map.Cell cell = game.getMap().cellAt(bot.getLocation());

            if (cell.getItem() instanceof InventoryItem) {
                InventoryItem item = (InventoryItem) cell.getItem();
                if (item.isActive()) {
                    double distance = bot.getLocation().distanceTo(
                            cell.getCenter());
                    double overlapAmount = bot.getBoundingRadius()
                        + item.getBoundingRadius() - distance;
                    if (overlapAmount > 0) {
                        result.add(new BotItemCollision(bot, item));
                    }
                }
            }
        }

        return result;
    }


    /**
     * Detect collisions between bots and walls.
     *
     * @return A list of bot/wall collisions
     */
    private List<Collision> detectBotWallCollisions() {
        List<Collision> result = new LinkedList<Collision>();
        List<Bot> bots = this.game.getBots();

        for (Bot bot : bots) {
            Set<Point> occupied = bot.getOccupyingCells();
            for (Point cell : occupied) {
                if (!game.getMap().cellAt(cell).isFree()) {
                    result.add(new BotWallCollision(bot,
                            game.getMap().cellAt(cell).getEntity()));
                }
            }
        }

        return result;
    }


    /**
     * Detect collisions between projectiles and walls.
     *
     * @return A list of projectile/wall collisions
     */
    private List<Collision> detectProjectileWallCollisions() {
        List<Collision> result = new LinkedList<Collision>();

        for (Projectile projectile : this.game.getProjectiles()) {
            try {
                Map.Cell cell = game.getMap().cellAt(projectile.getLocation());
                if (!cell.isFree()) {
                    result.add(new ProjectileWallCollision(projectile,
                            cell.getEntity()));
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                // Remove the projectile from the world.
                game.removeProjectile(projectile);
            }
        }

        return result;
    }


    /**
     * Detect collisions between bots and projectiles.
     *
     * The projectile collided with some bot, if there is an intersection
     * between its path and the bot's bounding circle. If there is more than
     * one collision, the one closer to the source location is chosen.
     *
     * @return A list of bot/bot collisions
     */
    private List<Collision> detectBotProjectileCollisions() {
        List<Collision> result = new LinkedList<Collision>();

        for (Projectile projectile : game.getProjectiles()) {
            // The projectile's trajectory
            Line trajectory = new Line(projectile.getPreviousLocation(),
                    projectile.getHeadingVector());

            // The distance covered by the projectile during the last update()
            double coveredDistance = projectile.getPreviousLocation()
                .distanceTo(projectile.getLocation());

            // The bot that was hit
            Bot hitBot = null;

            // The distance between the point at the beginning of the
            // projectile's path, and the point where hitBot was hit.
            double hitDistance = Double.MAX_VALUE;

            // Iterate over the list of bots, to check if there was a collision
            // with any of them
            for (Bot bot : game.getBots()) {
                List<Point> collisionPoints = trajectory.intersectionCircle(
                        bot.getLocation(), bot.getBoundingRadius());

                for (Point point : collisionPoints) {

                    // If this point was behind the projectile, forget about it
                    Vector toPoint = point
                        .minus(projectile.getPreviousLocation());

                    double distanceToBeginning = projectile
                        .getPreviousLocation().distanceTo(point);
                    final double tolerance = 0.001;

                    if (projectile.getHeadingVector().dot(toPoint) < 0
                            && distanceToBeginning > tolerance) {
                        continue;
                    }

                    // If the bot was closer to the projectile's previous
                    // location than the current hitBot, replace it
                    if (coveredDistance > distanceToBeginning
                            && distanceToBeginning < hitDistance) {
                        hitDistance = distanceToBeginning;
                        hitBot = bot;
                    }
                }
            }

            if (hitBot != null) {
                result.add(new BotProjectileCollision(hitBot, projectile));
            }
        }

        return result;
    }

    /** The game. */
    private VillageGame game;
}
