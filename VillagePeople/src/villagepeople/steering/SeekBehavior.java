package villagepeople.steering;

import villagepeople.entities.Bot;
import explicitlib.geometry.Point;
import explicitlib.geometry.Vector;


/**
 * The seek behavior.
 *
 * When this behavior is enabled, the agent moves directly to some target point
 * and stops when it gets there.
 *
 * @see http://www.red3d.com/cwr/steer/SeekFlee.html
 */
public class SeekBehavior extends SteeringBehavior {

    /**
     * {@inheritDoc}
     *
     * @param agent {@inheritDoc}
     * @param target The new destination point.
     */
    public SeekBehavior(Bot agent, Point target) {
        super(agent);

        this.target = target;
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

        double dist = toTarget.magnitude();
        double timeElapsedSeconds = timeElapsed / 1000.0;

        // If the agent is still away from the target...
        if (dist > agent.getMaxSpeed() * timeElapsedSeconds) {
            // ... calculate the desired velocity to reach the target
            return toTarget.multiplyBy(agent.getMaxSpeed()
                    / toTarget.magnitude());
        } else {
            // Otherwise, return the right velocity so that the agent reaches
            // the target during this iteration
            return toTarget.divideBy(timeElapsedSeconds);
        }
    }


    /**
     * The destination point that the bot is trying to reach.
     */
    private Point target;
}
