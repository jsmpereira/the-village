package villagepeople.agent.brain;

import explicitlib.event.Action;
import explicitlib.event.Event;
import explicitlib.event.EventArgs;
import explicitlib.geometry.Vector;
import villagepeople.entities.MovingEntity;
import villagepeople.entities.Player;

public class DefaultController implements BotControllerEngine{

	@Override
	public void startDriven(MovingEntity entity) {		
		this.player = (Player) entity;
		
		this.onFire().attach(new Action() {

			@Override
			public void execute(Object source, EventArgs args) {
				player.fire();
			}
			
		});
		
		this.onHeadingLeft.attach(new Action() {

			@Override
			public void execute(Object source, EventArgs args) {
				player.rotateFacing(player.getHeadingVector().plus(new Vector(-0.5,0)).reverse());	
			}
			
		});
		
		this.onHeadingRight().attach(new Action() {

			@Override
			public void execute(Object source, EventArgs args) {
				player.rotateFacing(player.getHeadingVector().plus(new Vector(0.5,0)).reverse());
			}
			
		});
		
		this.onWalkUp().attach(new Action() {

			@Override
			public void execute(Object source, EventArgs args) {
				player.seek(player.getLocation().plus(player.getHeadingVector()));	
			}
			
		});
		
		this.onWalkDown().attach(new Action() {

			@Override
			public void execute(Object source, EventArgs args) {
				player.seek(player.getLocation().minus(player.getHeadingVector().toPoint()).toPoint());				
			}
			
		});
		
		this.onWalkLeft().attach(new Action() {

			@Override
			public void execute(Object source, EventArgs args) {
				player.seek(player.getLocation().plus(player.getHeadingVector().perp().toPoint()));				
			}
			
		});
		
		this.onWalkRight().attach(new Action() {

			@Override
			public void execute(Object source, EventArgs args) {
				player.seek(player.getLocation().minus(player.getHeadingVector().perp().toPoint()).toPoint());				
			}
			
		});
		
		this.onSelectWeapon().attach(new Action() {						
			private int w = 0;
			@Override
			public void execute(Object source, EventArgs args) {
				w++;
				if(w>3) w = 1;
				player.chooseWeapon(w);						
			}
			
		});
	}

	@Override
	public void update(long timeElapsed) {
		
	}		
	
	public Event onFire() {
		return onFire;
	}

	public Event onWalkLeft() {
		return onWalkLeft;
	}

	public Event onWalkRight() {
		return onWalkRight;
	}

	public Event onWalkUp() {
		return onWalkUp;
	}

	public Event onWalkDown() {
		return onWalkDown;
	}

	public Event onHeadingLeft() {
		return onHeadingLeft;
	}

	public Event onHeadingRight() {
		return onHeadingRight;
	}

	public Event onSelectWeapon() {
		return onSelectWeapon;
	}

	private Player player = null;
	
	public static final int HEADING_UP = 0, HEADING_DOWN = 1, HEADING_LEFT = 2, HEADING_RIGHT = 3, FIRE = 4, WALK_UP = 5, WALK_DOWN = 6, WALK_LEFT = 7, WALK_RIGHT = 8, SELECT_WEAPON = 9;
	
	private Event onFire = new Event(this);
	private Event onWalkLeft = new Event(this);
	private Event onWalkRight = new Event(this);
	private Event onWalkUp = new Event(this);
	private Event onWalkDown = new Event(this);
	private Event onHeadingLeft = new Event(this);
	private Event onHeadingRight = new Event(this);
	private Event onSelectWeapon = new Event(this);	
}


