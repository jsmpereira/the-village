
package villagepeople.steering;

import villagepeople.entities.Bot;
import villagepeople.settings.Settings;
import explicitlib.geometry.Point;
import explicitlib.geometry.Vector;

/**
 * The arrive behavior.
 *
 * Arrive is similar to seek, in that it moves the agent towards some target
 * point. However, unlike seek, arrive decreases the agent's velocity linearly
 * as the agent is getting closer and closer to its target, which results in a
 * smooth and realistic movement.
 *
 * @see http://www.red3d.com/cwr/steer/Arrival.html
 */
public class ArriveBehavior extends SteeringBehavior {

    /**
     * Controls the deceleration experienced by the bot when the arrive behavior
     * is active.
     */
    public static enum Deceleration {
        /**
         * Slow deceleration, i.e., start braking early.
         */
        SLOW(1),

        /**
         * Normal deceleration.
         */
        NORMAL(2),

        /**
         * Fast deceleration, i.e., start braking when the agent is close to the
         * target.
         */
        FAST(3);


        /**
         * Constructs a new Deceleration with the given deceleration factor.
         *
         * @param value The deceleration factor to use.
         */
        Deceleration(int value) {
            this.value = value;
        }


        /**
         * @return Deceleration factor of this instance.
         */
        public int getValue() {
            return value;
        }


        /**
         * Deceleration factor of this instance.
         */
        private int value;
    }


    /**
     * Constant factor to use when decelerating (bigger factor means less
     * deceleration and vice-versa).
     */
    private static final double DECELERATION_FACTOR =
        Settings.getDouble("steeringBehaviors.arrive.decelerationFactor");


    /**
     * How much distance from the target is considered far (> TOLERANCE) and
     * near (<= TOLERANCE) ?
     */
    private static final double TOLERANCE = 0.01;


    /**
     * {@inheritDoc}
     *
     * @param agent {@inheritDoc}
     * @param target The new target point.
     * @param deceleration The deceleration type to use by this instance.
     */
    public ArriveBehavior(Bot agent, Point target, Deceleration deceleration) {
        super(agent);

        this.target = target;
        this.deceleration = deceleration;
    }


    /**
     * {@inheritDoc}
     *
     * @param {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Vector calculateVelocity(long timeElapsed) {
        Vector toTarget = target.minus(this.agent.getLocation());

        // The distance to the target position
        double dist = toTarget.magnitude();

        // Are we still far away from the target?
        if (dist > TOLERANCE) {
            // calculate the speed required to reach the target given the
            // desired deceleration
            double speed = dist / deceleration.getValue() * DECELERATION_FACTOR;

            // Now, calculate the desired velocity
            return toTarget.multiplyBy(speed / dist);
        } else {
            // The agent is very close to the target!
            // Return the right velocity so that the agent reaches the target
            // during this iteration
            return toTarget.divideBy(timeElapsed / 1000.0);
        }
    }


    /**
     * The target point.
     */
    private Point target;


    /**
     * Deceleration type to use on arriving.
     */
    private Deceleration deceleration;
}
