package villagepeople.agent.brain.evaluators;

import villagepeople.agent.brain.GetItemGoal;
import villagepeople.agent.brain.GoalThink;
import villagepeople.entities.Bot;
import villagepeople.entities.FirstAidItem;
import villagepeople.entities.InventoryItem;
import explicitlib.graph.search.Path;

public class GetHealthEvaluator extends GoalEvaluator{

	public GetHealthEvaluator(Bot agent, GoalThink goal) {
		super(agent, goal);
	}

	public double calculateDesirability() {		
		item = null;		
		double x = ((100-agent.getHealth())/healthDistance());				
		if(item!=null)
			return x;
		else return 0;		
	}

	public void setGoal() {
		goal.setGoal(new GetItemGoal(agent, item, GetItemGoal.ItemType.FIRST_AID));
	}
	
	private double healthDistance()
	{	
		/*		
		try {
			path = agent.getPathManager().getPathToHealthItem();
			item = (HealthItem)agent.getGame().getMap().getItemAt(path.getTarget());
			return path.getDistance()*path.getDistance();
		} catch (NoPathFoundException e) {
			return Double.MAX_VALUE;
		}*/				
		double best = Double.MAX_VALUE;
		for(FirstAidItem item: agent.getGame().getMap().getHealthItems())
		{
			double distance = agent.getLocation().distanceTo(item.getLocation());			
			if(distance < best)
			{
				best = distance;
				this.item = item; 
			}
		}
		if(item!=null)
			return best*best;
		else		
			return best;					
	}

	private FirstAidItem item;	
}
