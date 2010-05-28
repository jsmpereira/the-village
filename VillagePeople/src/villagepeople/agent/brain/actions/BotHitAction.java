package villagepeople.agent.brain.actions;

import villagepeople.agent.brain.Goal;
import villagepeople.agent.brain.RotateFacing;
import villagepeople.entities.Bot;
import villagepeople.events.HitEventArgs;
import villagepeople.weaponry.Projectile;
import explicitlib.event.Action;
import explicitlib.event.EventArgs;


/**
 * Action to be executed by the brain when the bot is hit.
 */
public class BotHitAction implements Action {

    /**
     * Creates a new action for the given goal.
     * 
     * @param goal The goal that this action refers to.
     */
    public BotHitAction(Goal<Bot> goal) {
        this.goal = goal;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Object source, EventArgs args) {
        Bot bot = ((HitEventArgs) args).getBot();  
        Projectile projectile =
            ((HitEventArgs) args).getProjectile();
        
        if (!bot.isSameTeam(projectile.getShooter())) {
            if (bot.getHealth() <= 70 &&
                    bot.getTotalWeaponStrength() <= 0.6) {
                goal.addSubgoal(new RotateFacing(bot,
                        projectile.getShooter().getLocation()));
            }                                               
        }
    }

    /** The goal that this action refers to. */
    private Goal<Bot> goal;
}
