package villagepeople.agent.brain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import villagepeople.entities.Bot;

public abstract class GoalComposite extends Goal<Bot>{

	public GoalComposite(Bot bot)
	{
		super(bot);
		subgoals = new LinkedList<Goal>();
	}	

	@Override
	public void addSubgoal(Goal goal)
	{
		//add the goal to the front of the subgoal list
		subgoals.addFirst(goal);
	}
	
	@Override
	public Goal getGoalInProcess() {		
		if(!this.subgoals.isEmpty())
			return this.subgoals.getFirst().getGoalInProcess();
		else return this;
	}
	
	protected void removeAllSubgoals()
	{						
		//Call the terminate method of all the goal in the subgoal list, and then
		//clear the list
		for(Goal goal: subgoals)		
			goal.terminate();		
		subgoals.clear();

	}
	
	protected Goal.State processSubgoals()
	{
		//Remove all completed and failes goals from the front of the subgoal list
		while(!subgoals.isEmpty() && (subgoals.getFirst().isCompleted() || subgoals.getFirst().hasFailed()))
		{
			subgoals.getFirst().terminate();
			subgoals.removeFirst();			
		}		
		//if any subgoal remain, process the one at the front of the list
		if(!subgoals.isEmpty())
		{
			//Grab the status of the frontmost goal
			Goal.State status = subgoals.getFirst().process();
			//We have to test for the special case where  the frontmost subgoal
			//reports completed  and the subgoal  list contains additional goals.
			//When this is the case, to ensure the parent  keeps processing its subgoal list
			//we most return the status "active"
			if(status == Goal.State.COMPLETED && subgoals.size()==0)
				return Goal.State.COMPLETED;			
			return Goal.State.ACTIVE;						
		}
		//no more subgoals to process, return completed
		else return Goal.State.COMPLETED;
	}
	
	protected LinkedList<Goal> subgoals;	
}
