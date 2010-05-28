package villagepeople.agent.brain;

import java.util.ArrayList;

import villagepeople.agent.brain.evaluators.GoalEvaluator;
import villagepeople.entities.Bot;

public abstract class GoalArbitrate extends GoalComposite{

	public GoalArbitrate(Bot bot) {
		super(bot);	
		this.evaluators = new ArrayList<GoalEvaluator>();
		initEvaluators();
	}

	@Override
	public void activate() {
		this.setState(Goal.State.ACTIVE);			
		this.arbitrate();		
	}	
	
	@Override
	public Goal.State process() {			
		activateIfInactive();
		Goal.State st = this.processSubgoals();
		if(this.isCompleted() || this.hasFailed())					
			this.setState(Goal.State.INACTIVE);		
		else 
			arbitrate();				
		return this.getState();
	}
	
	public void arbitrate()
	{			
		best = 0;
		mostDesirable = null;		
		//iterate through all the evaluators to see which produces the highest score
		for(GoalEvaluator eval: this.evaluators)
		{			
			double desirability = eval.calculateDesirability();			
			if(desirability >= best)
			{
				best = desirability;
				mostDesirable = eval;
			}
		}				
		mostDesirable.setGoal();		
	}
	
	public void setGoal(Goal goal)
	{
		
		if(!subgoals.isEmpty())
		{		
			if(!(subgoals.getFirst().getClass().getSimpleName().equals(goal.getClass().getSimpleName())))
			{				
				//removeAllSubgoals();				
				this.addSubgoal(goal);
			}
		}
		else				
			this.addSubgoal(goal);
					
	}
	
	public String getMostDesirable()
	{
		if(this.isActive())
			return this.mostDesirable.toString()+": "+this.best;
		else return "";
	}
	
	@Override
	public void terminate() {
		this.setState(Goal.State.COMPLETED);
		super.terminate();
	}
		
	protected abstract void initEvaluators();	
	protected ArrayList<GoalEvaluator> evaluators;		
	private double best;
	private GoalEvaluator mostDesirable;
	
}
