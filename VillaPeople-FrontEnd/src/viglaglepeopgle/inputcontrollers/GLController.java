package viglaglepeopgle.inputcontrollers;

import explicitlib.event.Action;
import explicitlib.event.Event;
import explicitlib.event.EventArgs;
import explicitlib.geometry.Point3D;
import explicitlib.geometry.Vector3D;
import viglaglepeopgle.display.MovingObserver;
import viglaglepeopgle.display.Scene;
import viglaglepeopgle.display.StaticObserver;
import villagepeople.entities.Player;

public abstract class GLController {

	public GLController(final Player player, final Scene scene)
	{
		this.player = player;
		this.scene = scene;
		
		onUserCamera().attach(
				new Action() {

					@Override
					public void execute(Object source, EventArgs args) {						
						scene.setObserver(
								new MovingObserver(new Point3D(player
										.getLocation().getX()-0.2, 0.75,
										-player.getLocation().getY()),
										new Vector3D(player
												.getHeadingVector()
												.getX(), 0, -player
												.getHeadingVector()
												.getY()), player));
					}

				});

		onWideCamera().attach(
				new Action() {

					@Override
					public void execute(Object source, EventArgs args) {						
						scene.setObserver(
								new StaticObserver(new Point3D(15, 20,
										-10),
										new Point3D(15, 0, -10)
												.minus(new Point3D(15,
														20, -10))));
					}

				});
	}
	
	public abstract void handleInputController();
	
    public Event onWideCamera() {
    	return onWideCamera;
    }
    
    public Event onUserCamera() {
    	return onUserCamera;
    }    
    
    /** Event for the wide Camera */
    protected Event onWideCamera = new Event(this);
    
    /** Event for the user Camera */
    protected Event onUserCamera = new Event(this);
    
    /** The player who is controlling */
    protected Player player = null;
    
    /** The scene */
	protected Scene scene = null;
}
