package villagepeople.weaponry;

import villagepeople.entities.Bot;
import villagepeople.entities.MovingEntity;
import villagepeople.entities.StaticEntity;
import villagepeople.events.CollisionWithStaticEntityEventArgs;
import villagepeople.events.HitEventArgs;
import villagepeople.game.VillageGame;
import explicitlib.event.Action;
import explicitlib.event.Event;
import explicitlib.event.EventArgs;
import explicitlib.geometry.Point;
import explicitlib.geometry.Vector;

/**
 * Represents a projectile fired by a weapon.
 *
 * A projectile is a moving entity, but, unlike bots, its velocity remains
 * constant until a collision happens. Then, the projectile explodes, inflicts
 * damage in hit bots and is removed from the world.
 */
public abstract class Projectile extends MovingEntity {

    /**
     * Creates and initializes a new projectile, ready to fly and hit someone.
     *
     * @param game The world where all the action is taking place.
     * @param shooter The agent that shot this projectile.
     * @param location The initial location of the projectile.
     * @param velocity The projectile's velocity vector.
     * @param rotation The initial angle of rotation of the projectile.
     * @param boundingRadius Projectile's bounding radius for collision
     *        detection.
     */
    public Projectile(VillageGame game, Bot shooter, Point location,
            Vector velocity, double rotation,
            double boundingRadius) {
        super(game, location, velocity, rotation, velocity.magnitude(),
                0, boundingRadius);

        this.shooter = shooter;

        this.onHitBot().attach(new Action() {
            @Override
            public void execute(Object source, EventArgs args) {
                Bot bot = ((HitEventArgs) args).getBot();
                hitBot(bot);
            }
        });

        this.onCollisionWithStaticEntity().attach(new Action() {
            @Override
            public void execute(Object source, EventArgs args) {
                StaticEntity entity =
                    ((CollisionWithStaticEntityEventArgs) args).getEntity();
                hitStaticEntity(entity);
            }
        });
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(long timeElapsed) {
        super.update(timeElapsed);
    }


    /**
     * @return the damage inflicted by this projectile, when it hits some bot.
     */
    public abstract int getDamage();


    /**
     * @return The bot that shot this projectile.
     */
    public Bot getShooter() {
        return shooter;
    }


    /**
     * @return the "On hit bot" event.
     */
    public Event onHitBot() {
        return this.onHitBot;
    }


    /**
     * Called when the projectile hits a bot. By default, inflicts damage in
     * the bot (if it's an opponent) and removes the projectile from the world.
     *
     * To obtain different behaviors (like explosions and such), override
     * the method.
     *
     * @param bot The bot that was hit
     */
    protected void hitBot(Bot bot) {
        game.removeProjectile(this);

        if (!this.shooter.isSameTeam(bot)) {
            bot.inflictDamage(this.getDamage());
        }
    }


    /**
     * Called when the projectile hits a wall or other kind of static entity
     * (i.e., everything in the map except bots and other projectiles).
     *
     * By default, removes the projectile from the game.
     *
     * @param entity The entity that was hit
     */
    protected void hitStaticEntity(StaticEntity entity) {
        game.removeProjectile(this);
    }


    /**
     * The bot that shot this projectile.
     */
    protected Bot shooter;


    /** Event fired when the projectile hits a bot. */
    protected Event onHitBot = new Event(this);
}
