package villagepeople.agent.brain;

import java.util.LinkedList;
import java.util.List;


import villagepeople.agent.brain.actions.BotHitAction;
import villagepeople.agent.brain.actions.BotWallCollisionAction;
import villagepeople.entities.Bot;
import villagepeople.events.HitEventArgs;
import villagepeople.weaponry.Projectile;
import explicitlib.event.Action;
import explicitlib.event.EventArgs;
import explicitlib.geometry.Point;
import explicitlib.geometry.Vector;
import explicitlib.graph.search.NoPathFoundException;

public class FollowPathGoal extends GoalComposite{

	public FollowPathGoal(Bot bot, Point destination) {
		super(bot);				
		try {			
			this.destination = destination;
			this.currentTarget = 0;
			this.wayPoints = owner.getPathManager().getPathTo(destination).getWayPoints();
		} catch (NoPathFoundException e) {		
			this.setState(Goal.State.FAILED);		
		}
	}

	@Override
	public void activate() {
		this.setState(Goal.State.ACTIVE);
		if (currentTarget >= wayPoints.size()) {
			this.setState(Goal.State.COMPLETED);
		}
		else {	
		    this.attachActionToOwner(owner.onHit(), new BotHitAction(this));
		    this.attachActionToOwner(owner.onCollisionWithStaticEntity(), new BotWallCollisionAction(this));

			addSubgoal(new SeekPositionGoal(owner, wayPoints.get(currentTarget)));				
			addSubgoal(new RotateFacing(owner, wayPoints.get(currentTarget)));	
			timescale = tolerance+(1000*((long)owner.getLocation().distanceTo(wayPoints.get(currentTarget))/(long)owner.getMaxSpeed())); 		
			timeStarted = System.currentTimeMillis();
		}
	}

	@Override
	public villagepeople.agent.brain.Goal.State process() {
		activateIfInactive();
		this.setState(processSubgoals());
		
		if (currentTarget >= wayPoints.size()) {	
			this.setState(Goal.State.COMPLETED);
			return this.getState();
		}	
		
		if(System.currentTimeMillis()-timeStarted > timescale)
		{			
			this.setState(Goal.State.COMPLETED);	
			return this.getState();
		}		
		
		//If the agent is near the current waypoint, try to reach the next one
		if (owner.getLocation().distanceTo(wayPoints.get(currentTarget)) <= WAYPOINT_SEEK_DISTANCE && currentTarget < wayPoints.size()) {						
			currentTarget++;			
			this.setState(Goal.State.INACTIVE);			
		}					
			
		return this.getState();
	}
	
	@Override
	public Goal getGoalInProcess() {		
		return this;
	}
	
	@Override
	public void terminate() {
		removeAllSubgoals();
		this.detachAllActions();
		super.terminate();
	}

	@Override
	public String toString() {		
		return this.getClass().getSimpleName()+": "+this.getState().toString();
	}
	
	//When is the agent sufficiently near the current waypoint?
	public final double WAYPOINT_SEEK_DISTANCE = 0;	
	// The path to follow
	private List<Point> wayPoints;	
	// The index of the current waypoint
	private int currentTarget = 0;		
	//the destination point
	private Point destination;

	private long timescale;
	private long timeStarted;
	private long tolerance = 500;	
}
