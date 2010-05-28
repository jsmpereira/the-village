package villagepeople.agent.brain;

import java.util.ArrayList;
import java.util.List;

import villagepeople.entities.Bot;
import explicitlib.geometry.Point;
import explicitlib.geometry.Vector;

public class RotateFacing extends Goal{

	public RotateFacing(Bot bot, Point destination) {
		super(bot);		
		this.destination = destination;
	}

	@Override
	public void activate() {
		this.setState(Goal.State.ACTIVE);		
	}

	@Override
	public void addSubgoal(Goal goal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Goal getGoalInProcess() {		
		return this;
	}

	@Override
	public State process() {
		activateIfInactive();
		Vector direction = destination.minus(owner.getLocation());
		if(owner.rotateFacing(direction))
		{
			this.setState(Goal.State.COMPLETED);
		}
		return this.getState();
	}

	@Override
	public void terminate() {
		this.setState(Goal.State.COMPLETED);	
		super.terminate();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+": "+this.getState();
	}

	private Point destination;
}
