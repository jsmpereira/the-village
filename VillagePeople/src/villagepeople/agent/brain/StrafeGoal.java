package villagepeople.agent.brain;

import java.util.ArrayList;
import java.util.List;

import villagepeople.entities.Bot;
import explicitlib.geometry.Point;
import explicitlib.geometry.Vector;

public class StrafeGoal extends Goal{

	public StrafeGoal(Bot bot, Bot target) {
		super(bot);
		this.target = target;		
	}

	@Override
	public void activate() {		
		this.setState(Goal.State.ACTIVE);									
		// to mix between left and right
		if(clockWise)
		{	
			strafeDestination = owner.toWorldCoordinates(new Point(0, -1));			
			//if he can go right
			if(strafeDestination!=null)
			{							
				owner.seek(strafeDestination);	
				strafeTimes++;
				timescale = tolerance+(1000*((long)owner.getLocation().distanceTo(strafeDestination)/(long)owner.getMaxSpeed())); 		
				timeStarted = System.currentTimeMillis();
			}
			// else change the side
			else
			{
				clockWise = !clockWise;
				this.setState(Goal.State.INACTIVE);				
			}
		}
		else
		{
			strafeDestination = owner.toWorldCoordinates(new Point(0, 1));  
			// if he can go left
			if(strafeDestination!=null)
			{										
				owner.seek(strafeDestination);
				strafeTimes++;
				timescale = tolerance+(1000*((long)owner.getLocation().distanceTo(strafeDestination)/(long)owner.getMaxSpeed())); 		
				timeStarted = System.currentTimeMillis();
			}
			else
			{
				clockWise = !clockWise;
				this.setState(Goal.State.INACTIVE);				
			}
		}
	}

	@Override
	public State process() {
		activateIfInactive();				
		activateIfInactive();
		//if target goes out of view the goal is completed
		if(!owner.isInFOV(target))
		{
			this.setState(Goal.State.FAILED);
			return this.getState();
		}
		
		//if the bot reaches the target position set status to inactive
		//to reactive the goal		
		if(owner.getLocation().equals(strafeDestination))
		{			
			this.setState(Goal.State.INACTIVE);
			clockWise = !clockWise;
		}					
		
		//if he cannot strafe, or has done more than 2 strafes, consider it completed
		if((!owner.canStepLeft() && !owner.canStepRight()) || strafeTimes > 2 || System.currentTimeMillis()-timeStarted> timescale)
		{			
			this.setState(Goal.State.COMPLETED);
			return this.getState();
		}
				
		return this.getState();
	}

	@Override
	public void terminate() {
		this.setState(Goal.State.COMPLETED);
		owner.stop();
		super.terminate();
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
	public String toString() {
		return this.getClass().getSimpleName()+": "+this.getState().toString();
	}	
	
	private boolean clockWise = false;
	private Point strafeDestination;
	private Bot target;
	private int strafeTimes = 0;
	private long timescale;
	private long timeStarted;
	private long tolerance = 1000;
}
;
