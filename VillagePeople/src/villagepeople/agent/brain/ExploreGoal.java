package villagepeople.agent.brain;

import villagepeople.entities.Bot;
import explicitlib.geometry.Point;

public class ExploreGoal extends GoalComposite {

	public ExploreGoal(Bot bot) {
		super(bot);		
	}

	@Override
	public void activate() {
		this.setState(Goal.State.ACTIVE);
		removeAllSubgoals();		
		destination = owner.getGame().getRandomLocation();									
		addSubgoal(new FollowPathGoal(owner, destination));		
	}

	@Override
	public Goal.State process() {
		activateIfInactive();
		this.setState(processSubgoals());
		return this.getState();
	}

	@Override
	public void terminate() {		
		removeAllSubgoals();		
		this.setState(Goal.State.COMPLETED);
		super.terminate();
	}
	
	@Override
	public String toString() {	
		return this.getClass().getSimpleName()+": "+this.getState().toString();
	}
	

	private Point destination;	
}
