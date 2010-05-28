package viglaglepeopgle.entities;

import viglaglepeopgle.renderable.Box;
import viglaglepeopgle.renderable.ModelRenderer;
import viglaglepeopgle.settings.Resources;
import villagepeople.entities.Bot;
import villagepeople.entities.Team;
import explicitlib.geometry.Point3D;
import explicitlib.geometry.Size3D;
import explicitlib.geometry.Vector;
import explicitlib.geometry.Vector3D;

/**
 * GLEntity for a Bot.
 *
 * Used to display a bot with OpenGL.
 */
public class GLBot extends GLMovingEntity {

    /**
     * Creates a new GLBot for a given bot in the game.
     *
     * @param bot The corresponding bot.
     */
    public GLBot(Bot bot) {
        super(bot);

        this.model = new ModelRenderer(Resources.getModel("duke"),
                new Point3D(0, 0, 0));
        this.model.setAnimation("run");

        //Joel Stuff
        if(bot.getTeam().getColor() != Team.NULL)
        {
        	Size3D boxSize = new Size3D((float) 0.1,
        			(float) 0.1,
        			(float) 0.02);
        	this.box = new Box(boxSize, new Point3D(0, 0, 0),
        			new Vector3D(0, 1, 0));
        	
        	switch (bot.getTeam().getColor()) {
        	case Team.GREEN:
        		this.box.setColor(new float[] { 0, 1, 0 });
        	}
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void render() {
        model.render();
        if(((Bot)this.entity).getTeam().getColor() != Team.NULL)
        	box.render();
    }


    /**
     * {@inheritDoc}
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

        if(((Bot)this.entity).getTeam().getColor() != Team.NULL)
        {

        	//Joel Stuff
        	Bot Bot = (Bot) this.entity;
        	Point3D boxLocation = new Point3D(
        			bot.getLocation().getX(),
        			1,
        			-bot.getLocation().getY());
        	box.setRotation((float) bot.getRotation());
        	box.setCenter(boxLocation);
        }
    }


    /**
     * @return the model that represents this bot on screen.
     */
    public ModelRenderer getModel() {
        return this.model;
    }


    /** The model that represents this bot on screen. */
    protected ModelRenderer model;
    
    //Joel stuff
    /** Box that represents of wich team a bot belong */
    private Box box = null;
}
