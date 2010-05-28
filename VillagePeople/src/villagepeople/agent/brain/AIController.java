package villagepeople.agent.brain;

import villagepeople.entities.Bot;
import villagepeople.entities.MovingEntity;

public class AIController implements BotControllerEngine{

	@Override
	public void startDriven(MovingEntity entity) {		
		brain = new GoalThink((Bot)entity);
		brain.activate();
		this.time = System.currentTimeMillis();
	}	
	
	@Override
	public void update(long timeElapsed) {		
		if(timeElapsed-this.time < 100)
		{
			brain.process();
			this.time = timeElapsed;
		}
	}
	
	private GoalThink brain;
	private long time;
}
