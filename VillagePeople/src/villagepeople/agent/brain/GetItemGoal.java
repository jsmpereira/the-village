package villagepeople.agent.brain;

import java.util.ArrayList;
import java.util.List;

import villagepeople.agent.brain.actions.BotWallCollisionAction;
import villagepeople.entities.Bot;
import villagepeople.entities.FirstAidItem;
import villagepeople.entities.InventoryItem;
import villagepeople.entities.WeaponItem;
import villagepeople.events.ItemCaughtEventArgs;
import explicitlib.event.Action;
import explicitlib.event.EventArgs;
import explicitlib.graph.search.NoPathFoundException;
import explicitlib.graph.search.Path;

public class GetItemGoal extends GoalComposite{

    public enum ItemType {
        FIRST_AID, WEAPON
    }
    
	public GetItemGoal(Bot bot, InventoryItem item, ItemType type) {
		super(bot);
		this.type = type;
		this.item = item;		
	}

	@Override
	public void activate() {		
		this.setState(Goal.State.ACTIVE);	
		
		// Terminate the goal when the item is caught
        this.attachActionToOwner(owner.onCatchItem(), new Action() {
            @Override
            public void execute(Object source, EventArgs args) {
                InventoryItem item = ((ItemCaughtEventArgs) args).getItem();
                
                if (type == ItemType.FIRST_AID
                        && item instanceof FirstAidItem) {
                    setState(Goal.State.COMPLETED);
                } else if (type == ItemType.WEAPON
                        && item instanceof WeaponItem) {
                    setState(Goal.State.COMPLETED);
                }
            }
        });
				
        this.attachActionToOwner(owner.onCollisionWithStaticEntity(), new BotWallCollisionAction(this));
        
		//this.addCallbackToOwner(new BotWallCollisionCallback(this));
		addSubgoal(new FollowPathGoal(owner, owner.getGame().getMap().cellAt(this.item.getLocation()).getCenter()));		
	}
	
	@Override
	public Goal.State process() {		
		activateIfInactive();	
		this.setState(processSubgoals());				
		if(owner.getLocation().equals(this.item.getLocation()))
		{						
			this.setState(Goal.State.COMPLETED);
		}
		return this.getState();
	}

	@Override
	public void terminate() {		
		this.removeAllSubgoals();	
		this.detachAllActions();
		super.terminate();
	}
	
	@Override
	public String toString() {			
		return this.getClass().getSimpleName()+": "+this.getState();
	}

	private ItemType type;
	private int actualHealth;
	private InventoryItem item;	
}
