package villagepeople.agent.brain.actions;

import villagepeople.agent.brain.Goal;
import villagepeople.agent.brain.RotateFacing;
import villagepeople.entities.Bot;
import villagepeople.events.CollisionWithStaticEntityEventArgs;
import villagepeople.events.HitEventArgs;
import villagepeople.weaponry.Projectile;
import explicitlib.event.Action;
import explicitlib.event.EventArgs;
import explicitlib.geometry.Point;


/**
 * Action to be executed by the brain when the bot collides with a wall.
 */
public class BotWallCollisionAction implements Action {

    /**
     * Creates a new action for the given goal.
     * 
     * @param goal The goal that this action refers to.
     */
    public BotWallCollisionAction(Goal<Bot> goal) {
        this.goal = goal;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Object source, EventArgs args) {
        Bot bot = (Bot) source;  
        
        Point point = bot.getGame().getMap()
            .cellAt(bot.getLocation()).getCenter();
        
        goal.setState(Goal.State.COMPLETED);        
        goal.addSubgoal(new RotateFacing(bot, point));
    }

    /** The goal that this action refers to. */
    private Goal<Bot> goal;
}
