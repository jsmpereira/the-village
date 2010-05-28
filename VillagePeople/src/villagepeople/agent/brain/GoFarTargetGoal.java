package villagepeople.agent.brain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import villagepeople.agent.brain.actions.BotHitAction;
import villagepeople.agent.brain.actions.BotWallCollisionAction;
import villagepeople.entities.Bot;
import explicitlib.geometry.Point;
import explicitlib.graph.search.NoPathFoundException;

public class GoFarTargetGoal extends GoalComposite {

	public GoFarTargetGoal(Bot bot, Bot target, int maxCloser) {
		super(bot);
		this.target = target;
		this.closer = maxCloser;		
	}

	@Override
	public void activate() {
		this.setState(Goal.State.ACTIVE);
		List<Point> freePoints = owner.getGame().getMap().getFreeNeighbours(owner.getLocation());
		double best = 0;
		Point point = null;
		for(Point p: freePoints)
		{
			if(p.distanceTo(target.getLocation()) > best)
			{
				best = p.distanceTo(target.getLocation());
				point = p;
			}
		}			
		try {
			if(point!=null)
			{
				wayPoints = owner.getPathManager().getPathTo(point).getWayPoints();
				if (currentTarget >= wayPoints.size()) {
					this.setState(Goal.State.COMPLETED);
				}
				else {
				    this.attachActionToOwner(owner.onHit(), new BotHitAction(this));
		            this.attachActionToOwner(owner.onCollisionWithStaticEntity(), new BotWallCollisionAction(this));

					//this.addCallbackToOwner(new BotWallCollisionCallback(this));										
					
					owner.onHit().attach(new BotHitAction(this));
					addSubgoal(new SeekPositionGoal(owner, wayPoints.get(currentTarget)));					
					timescale = tolerance+(1000*((long)owner.getLocation().distanceTo(wayPoints.get(currentTarget))/(long)owner.getMaxSpeed())); 		
					timeStarted = System.currentTimeMillis();
				}
			}
			else
			{
				wayPoints = new ArrayList<Point>();
				System.out.println("Evade, e agora?");
			}
		} catch (NoPathFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public void terminate() {
		removeAllSubgoals();
		this.detachAllActions();
		super.terminate();
	}

	@Override
	public Goal getGoalInProcess() {		
		return this;
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
	//the destination target
	private Bot target;

	private int closer;
	private long timescale;
	private long timeStarted;
	private long tolerance = 500;	
}
