package villagepeople.entities;

import explicitlib.geometry.Point;
import villagepeople.agent.brain.DefaultController;
import villagepeople.entities.Bot;
import villagepeople.entities.Team;
import villagepeople.game.VillageGame;

public class Player extends Bot{

	public Player(VillageGame game, Point location, Team team) {
		super(game, location, team);
		this.controllerEngine = new DefaultController();
		this.controllerEngine.startDriven(this);
	}
	
	@Override
	public DefaultController getControllerEngine()
	{
		return (DefaultController)this.controllerEngine;
	}

}
