package villagepeople.agent.brain.evaluators;

import villagepeople.agent.brain.GoalArbitrate;
import villagepeople.entities.Bot;

public abstract class GoalEvaluator {	
	
	public GoalEvaluator(Bot agent, GoalArbitrate goal)
	{
		this.agent = agent;
		this.goal = goal;
	}
	
	protected double normalizeValue(double value, double actualMaxValue, double normalize)
	{		
		double x = (value*normalize)/actualMaxValue;
		if(x < 1 && normalize != 1)
			return 1;
		return x;
	}
	
	public String toString()
	{
		return this.getClass().getSimpleName();
	}
	
	public abstract double calculateDesirability();
	public abstract void setGoal();
	
	protected double k = 0;
	protected GoalArbitrate goal;
	protected Bot agent;
}
