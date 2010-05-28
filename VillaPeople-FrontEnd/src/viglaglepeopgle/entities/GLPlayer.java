package viglaglepeopgle.entities;

import explicitlib.geometry.Point3D;
import explicitlib.geometry.Vector;
import viglaglepeopgle.inputcontrollers.GLController;
import villagepeople.entities.Bot;
import villagepeople.entities.Player;

public class GLPlayer extends GLBot {

	public GLPlayer(Player player) {
		super(player);				
	}

    /**
     * {@inheritDoc}
     */
	/**
	 * @Todo super.update
	 */
    @Override
    public void update(long timeElapsed) {
        Bot bot = (Bot) this.entity;

        Point3D location = new Point3D(
                bot.getLocation().getX(),
                0,
                -bot.getLocation().getY());

        model.setRotation((float) bot.getRotation());
        model.setLocation(location);

        Vector velocity = bot.getVelocity();
        if (velocity.getX() == 0 && velocity.getY() == 0) {
            model.setAnimation("jetpack");
        } else {
            model.setAnimation("run");
        }

        model.setFps((int) (bot.getVelocity().magnitude() * 16));
        model.step(timeElapsed);
        glController.handleInputController();        
    }    

	public void setGlController(GLController glController) {
		this.glController = glController;
	} 
    
	public GLController getGlController() {
		return this.glController;
	}
	
    private GLController glController = null;
}
