package villagepeople.agent.brain;

import java.util.ArrayList;
import java.util.List;

import villagepeople.entities.Bot;
import explicitlib.geometry.Point;
import explicitlib.graph.search.NoPathFoundException;

public class SeekPositionGoal extends Goal {	

	public SeekPositionGoal(Bot owner, Point destination) {
		super(owner);
		this.destination = destination;
	}

	@Override
	public void activate() {		
		this.setState(Goal.State.ACTIVE);		
		owner.seek(destination);							
	}

	@Override
	public State process() {
		activateIfInactive();								
		if(owner.getLocation().equals(destination))
			this.setState(Goal.State.COMPLETED);					
		return this.getState();
	}

	@Override
	public void terminate() {			
		owner.stop();
		super.terminate();
	}

	@Override
	public void addSubgoal(Goal goal) {
	}

	@Override
	public Goal getGoalInProcess() {		
		return this;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+": "+this.getState().toString()+": "+this.destination.toString();
	}
	
	private Point destination;
}
