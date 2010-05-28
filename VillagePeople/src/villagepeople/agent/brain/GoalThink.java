package villagepeople.agent.brain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import villagepeople.agent.brain.evaluators.AttackTargetEvaluator;
import villagepeople.agent.brain.evaluators.ExploreEvaluator;
import villagepeople.agent.brain.evaluators.GetHealthEvaluator;
import villagepeople.agent.brain.evaluators.GetWeaponEvaluator;
import villagepeople.agent.brain.evaluators.GoalEvaluator;
import villagepeople.entities.Bot;
import villagepeople.entities.StaticEntity;

public class GoalThink extends GoalArbitrate{

	public GoalThink(Bot bot) {
		super(bot);		
	}

	@Override
	protected void initEvaluators() {							
		this.evaluators.add(new ExploreEvaluator(owner, this));
		this.evaluators.add(new GetHealthEvaluator(owner, this));
		this.evaluators.add(new GetWeaponEvaluator(owner, this));
		this.evaluators.add(new AttackTargetEvaluator(owner, this));
	}				
	
	@Override
	public String toString() {										
		return this.getClass().getSimpleName()+": "+this.getState().toString();
	}
	
}
