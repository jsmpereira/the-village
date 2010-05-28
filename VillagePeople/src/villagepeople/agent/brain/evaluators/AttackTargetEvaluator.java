package villagepeople.agent.brain.evaluators;

import villagepeople.agent.brain.AttackGoal;
import villagepeople.agent.brain.GoalArbitrate;
import villagepeople.agent.brain.AIController;
import villagepeople.agent.brain.GoalThink;
import villagepeople.entities.Bot;

public class AttackTargetEvaluator extends GoalEvaluator {

	public AttackTargetEvaluator(Bot agent, GoalThink goal) {
		super(agent, goal);
	}

	public double calculateDesirability() {
		k = 0.1;
		Bot target = agent.getAttackTarget();
		
		if(target==null)			
		  return 0;		
		double x = (100*agent.getTotalWeaponStrength()/agent.getLocation().distanceTo(target.getLocation()))*k;				
		return x;
	}

	public void setGoal() {						
		goal.setGoal(new AttackGoal(agent));				
	}
}
