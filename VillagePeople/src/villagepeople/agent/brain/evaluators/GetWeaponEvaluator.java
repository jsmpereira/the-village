package villagepeople.agent.brain.evaluators;

import villagepeople.agent.brain.GetItemGoal;
import villagepeople.agent.brain.GoalArbitrate;
import villagepeople.agent.brain.GoalThink;
import villagepeople.entities.Bot;
import villagepeople.entities.FirstAidItem;
import villagepeople.entities.InventoryItem;
import villagepeople.entities.WeaponItem;
import explicitlib.graph.search.NoPathFoundException;
import explicitlib.graph.search.Path;

public class GetWeaponEvaluator extends GoalEvaluator{

	public GetWeaponEvaluator(Bot agent, GoalThink goal) {
		super(agent, goal);
		// TODO Auto-generated constructor stub
	}

	public double calculateDesirability() {
		k = 1;
		double x = ((this.normalizeValue(agent.getHealth(), 100, 1)*(1-agent.getTotalWeaponStrength()))/weaponDistance())*k;
		//System.out.println(this.toString()+" "+x+" Health: "+agent.getHealth()+" W: "+agent.totalWeaponStrength()+"D: "+weaponDistance());
		if(item!=null)
			return x;
		else return 0;
	}
	
	private double weaponDistance()
	{
		/*
		try {
			path = agent.getPathManager().getPathToWeapon();
			item = (WeaponItem)agent.getGame().getMap().getItemAt(path.getTarget());
			return path.getDistance()*path.getDistance();			
		} catch (NoPathFoundException e) {
			return Double.MAX_VALUE;
		}*/
		double best = Double.MAX_VALUE;
		for(WeaponItem item: agent.getGame().getMap().getWeaponItems())
		{
			double distance = agent.getLocation().distanceTo(item.getLocation());			
			if(distance < best)
			{
				best = distance;
				this.item = item; 
			}
		}
		if(item!=null)
			return best;
		else		
			return best;	
	}
	
	public void setGoal() {
		goal.setGoal(new GetItemGoal(agent, item, GetItemGoal.ItemType.WEAPON));
		
	}

	private WeaponItem item;
}
