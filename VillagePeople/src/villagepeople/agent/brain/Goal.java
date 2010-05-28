package villagepeople.agent.brain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import explicitlib.event.Action;
import explicitlib.event.Event;

import villagepeople.entities.Bot;
import villagepeople.entities.Entity;

/**
 * 
 * @author Joel Cordeiro
 *	
 * @param <T> Just to be clear that this goal belong to the bot
 */
public abstract class Goal<T extends Bot> {

	public static enum State 
	{
		INACTIVE,
		ACTIVE,
		COMPLETED,
		FAILED,
	};
	
	public Goal(Bot bot)
	{
		this.owner = bot;
		this.attachedActions = new HashMap<Event, List<Action>>();
	}	
	
	protected void activateIfInactive()
	{
		if(isInactive())		
			activate();
	}
	
	protected void reactivateIfFailed()
	{
		if(hasFailed())
			activate();
	}
	
	protected boolean isActive()
	{
		if(this.status == Goal.State.ACTIVE)
			return true;
		return false;
	}
	
	protected boolean isInactive()
	{
		if(this.status == Goal.State.INACTIVE)
			return true;
		return false;
	}
	
	protected boolean isCompleted()
	{
		if(this.status == Goal.State.COMPLETED)
			return true;
		return false;
	}
	
	protected boolean hasFailed()
	{
		if(this.status == Goal.State.FAILED)
			return true;
		return false;	
	}

	public void setState(Goal.State status)
	{	
		this.status = status;
	}
		
	protected Goal.State getState()
	{
		return this.status;
	}
	
	
	protected void attachActionToOwner(Event event, Action action)
	{
	    if (!this.attachedActions.containsKey(event)) {
	        this.attachedActions.put(event, new LinkedList<Action>());
	    }
	    
		this.attachedActions.get(event).add(action);
		event.attach(action);
	}
	
	protected void detachAllActions()
	{
	    for (Map.Entry<Event, List<Action>> pair : this.attachedActions.entrySet()) {
	        for (Action action : pair.getValue()) {
	            pair.getKey().detach(action);
	        }
	    }
	    
	    this.attachedActions.clear();
	}
	
	public abstract void addSubgoal(Goal goal);			
	public abstract Goal getGoalInProcess();
	public abstract void activate();
	public abstract State process();
	
	public void terminate() {
	    // XXX Joel: Será que não se poderia colocar aqui o setState(COMPLETED) ?
	    // E o detachAllActions() ? Já lá está: falta apagar noutros stitios onde já nao seja preciso
	    
	    this.detachAllActions();
	    assert this.attachedActions.isEmpty();
	}
	
	public abstract String toString();
	
	protected Bot owner;
	private State status = Goal.State.INACTIVE;
	private Map<Event, List<Action>> attachedActions;
}
