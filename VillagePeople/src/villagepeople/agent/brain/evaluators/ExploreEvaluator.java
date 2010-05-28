package villagepeople.agent.brain.evaluators;

import villagepeople.agent.brain.ExploreGoal;
import villagepeople.agent.brain.GoalThink;
import villagepeople.entities.Bot;

public class ExploreEvaluator extends GoalEvaluator{

	public ExploreEvaluator(Bot agent, GoalThink goal) {
		super(agent, goal);
		// TODO Auto-generated constructor stub
	}

	public double calculateDesirability() {		
		return 0.1;
	}

	public void setGoal() {
		goal.setGoal(new ExploreGoal(agent));
	}

}
