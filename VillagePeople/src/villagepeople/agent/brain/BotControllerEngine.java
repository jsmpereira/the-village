package villagepeople.agent.brain;

import villagepeople.entities.MovingEntity;

public interface BotControllerEngine {
	
	void startDriven(MovingEntity entity);
	void update(long timeElapsed);	
	
	public static enum Type {
		AI_CONTROLLER,
		DEFAULT_CONTROLLER
	};		
}
