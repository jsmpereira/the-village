package villagepeople.agent.brain;

import villagepeople.agent.brain.actions.BotWallCollisionAction;
import villagepeople.entities.Bot;

public class AttackGoal extends GoalComposite {

	public AttackGoal(Bot agent) {
		super(agent);
		this.target = agent.getAttackTarget();
	}

	@Override
	public void activate() {
		this.setState(Goal.State.ACTIVE);
		removeAllSubgoals();
		//Se ele poder deslocar-se para a esquerda ou para a direita, faz Strafe
		if ((owner.canStepLeft() || owner.canStepRight())) {
			addSubgoal(new StrafeGoal(owner, target));
		}
		else if(owner.getLocation().distanceTo(target.getLocation()) < 2)
		{
			addSubgoal(new GoFarTargetGoal(owner, target, 2));
		} else {
            addSubgoal(new GoNearestTarget(owner, target, 2));
        }

		this.attachActionToOwner(owner.onCollisionWithStaticEntity(), new BotWallCollisionAction(this));
	}

	@Override
	public Goal.State process() {
		activateIfInactive();

		if(target==null)
		{
			this.setState(Goal.State.COMPLETED);
		}
		else if(target.isDead())
		{
			this.setState(Goal.State.COMPLETED);
		}
		else
		{
			this.setState(processSubgoals());
			reactivateIfFailed();
			//If no remaining aimmo this goal has failed
			if(owner.getTotalWeaponStrength()==0) {
                this.setState(Goal.State.FAILED);
			//else fire the target
            } else {
                owner.fireAt(target);
            }
		}

		return this.getState();
	}

	@Override
	public void terminate() {
		removeAllSubgoals();
		this.detachAllActions();
		super.terminate();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+": "+this.getState().toString();
	}

	private Bot target;
	private int closestDistance = 5;
}

